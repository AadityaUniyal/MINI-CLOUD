# ☁️ MiniCloud — Java-Based Private Cloud Platform

> A locally-running, full-featured cloud infrastructure platform replicating core AWS services — built with a **Spring Cloud Microservices** stack as a Project-Based Learning (PBL) initiative.

---

## 📌 What We Are Building

**MiniCloud** is a private cloud management system that simulates the experience of running your own AWS-like infrastructure — entirely on your local machine. It decomposes fundamental cloud primitives (compute, storage, networking, security) into distinct, independent microservices orchestrated via **Spring Cloud Gateway** and **Eureka Service Discovery**.

The platform is accessible through a native **JavaFX desktop console** and a centralized **REST API Gateway**.

---

## 🏗️ Microservices Architecture

MiniCloud is structured as a distributed system:

| Service | Port | Description | AWS Equivalent |
|---|---|---|---|
| **Eureka Server** | 8761 | Service Registry & Discovery | Route 53 / Cloud Map |
| **API Gateway** | 8080 | Central entry point, routing & load balancing | Amazon API Gateway |
| **IAM Service** | 8081 | Identity & Access Management, JWT Auth | AWS IAM / Cognito |
| **Backend Service** | 8082 | Core business logic, Storage, RDS, VPC | Core AWS APIs |
| **Compute Service** | 8083 | Resource management & Docker orchestration | Amazon EC2 |

---

## 🚀 Services We Offer

MiniCloud replicates **15+ AWS-equivalent services**:

### 🖥️ Compute & Orchestration
- **Compute Instances (EC2)**: Launch containerized Tomcat/JDK application servers.
- **Lambda Functions**: Execute event-driven logic in isolated containers.
- **Auto-Orchestration**: Deploy full stacks (Web + DB + LB) in one call.

### 🗄️ Storage & Database
- **Object Storage (S3)**: Manage buckets and files using Java NIO.
- **Managed Database (RDS)**: Dynamic provisioning of PostgreSQL instances.
- **DynamoDB-style Tables**: Schema-free key-value store.

### 🌐 Networking & Security
- **VPC & Subnets**: Isolated virtual networks and address spaces.
- **Load Balancer (ELB)**: Traffic distribution across compute nodes.
- **IAM**: Robust RBAC (Role Based Access Control) with JWT security.

---

## 📁 Project Structure

The project follows a modern **Multi-Module Maven** architecture:

```
JAVA-PBL/
├── minicloud-eureka-server/    # Service Registry (Port 8761)
├── minicloud-api-gateway/      # Central Router (Port 8080)
├── minicloud-iam/              # Security & Users (Port 8081)
├── minicloud-backend/          # Core Business Logic (Port 8082)
├── minicloud-compute/          # Docker Instance Workflows (Port 8083)
├── minicloud-common/           # Shared DTOs, Utils, and Domain Models
├── minicloud-admin-console/    # JavaFX Desktop Management UI
├── docker/                     # Service-specific Dockerfiles & DB Initialization
├── docs/                       # Project specifications, licenses, and guides
├── scripts/                    # Deployment and utility scripts
├── docker-compose.yml          # Full-stack orchestration
└── pom.xml                     # Parent Maven Project
```

---

## 🏁 Getting Started

### 1. Prerequisites
- **JDK 21+** (Project uses Java 21 features)
- **Maven 3.9+**
- **Docker Desktop** (Must be running)

### 2. Deployment
You can start the entire infrastructure using Docker Compose:

```bash
docker-compose up -d --build
```

This will spin up:
1. **PostgreSQL**: The primary database.
2. **Eureka Server**: Wait ~10s for health check.
3. **IAM, Gateway, Backend, and Compute**: Auto-registering with Eureka.

### 3. Usage
- **Management Console**: Run the `minicloud-admin-console` module to open the AWS-style dashboard.
- **API Access**: All requests should be sent to the **Gateway (8080)**.
- **Database**: Connected via `localhost:5432` (User: `postgres`, Pass: `password`).

---

## 📄 Documentation & Metadata
- Detailed project requirements can be found in `docs/CloudPlatform_Project_Specification.txt`.
- Multi-module lifecycle is managed via the root `pom.xml`.

---

## 📄 License
Academic project developed for the **Mini Project - Cloud Computing** (PBL) course.
` | Register a new user |
| `POST` | `/api/auth/login` | Login and receive JWT |

### Compute & Storage
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/compute/launch` | Launch a compute instance |
| `GET` | `/api/compute/instances` | List your instances |
| `POST` | `/api/storage/upload` | Upload file to a bucket |
| `POST` | `/api/database/provision` | Provision a PostgreSQL container |
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

---

## 🧪 Testing & Quality
To run the full suite of unit and integration tests across all modules:
```bash
mvn test
```

## 📦 Building the Platform
To compile and package all services into JAR files:
```bash
mvn clean package
```
Build artifacts will be located in each module's `target/` directory.

---

## 👥 Team
- **Member 1**: Backend Lead (Compute, Storage, Health)
- **Member 2**: API Lead (Networking, Gateway, Messaging)
- **Member 3**: UX Lead (JavaFX Console, Dashboards)

---

## 📄 License
Academic project developed for the **Mini Project - Cloud Computing** (PBL) course.
