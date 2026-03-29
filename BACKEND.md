# 🧠 MiniCloud — Backend Deep Dive

> What each backend package, class, and layer does — in short.

---

## Package Map

```
com.minicloud/
├── config/        → Startup config, data seeding, scheduling
├── security/      → JWT filter, token utils, user loader
├── controller/    → REST endpoints (22 controllers)
├── service/       → Business logic (18 services)
├── model/         → JPA entities (32 database tables)
├── repository/    → Spring Data CRUD interfaces
├── dto/           → Request/Response data shapes
├── exception/     → Global error handling
└── ui/            → JavaFX desktop console
```

---

## 1. `config/` — Startup & Configuration

| File | What It Does |
|---|---|
| `SecurityConfig.java` | Configures Spring Security: whitelists `/api/auth/**`, plugs in `JwtFilter` before every other filter, sets session to stateless |
| `DockerConfig.java` | Instantiates the `DockerClient` bean — connects to the local Docker daemon via Unix socket / TCP |
| `AppConfig.java` | Registers `PasswordEncoder` (BCrypt) and other app-wide beans |
| `DataSeeder.java` | Runs **once on startup** (via `CommandLineRunner`) — seeds the DB with a root admin, default VPC, subnet, compute instance, database, security group, and bucket so the dashboard has live-looking data immediately |
| `BillingScheduler.java` | A `@Scheduled` task that periodically calculates usage cost for running resources and writes billing records |

---

## 2. `security/` — Auth & JWT

| File | What It Does |
|---|---|
| `JwtFilter.java` | Intercepts **every HTTP request**. Reads `Authorization: Bearer <token>`, validates it, and if valid sets the user identity into Spring's `SecurityContext` so downstream code knows who's calling |
| `JwtUtil.java` | Signs JWTs with an HMAC-SHA key. Has methods: `generateToken(username)`, `extractUsername(token)`, `validateToken(token, userDetails)` |
| `CustomUserDetailsService.java` | Loads users from `UserRepository` by username — used by `JwtFilter` during token validation |

**Flow:** Request → `JwtFilter` → extract username from token → load `UserDetails` from DB → set auth in context → controller runs.

---

## 3. `controller/` — REST Layer (22 Controllers)

Each controller maps URLs to service calls. No business logic lives here — only routing, DTO mapping, and HTTP response codes.

| Controller | Route Prefix | Responsibility |
|---|---|---|
| `AuthController` | `/api/auth` | Register new user, login (returns JWT) |
| `ComputeController` | `/api/compute` | Launch / list / stop compute instances |
| `BucketController` | `/api/storage` | Create buckets, upload files, list files, delete |
| `DatabaseController` | `/api/database` | Provision / list / delete MySQL containers |
| `DynamoController` | `/api/dynamo` | Create DynamoDB-style tables, put/get/delete items |
| `VolumeController` | `/api/volumes` | Create, attach, detach EBS-style volumes |
| `LoadBalancerController` | `/api/loadbalancer` | Create LBs, register targets, get status |
| `VPCController` | `/api/vpc` | Create / list Virtual Private Clouds |
| `SubnetController` | `/api/subnets` | Create subnets within a VPC |
| `DnsController` | `/api/dns` | Create hosted zones, manage DNS records |
| `FirewallController` | `/api/firewall` | Add / list / delete firewall rules |
| `IamController` | `/api/iam` | Create IAM users, roles, groups, policies |
| `SecurityController` | `/api/security` | Manage security groups and findings |
| `LambdaController` | `/api/lambda` | Register and invoke Lambda functions |
| `OrchestrationController` | `/api/orchestration` | Deploy full Web+DB+LB stacks atomically |
| `CloudFormationController` | `/api/cloudformation` | Template-based stack creation (CF-style) |
| `MessagingController` | `/api/messaging` | Create SQS queues / SNS topics, send messages |
| `StatsController` | `/api/stats` | Live CPU/RAM metrics for a container |
| `MonitoringController` | `/api/monitoring` | Query stored metrics and health status |
| `BillingController` | `/api/billing` | View usage summary and estimated cost |
| `AuditLogController` | `/api/audit` | Retrieve audit trail of all user actions |
| `MetadataController` | `/api/metadata` | Cloud metadata for running instances |

---

## 4. `service/` — Business Logic

This is where things actually happen.

| Service | How It Works |
|---|---|
| `AuthService` | Hashes password with BCrypt, saves `User` entity, or validates credentials and calls `JwtUtil.generateToken()` |
| `DockerService` | Raw Docker operations: `pullImage()`, `createContainer()`, `startContainer()`, `stopContainer()`, `inspectContainer()`. Used by Compute, Database, Lambda |
| `ComputeService` | Calls `DockerService` to launch a Tomcat container, assigns a random host port, saves `ComputeInstance` to H2 |
| `DatabaseService` | Same pattern but launches a `mysql:8` container with a generated password, saves `DatabaseInstance` |
| `BucketService` | Uses `java.nio.file.Files` and `Path` to create directories per bucket, write uploaded files to disk, list contents, and delete. Saves metadata to H2 |
| `HealthCheckService` | `@Scheduled` every 10 seconds — loads all instances from H2, inspects each via Docker, calls `startContainer()` on any that have stopped |
| `LoadBalancerService` | Stores a list of target instances, cycles through them in Round Robin order for each incoming route request |
| `OrchestrationService` | Calls `ComputeService` + `DatabaseService` + `LoadBalancerService` in sequence, wraps result into a `Stack` entity |
| `IamService` | CRUD for `IamUser`, `IamRole`, `IamGroup`, `IamPolicy` — all JPA-persisted |
| `DnsService` | CRUD for `HostedZone` and `DnsRecord` entities — simulates Route 53 |
| `SqsService` | Uses a `ConcurrentLinkedQueue<SqsMessage>` per queue — send pushes, receive polls |
| `SnsService` | Maintains a subscriber list per `SnsTopic`, publish fans out to all subscribers |
| `StatsService` | Calls Docker's `statsCmd(containerId)` to get CPU/RAM usage and returns it as a `StatsResponse` |
| `BillingService` | Reads running resources, calculates cost based on uptime and instance type, writes `BillingRecord` |
| `CloudFormationService` | Parses a template-style request, provisions the described compute + network resources |
| `SecurityService` | Manages `SecurityGroup` and `WafRule` entities; scans for open-to-world rules and creates `SecurityFinding` records |
| `CloudTrailService` | Saves an `AuditLog` entry after every significant action |
| `CloudMetadataService` | Returns metadata (region, VPC, subnet, IP) for a given instance at runtime |

---

## 5. `model/` — Database Entities (32 tables)

Each class is a `@Entity` mapped to an H2 table via JPA/Hibernate.

| Group | Entities |
|---|---|
| **Identity** | `User`, `IamRole`, `IamGroup`, `IamPolicy` |
| **Compute** | `ComputeInstance`, `LambdaFunction`, `Stack`, `CfStack` |
| **Storage** | `Bucket`, `BucketPolicy`, `BucketVersion`, `StorageFile`, `Volume` |
| **Database** | `DatabaseInstance`, `DynamoTable`, `DynamoItem` |
| **Networking** | `VPC`, `Subnet`, `LoadBalancer`, `HostedZone`, `DnsRecord` |
| **Security** | `FirewallRule`, `SecurityGroup`, `SecurityFinding`, `WafRule` |
| **Messaging** | `SqsQueue`, `SqsMessage`, `SnsTopic`, `SnsSubscription` |
| **Observability** | `Metric`, `AuditLog`, `BillingRecord` |

---

## 6. `repository/` — Data Access

One interface per entity, extending `JpaRepository<Entity, Long>`. Spring auto-generates all SQL. Services call methods like:
- `findByOwner(username)` — multi-tenancy filter
- `findByContainerId(id)` — look up instance by Docker ID
- `save(entity)` / `deleteById(id)` — persist or remove

No raw SQL written anywhere.

---

## 7. `dto/` — Data Transfer Objects

Decouples the API shape from the database shape.

- **Request DTOs**: Carry fields from the HTTP body (e.g. `LaunchRequest` has `name`, `image`, `instanceType`)
- **Response DTOs**: What the API returns (e.g. `StatsResponse` has `cpuPercent`, `memoryUsageMb`)
- Prevents internal entity fields (like passwords) from leaking into API responses

---

## 8. `exception/` — Error Handling

| File | What It Does |
|---|---|
| `GlobalExceptionHandler.java` | `@RestControllerAdvice` — catches all unhandled exceptions across every controller and returns a clean `{ error, message, status }` JSON response instead of a Java stack trace |
| `ResourceNotFoundException.java` | Thrown when a requested entity (instance, bucket, etc.) doesn't exist — maps to HTTP `404` |

---

## 9. `ui/` — JavaFX Console

The desktop app runs as a separate JavaFX `Application`. It:
- Makes HTTP calls to `localhost:8080` using Java's `HttpClient`
- Stores the JWT in memory after login and attaches it as `Authorization: Bearer` to every request
- Renders data in `TableView`, `TreeView`, and `LineChart` components
- Refreshes data on an `AnimationTimer` / background thread every few seconds for live stats
