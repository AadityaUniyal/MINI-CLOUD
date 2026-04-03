# 🔄 MiniCloud — Architecture & Working Flow

> This document explains how a request travels through the MiniCloud system end-to-end, and which technology handles each stage.

---

## 1. Entry Points

MiniCloud has **two entry points** — both talk to the same backend:

| Entry Point | Technology | Purpose |
|---|---|---|
| **JavaFX Desktop Console** | JavaFX 21 | Native GUI — users manage resources visually |
| **REST API** | Spring Boot 3.2.4 | Programmatic access (curl, Postman, integrations) |

---

## 2. End-to-End Request Flow

```
┌──────────────────────────────────────────────────────────┐
│  Step 1 — User Action                                     │
│  JavaFX Console or REST Client sends an HTTP request      │
│  Tech: JavaFX (HttpClient) / Postman / curl              │
└───────────────────────────┬──────────────────────────────┘
                            │ HTTP + Bearer JWT
┌───────────────────────────▼──────────────────────────────┐
│  Step 2 — Security Gate                                   │
│  Every request hits the JWT filter first.                 │
│  • Validates the token signature and expiry               │
│  • Extracts the authenticated user identity               │
│  Tech: Spring Security + JJWT 0.11.5                     │
│  Files: JwtFilter.java, JwtUtil.java, SecurityConfig.java │
└───────────────────────────┬──────────────────────────────┘
                            │ Authorized request
┌───────────────────────────▼──────────────────────────────┐
│  Step 3 — REST Controller Layer                           │
│  Routes request to the correct service (22 controllers).  │
│  Validates DTOs, reads path/query params.                 │
│  Tech: Spring Boot Web (Spring MVC)                       │
│  Files: controller/ package                               │
└───────────────────────────┬──────────────────────────────┘
                            │ Calls service method
┌───────────────────────────▼──────────────────────────────┐
│  Step 4 — Service / Business Logic Layer                  │
│  Core intelligence of the platform lives here.            │
│  Each service handles its domain independently.           │
│  Tech: Plain Java + Spring @Service beans                 │
│  Files: service/ package                                  │
│                                                           │
│  ┌─────────────────────────────────────────────────────┐  │
│  │ Compute / Lambda / Orchestration                    │  │
│  │  → calls DockerService → talks to Docker Engine     │  │
│  │  Tech: docker-java SDK 3.3.4                        │  │
│  ├─────────────────────────────────────────────────────┤  │
│  │ Object Storage / Volumes                            │  │
│  │  → reads/writes files on local disk                 │  │
│  │  Tech: Java NIO (java.nio.file, Path, Files)        │  │
│  ├─────────────────────────────────────────────────────┤  │
│  │ Database / DynamoDB                                 │  │
│  │  → provisions MySQL containers via Docker           │  │
│  │  → stores table metadata in H2                     │  │
│  │  Tech: docker-java + Spring Data JPA                │  │
│  ├─────────────────────────────────────────────────────┤  │
│  │ IAM / Security / Billing / Audit                    │  │
│  │  → pure business logic, persists to H2              │  │
│  │  Tech: Spring Data JPA + Hibernate                  │  │
│  ├─────────────────────────────────────────────────────┤  │
│  │ Networking (VPC / Subnet / DNS / Firewall / LB)     │  │
│  │  → stores topology metadata in H2                   │  │
│  │  Tech: Spring Data JPA + Round Robin (Java)         │  │
│  ├─────────────────────────────────────────────────────┤  │
│  │ Messaging (SQS / SNS)                               │  │
│  │  → in-memory queues and topic subscriptions         │  │
│  │  Tech: Java ConcurrentLinkedQueue / Observer        │  │
│  └─────────────────────────────────────────────────────┘  │
└───────────────────────────┬──────────────────────────────┘
                            │ Read / Write
┌───────────────────────────▼──────────────────────────────┐
│  Step 5 — Persistence Layer                               │
│  All resource state is persisted here.                    │
│  Tech: Spring Data JPA + Hibernate ORM + H2 Database      │
│  Files: repository/ package, model/ package               │
│  Console: http://localhost:8080/h2-console                │
└───────────────────────────┬──────────────────────────────┘
                            │ JSON response
┌───────────────────────────▼──────────────────────────────┐
│  Step 6 — Response                                        │
│  Controller serializes result to JSON and sends back.     │
│  Tech: Jackson (via Spring Boot Web)                      │
│  JavaFX console updates its TableView / charts live.      │
└──────────────────────────────────────────────────────────┘
```

---

## 3. Background Services (Always Running)

These services run on startup independently of user requests:

| Service | Tech | What It Does |
|---|---|---|
| **HealthCheckService** | Java `@Scheduled` + docker-java | Polls every 10s, auto-restarts crashed containers |
| **MetricCollectionTask** | Java `@Scheduled` + StatsService | Persists container metrics to H2 every 30s |
| **CloudTrailService** | Spring AOP / JPA | Records every user action to the audit log table |
| **CloudMetadataService** | Spring Boot + JPA | Exposes runtime metadata for running instances |

---

## 4. Tech Stack — Where Each Technology Is Used

| Technology | Used In | Why |
|---|---|---|
| **Java 17** | Entire codebase | Core language |
| **Spring Boot 3.2.4** | Backend server, all REST APIs | Rapid development, auto-configuration |
| **Spring Security** | `JwtFilter`, `SecurityConfig` | Intercepts every request, enforces auth |
| **JJWT 0.11.5** | `JwtUtil`, `AuthService` | Signs and validates JWT tokens |
| **Spring Data JPA** | All `*Repository` files | ORM — maps Java objects to H2 tables |
| **Hibernate** | Under JPA | SQL generation, session management |
| **H2 (embedded)** | `application.properties` | In-memory relational DB; no setup required |
| **docker-java 3.3.4** | `DockerService`, `ComputeService`, `DatabaseService` | Controls Docker containers from Java |
| **Java NIO** | `BucketService`, `VolumeService` | File system operations for object storage |
| **JavaFX 21** | `ui/` package | Native desktop management console |
| **Thymeleaf** | Web views (if any) | Server-side HTML templating |
| **JAXB 2.3.1** | XML processing (docker-java dependency) | XML binding for Docker API responses |
| **Apache Maven 3.9.6** | `pom.xml` | Dependency management and build lifecycle |
| **Jackson** | Auto-configured by Spring Boot | JSON serialization / deserialization |
| **Docker Engine** | Runtime container environment | Hosts Tomcat, MySQL, Lambda containers |

---

## 5. Authentication Flow (Detailed)

```
Client                    JwtFilter              AuthService          DB (H2)
  │                           │                       │                  │
  │── POST /api/auth/login ──▶│                       │                  │
  │                           │── pass through ──────▶│                  │
  │                           │                       │── find user ────▶│
  │                           │                       │◀── user entity ──│
  │                           │                       │── verify password│
  │                           │                       │── generate JWT   │
  │◀──────────── { token } ───│◀─────────────────────│                  │
  │                           │                       │                  │
  │── GET /api/compute/instances (Bearer token) ─────▶│                  │
  │                           │── validate JWT        │                  │
  │                           │── set SecurityContext │                  │
  │                           │── forward to Controller                  │
```

---

## 6. Container Lifecycle (Compute / Database)

```
User: POST /api/compute/launch
        │
        ▼
ComputeController.launch()
        │
        ▼
ComputeService.launchInstance()
  ├── Generate unique name + port
  ├── Build DockerCreateContainerCmd (via docker-java)
  ├── Pull image if not cached (tomcat:latest / mysql:8)
  ├── Start container
  ├── Save ComputeInstance entity → H2 via JPA
  └── Return instance metadata to client

Background:
HealthCheckService (every 10s)
  ├── List all instances from H2
  ├── Inspect container status via docker-java
  └── If stopped → docker.startContainer(id) [auto-heal]
```

---

## 7. Object Storage Flow (Buckets)

```
User: POST /api/storage/upload (multipart file)
        │
        ▼
BucketController.uploadFile()
        │
        ▼
BucketService.storeFile()
  ├── Resolve target path: storage/<owner>/<bucket>/<filename>
  ├── Create directories if missing (Java NIO: Files.createDirectories)
  ├── Write bytes to disk (Files.copy / Files.write)
  ├── Save StorageFile entity → H2 (metadata: name, size, type, path)
  └── Return file URL / metadata

#### [NEW] Virtual Web Hosting (S3)
- MiniCloud can now serve files from S3 buckets as a static website.
- Supports `index.html` resolution and automatic MIME type detection (HTML/CSS/JS).
- Accessible via `/api/storage/website/{bucketName}/`.

Tech: java.nio.file.Path, java.nio.file.Files, MediaType resolution
```

---

## 8. Stack Deployment Flow (Orchestration)

```
User: POST /api/orchestration/deploy-stack
        │
        ▼
OrchestrationController
        │
        ▼
OrchestrationService.deployStack()
  ├── 1. ComputeService.launchInstance()   → Tomcat container
  ├── 2. DatabaseService.provision()       → MySQL container
  ├── 3. LoadBalancerService.create()      → LB record pointing to instance
  └── 4. Return combined stack summary

Single API call → full environment provisioned in ~5 seconds
```
