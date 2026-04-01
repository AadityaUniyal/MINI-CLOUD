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
- **MySQL Server 8.x**: Installed and running on `localhost:3306`.
  - Database: `minicloud`
  - User: `root`
  - Password: `password`
- **JDK 17+**: (JDK 25 is currently being used).
- **Maven**: Ensure Maven is configured to handle the multi-module build.

### Running the Platform:
1. Start your **MySQL Server**.
2. Run the following command from the root folder:
   ```cmd
   mvn clean install
   cd mini-cloud-backend
   mvn spring-boot:run
   ```

The application will initialize the schema from `database/init/schema.sql` and `data.sql` on the first run, then launch the **MiniCloud Console**.
