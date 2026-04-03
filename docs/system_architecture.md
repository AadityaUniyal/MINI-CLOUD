# System Architecture Flowchart (Detailed)

This diagram breaks down the **MiniCloud** architecture into specific components, showing the interactions between the JavaFX UI, the Spring Boot Backend services, and the simulated infrastructure layer.

```mermaid
graph TD
    %% Styling
    classDef ui fill:#e3f2fd,stroke:#1565c0,stroke-width:2px;
    classDef backend fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px;
    classDef infra fill:#fff3e0,stroke:#ef6c00,stroke-width:2px;
    classDef storage fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px;

    User([User]) -->|Interacts| JavaFX_App

    subgraph "Pure Java Desktop App (Frontend)"
        direction TB
        JavaFX_App[JavaFX Application]:::ui
        
        subgraph "UI Scenes"
            LoginScene[Login View]:::ui
            Dashboard[Compute Dashboard]:::ui
            StorageView[File Browser]:::ui
        end

        HttpClient[Java 11 HttpClient]:::ui
        Poller[Stats Poller (Timeline)]:::ui
        
        JavaFX_App --> LoginScene
        JavaFX_App --> Dashboard
        JavaFX_App --> StorageView
        
        LoginScene -->|1. Login Request| HttpClient
        Dashboard -->|2. Launch Instance| HttpClient
        StorageView -->|3. Upload File| HttpClient
        Poller -.->|4. Poll Stats (Every 2s)| HttpClient
    end

    HttpClient -->|REST API (JSON)| SpringBoot

    subgraph "Spring Boot Backend (Server)"
        direction TB
        SpringBoot[Spring Boot 3.x]:::backend
        
        subgraph "API Layer"
            AuthCtrl[AuthController]:::backend
            ComputeCtrl[ComputeController]:::backend
            StorageCtrl[StorageController]:::backend
            StatsCtrl[StatsController]:::backend
        end
        
        subgraph "Service Layer"
            AuthService[Auth Service (JWT)]:::backend
            DockerService[Docker Service]:::backend
            StorageService[Storage Service (NIO)]:::backend
            Scheduler[Background Scheduler]:::backend
        end

        AuthCtrl --> AuthService
        ComputeCtrl --> DockerService
        StorageCtrl --> StorageService
        StatsCtrl --> Scheduler
    end

    subgraph "Infrastructure Adapters"
        H2Repo[JPA Repository]:::infra
        DockerClient[docker-java Lib]:::infra
        NIOHandler[java.nio.file]:::storage
    end

    %% Data Flow Connections
    AuthService --> H2Repo
    DockerService --> DockerClient
    DockerService --> H2Repo
    StorageService --> NIOHandler
    StorageService --> H2Repo

    %% Scheduler Logic
    Scheduler -.->|Periodically Polls| DockerClient
    Scheduler -.->|Updates DB| H2Repo

    subgraph "Physical Resources (Emulated Cloud)"
        H2DB[(H2 Database File)]:::infra
        DockerDaemon[[Docker Desktop (Containers)]]:::infra
        LocalDisk[Local File System (Buckets)]:::storage
    end

    H2Repo --> H2DB
    DockerClient --> DockerDaemon
    NIOHandler --> LocalDisk
```
