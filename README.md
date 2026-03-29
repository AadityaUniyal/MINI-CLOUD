# ☁️ MiniCloud — Java-Based Private Cloud Platform

> A locally-running, full-featured cloud infrastructure platform replicating core AWS services — built entirely with a **Pure Java** stack as a Project-Based Learning (PBL) initiative.

---

## 📌 What We Are Building

**MiniCloud** is a private cloud management system that simulates the experience of running your own AWS-like infrastructure — entirely on your local machine. It exposes the fundamental primitives that every real cloud platform provides: compute, storage, networking, databases, security, serverless, and messaging — all accessible through a native **JavaFX desktop console** and a **REST API**.

The goal is to demonstrate how cloud services work under the hood: using Docker as the virtualization layer, Spring Boot as the service backbone, and Java NIO for storage — with no third-party cloud dependencies.

---

## 🚀 Services We Offer

MiniCloud replicates **15+ AWS-equivalent services** grouped into 6 categories:

### 🖥️ Compute
| MiniCloud Service | AWS Equivalent | Description |
|---|---|---|
| **Compute Instances** | EC2 | Launch containerized Tomcat application servers via Docker |
| **Lambda Functions** | AWS Lambda | Execute lightweight, event-driven functions in Docker containers |
| **Auto-Orchestration** | CloudFormation | Deploy full stacks (Web + DB + LB) in a single API call |

### 🗄️ Storage & Database
| MiniCloud Service | AWS Equivalent | Description |
|---|---|---|
| **Object Storage (Buckets)** | S3 | Store files, assets, and media using Java NIO on local disk |
| **Volumes** | EBS | Attach persistent block storage to compute instances |
| **Managed Database** | RDS | Provision isolated MySQL containers with dynamic port mapping |
| **DynamoDB-style Tables** | DynamoDB | Schema-free key-value store with item-level CRUD |

### 🌐 Networking
| MiniCloud Service | AWS Equivalent | Description |
|---|---|---|
| **Virtual Private Cloud** | VPC | Isolate resource groups into private virtual networks |
| **Subnets** | Subnets | Segment VPCs into logical address spaces |
| **DNS (Hosted Zones)** | Route 53 | Manage domain name resolution records internally |
| **Load Balancer** | ELB/ALB | Distribute traffic across instances using Round Robin |
| **Firewall Rules** | Security Groups | Define allow/deny rules for inbound and outbound traffic |

### 🔐 Security & Identity
| MiniCloud Service | AWS Equivalent | Description |
|---|---|---|
| **IAM (Users & Roles)** | IAM | Manage users, roles, and permission policies |
| **JWT Authentication** | Cognito / STS | Secure all API access with signed JSON Web Tokens |
| **Security Policies** | IAM Policies | Enforce resource-level access control per user |

### 📬 Messaging
| MiniCloud Service | AWS Equivalent | Description |
|---|---|---|
| **Message Queue (SQS)** | SQS | Producer-consumer message queue with async delivery |
| **Pub/Sub Notifications (SNS)** | SNS | Topic-based event broadcasting (supports SQS endpoints, fully functional) |

### 📊 Monitoring & Governance
| MiniCloud Service | AWS Equivalent | Description |
|---|---|---|
| **Real-Time Metrics** | CloudWatch | Persist CPU/RAM usage of containers every 30s |
| **Health Check Engine** | Route 53 Health Checks | Auto-detect crashed instances and restart them every 10s |
| **Audit Log (CloudTrail)** | CloudTrail | Record all user actions for compliance and review |
| **Billing Dashboard** | AWS Cost Explorer | Track resource usage and compute simulated cost |
| **Cloud Metadata** | EC2 Instance Metadata | Expose runtime metadata for running instances |
| **Virtual Web Hosting** | S3 & DNS | Host static websites from buckets with domain mapping |

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│               JavaFX Management Console                  │
│         (Native Desktop Dashboard — AWS-style UI)        │
└────────────────────────┬────────────────────────────────┘
                         │ REST + JWT (HTTP)
┌────────────────────────▼────────────────────────────────┐
│              Spring Boot REST API (Port 8080)            │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────┐  │
│  │ Compute  │ │ Storage  │ │ Network  │ │  Security │  │
│  │ Lambda   │ │ Database │ │   DNS    │ │    IAM    │  │
│  │ Orchestr.│ │ DynamoDB │ │   VPC    │ │  Billing  │  │
│  │ Stats    │ │ Volumes  │ │ Firewall │ │ Messaging │  │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └─────┬─────┘  │
│       │             │            │              │         │
│  ┌────▼─────────────▼────────────▼──────────────▼─────┐  │
│  │         Spring Data JPA + H2 (Embedded DB)          │  │
│  └─────────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────┘
                           │ docker-java SDK
┌──────────────────────────▼──────────────────────────────┐
│                    Docker Engine                          │
│   [ Tomcat Containers ]  [ MySQL Containers ]            │
│   [ Lambda Runners   ]   [ Custom Networks ]             │
└─────────────────────────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────┐
│              Local Disk (Java NIO)                        │
│   [ Object Storage Buckets ]  [ EBS-style Volumes ]      │
└─────────────────────────────────────────────────────────┘
```

### Key Design Principles
- **Multi-tenancy**: Every resource is scoped to the authenticated owner; users see only their own instances.
- **Auto-healing**: `HealthCheckService` polls Docker every 10 seconds and restarts crashed containers automatically.
- **Stack Deployment**: `OrchestrationService` provisions a full Web Server + Database + Load Balancer in a single atomic operation.

---

## 🛠️ Tech Stack

| Layer | Technology | Version |
|---|---|---|
| **Language** | Java | 17 |
| **Backend Framework** | Spring Boot | 3.2.4 |
| **Security** | Spring Security + JJWT | 0.11.5 |
| **ORM / Persistence** | Spring Data JPA + Hibernate | 3.x |
| **Embedded Database** | H2 (in-memory, dev mode) | — |
| **Container Engine** | docker-java SDK | 3.3.4 |
| **Storage Engine** | Java NIO (`java.nio.file`) | Built-in |
| **Frontend / Console** | JavaFX | 21.0.2 |
| **Template Engine** | Thymeleaf | 3.x |
| **XML Binding** | JAXB | 2.3.1 |
| **Build Tool** | Apache Maven | 3.9.6 |

---

## 📂 Project Structure

```
JAVA-PBL/
├── src/main/java/com/minicloud/
│   ├── MiniCloudApplication.java
│   ├── config/                    # Security, Docker, App configuration
│   ├── controller/                # 22 REST controllers (one per service)
│   │   ├── AuthController.java
│   │   ├── ComputeController.java
│   │   ├── BucketController.java
│   │   ├── DatabaseController.java
│   │   ├── DynamoController.java
│   │   ├── VolumeController.java
│   │   ├── LoadBalancerController.java
│   │   ├── VPCController.java
│   │   ├── SubnetController.java
│   │   ├── DnsController.java
│   │   ├── FirewallController.java
│   │   ├── IamController.java
│   │   ├── SecurityController.java
│   │   ├── LambdaController.java
│   │   ├── OrchestrationController.java
│   │   ├── CloudFormationController.java
│   │   ├── MessagingController.java
│   │   ├── StatsController.java
│   │   ├── MonitoringController.java
│   │   ├── BillingController.java
│   │   ├── AuditLogController.java
│   │   └── MetadataController.java
│   ├── service/                   # Business logic per service
│   ├── model/                     # JPA entities
│   ├── dto/                       # Request / Response DTOs
│   ├── repository/                # Spring Data repositories
│   ├── security/                  # JWT filter, util, user details
│   ├── ui/                        # JavaFX dashboard application
│   └── util/
├── src/main/resources/
│   └── application.properties
├── docker/                        # Docker-related configs
├── storage/                       # Local bucket / volume storage root
├── pom.xml
└── README.md
```

---

## ⚙️ Prerequisites

- **JDK 17+** — [Download](https://adoptium.net/)
- **Apache Maven 3.8+** — [Download](https://maven.apache.org/)
- **Docker Desktop** — [Download](https://www.docker.com/products/docker-desktop/) *(must be running)*

---

## 🏁 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/AadityaUniyal/MINI-CLOUD.git
cd MINI-CLOUD
```

### 2. Start Docker Desktop
Docker must be running before starting the backend. The application connects to the Docker daemon on startup to manage containers.

### 3. Run the backend
```bash
mvn spring-boot:run
```
The backend starts on **`http://localhost:8080`**

### 4. H2 Database Console *(dev only)*
```
http://localhost:8080/h2-console
```

---

## 📡 Key API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive JWT |

### Compute & Storage
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/compute/launch` | Launch a compute instance |
| `GET` | `/api/compute/instances` | List your instances |
| `POST` | `/api/storage/upload` | Upload file to a bucket |
| `POST` | `/api/database/provision` | Provision a MySQL container |
| `POST` | `/api/dynamo/tables` | Create a DynamoDB-style table |
| `POST` | `/api/volumes` | Create a persistent volume |

### Networking
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/vpc` | Create a VPC |
| `POST` | `/api/subnets` | Create a subnet in a VPC |
| `POST` | `/api/dns/zones` | Create a hosted zone |
| `POST` | `/api/firewall/rules` | Add a firewall rule |
| `POST` | `/api/loadbalancer` | Create a load balancer |

### Advanced
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/orchestration/deploy-stack` | Deploy a full Web+DB+LB stack |
| `POST` | `/api/lambda/invoke` | Execute a Lambda function |
| `GET` | `/api/stats/{containerId}` | Live CPU/RAM metrics |
| `GET` | `/api/billing/summary` | View usage and estimated cost |
| `GET` | `/api/audit/logs` | View your audit trail |

> **Auth**: All endpoints except `/api/auth/**` require `Authorization: Bearer <JWT>`

---

## 👥 Team

| Member | Role | Core Responsibility |
|---|---|---|
| **Member 1** | Backend Lead | Services layer, Docker engine, Health Check, Java NIO storage |
| **Member 2** | API Lead | REST Controllers, Networking, Messaging, Orchestration, Metrics |
| **Member 3** | UX Lead | JavaFX console, Dashboard UI, Resource monitoring views |

---

## 🧪 Running Tests

```bash
mvn test
```

## 📦 Build JAR

```bash
mvn package
java -jar target/minicloud-backend-0.0.1-SNAPSHOT.jar
```

---

## 📄 License

Academic project developed as part of a semester Project-Based Learning (PBL) course.
