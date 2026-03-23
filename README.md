# ☁️ MiniCloud — Java-Based Cloud Infrastructure Platform

> A simplified, locally-running cloud platform that simulates core AWS services — built entirely with a **Pure Java** tech stack as a semester PBL project.

---

## 📌 Problem Statement

Modern cloud platforms (AWS, GCP, Azure) are powerful but opaque and expensive for students and small teams. **MiniCloud** is a desktop cloud console that exposes the same core cloud primitives — Compute, Storage, Databases, Load Balancing, and IAM — using nothing but Java, Docker, and a modern Spring Boot backend. The goal is to make cloud infrastructure tangible, scriptable, and locally deployable.

---

## 🚀 Core Services

| Service | AWS Equivalent | Technology |
|---|---|---|
| **Compute** | EC2 | Docker + docker‑java API |
| **Object Storage** | S3 | Java NIO (`java.nio.file`) |
| **Managed Database** | RDS | Docker (MySQL containers) |
| **Load Balancer** | ELB | Spring Cloud Gateway / Java Reverse Proxy |
| **IAM & Auth** | IAM + Cognito | Spring Security + JWT |
| **Monitoring & Billing** | CloudWatch + Cost Explorer | Docker Stats API + `@Scheduled` |

---

## 🏗️ System Architecture

```
JavaFX Desktop App (Frontend)
        │
        │  REST API (JSON + JWT)
        ▼
Spring Boot 3.x Backend
   ├── AuthController      → AuthService       → H2 Database (JPA)
   ├── ComputeController   → DockerService      → Docker Daemon (containers)
   ├── StorageController   → BucketService      → Local File System (NIO)
   ├── DatabaseController  → DatabaseService    → Docker Daemon (MySQL)
   ├── LoadBalancerController → LoadBalancerService → Spring Cloud Gateway
   ├── StatsController     → StatsService       → Docker Stats API
   └── OrchestrationController → OrchestrationService → All of the above
```

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|---|---|---|
| **Language** | Java | 17 |
| **Backend Framework** | Spring Boot | 3.2.4 |
| **Security** | Spring Security + JJWT | 0.11.5 |
| **ORM / DB** | Spring Data JPA + H2 | Embedded |
| **Container Engine** | docker‑java | 3.3.4 |
| **Frontend** | JavaFX | 21 |
| **Build Tool** | Apache Maven | 3.x |

---

## 📂 Project Structure

```
JAVA-PBL/
├── planning/                        # Team planning and architecture docs
│   ├── overview.txt
│   ├── plan.txt
│   ├── system_architecture.md
│   ├── features.txt
│   ├── team_structure.txt
│   ├── week1_plan.txt
│   ├── week2_plan.txt
│   ├── week2_3_overview.txt
│   ├── member1_week2_3.txt
│   ├── member2_week2_3.txt
│   └── member3_week2_3.txt
│
├── src/
│   ├── main/
│   │   ├── java/com/minicloud/
│   │   │   ├── MiniCloudApplication.java
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java
│   │   │   │   ├── DockerConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── ComputeController.java
│   │   │   │   ├── StorageController.java
│   │   │   │   ├── DatabaseController.java
│   │   │   │   ├── StatsController.java
│   │   │   │   ├── LoadBalancerController.java
│   │   │   │   └── OrchestrationController.java
│   │   │   ├── dto/
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── LaunchRequest.java
│   │   │   │   ├── LaunchResponse.java
│   │   │   │   ├── ProvisionDbRequest.java
│   │   │   │   ├── DeployStackRequest.java
│   │   │   │   ├── DeployStackResponse.java
│   │   │   │   └── StatsResponse.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── ComputeInstance.java
│   │   │   │   ├── Bucket.java
│   │   │   │   ├── StorageFile.java
│   │   │   │   ├── DatabaseInstance.java
│   │   │   │   ├── LoadBalancer.java
│   │   │   │   └── AuditLog.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ComputeInstanceRepository.java
│   │   │   │   ├── BucketRepository.java
│   │   │   │   ├── StorageFileRepository.java
│   │   │   │   ├── DatabaseInstanceRepository.java
│   │   │   │   ├── LoadBalancerRepository.java
│   │   │   │   └── AuditLogRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtFilter.java
│   │   │   │   ├── JwtUtil.java
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── service/
│   │   │       ├── AuthService.java
│   │   │       ├── DockerService.java
│   │   │       ├── BucketService.java
│   │   │       ├── DatabaseService.java
│   │   │       ├── HealthCheckService.java
│   │   │       ├── StatsService.java
│   │   │       ├── LoadBalancerService.java
│   │   │       └── OrchestrationService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/minicloud/
│           ├── controller/
│           │   ├── AuthControllerTest.java
│           │   └── ComputeControllerTest.java
│           └── service/
│               ├── DockerServiceTest.java
│               ├── BucketServiceTest.java
│               └── DatabaseServiceTest.java
│
├── pom.xml
└── README.md
```

---

## ⚙️ Prerequisites

Before running the project, ensure you have the following installed:

- **JDK 17+** — [Download](https://adoptium.net/)
- **Apache Maven 3.8+** — [Download](https://maven.apache.org/)
- **Docker Desktop** — [Download](https://www.docker.com/products/docker-desktop/) *(must be running)*

---

## 🏁 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/your-org/minicloud.git
cd minicloud
```

### 2. Start Docker Desktop
Make sure Docker is running before starting the backend — the application connects to the Docker daemon on startup.

### 3. Run the Spring Boot backend
```bash
mvn spring-boot:run
```
The backend starts on **`http://localhost:8080`**.

### 4. Access the H2 Console (dev only)
Navigate to `http://localhost:8080/h2-console` and connect with the credentials defined in `application.properties`.

---

## 📡 Key API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive JWT token |
| `POST` | `/api/compute/launch` | Launch a new Tomcat container |
| `GET` | `/api/compute/instances` | List all compute instances |
| `POST` | `/api/storage/upload` | Upload a file to a bucket |
| `GET` | `/api/storage/files` | List files in a bucket |
| `POST` | `/api/database/provision` | Provision a MySQL container |
| `GET` | `/api/databases` | List all database instances |
| `POST` | `/api/orchestration/deploy-stack` | Deploy a full Web + DB + LB stack |
| `GET` | `/api/stats/{containerId}` | Get live CPU/RAM metrics |

> **Authentication**: All endpoints (except `/api/auth/**`) require a `Bearer <JWT>` header.

---

## 👥 Team Structure & Work Division

| Member | Role | Core Responsibility |
|---|---|---|
| **Member 1** | Hardware Lead | Services layer, Docker logic, Health Check engine, Java NIO storage |
| **Member 2** | Network Lead | REST Controllers, Load Balancer, Orchestration API, Metrics endpoints |
| **Member 3** | UX Architect | JavaFX frontend, Dashboard UI, Website Wizard, Operations Monitor |

### 8-Week Timeline Summary

| Phase | Weeks | Goal |
|---|---|---|
| Phase 1 | 1–3 | **70% Functional** — IAM, Compute, Storage, Database, Load Balancer |
| Phase 2 | 4–5 | Real-time Monitoring & Billing |
| Phase 3 | 6–7 | Polish, Error Handling, Dark Mode, Animations |
| Phase 4 | 8 | JUnit Tests, Postman, `jpackage` Installer, Demo Video |

---

## 🧪 Running Tests

```bash
mvn test
```

---

## 📦 Building a Standalone Package

```bash
mvn package
# Output: target/minicloud-backend-0.0.1-SNAPSHOT.jar
java -jar target/minicloud-backend-0.0.1-SNAPSHOT.jar
```

---

## 📄 License

This project is developed for academic purposes as part of a semester Project-Based Learning (PBL) course.
