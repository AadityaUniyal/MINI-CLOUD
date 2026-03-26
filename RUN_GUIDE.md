# MiniCloud Run Guide

To start the MiniCloud platform (Backend + JavaFX Console), copy and paste the following command into your terminal:

```cmd
"C:\Users\HP\.m2\wrapper\dists\apache-maven-3.9.6-bin\3311e1d4\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
```

### Alternative (if Maven is in PATH):
```cmd
mvn spring-boot:run
```

### Pre-requisites:
- **JDK 17+** (JDK 25 is currently being used)
- **Environment**: Windows Command Prompt or PowerShell

The application will start the Spring Boot backend on `http://localhost:8080` and automatically launch the JavaFX Management Console window.
