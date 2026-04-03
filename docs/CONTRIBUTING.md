# Contributing to MiniCloud

Welcome to the **MiniCloud** project! We're excited to have you contribute to this locally-deployable cloud infrastructure platform. By participating, you agree to abide by our standards and development practices.

---

## 🚀 How to Contribute

We follow the standard **Fork & Pull Request** workflow:

1.  **Fork** this repository.
2.  **Clone** your forked repository:
    ```bash
    git clone https://github.com/your-username/MINI-CLOUD.git
    ```
3.  **Create a branch** for your feature or bugfix:
    ```bash
    git checkout -b feature/your-feature-name
    ```
4.  **Commit** your changes with clear messages:
    ```bash
    git commit -m "feat: Add support for custom Docker images"
    ```
5.  **Push** to your fork and **Open a Pull Request** against our `main` branch.

---

## 🛠️ Local Development Setup

To contribute effectively, you need the following environment:

- **JDK 17+** (Amazon Corretto or GraalVM recommended)
- **Docker Desktop** (Required for the `DockerService` and `Compute` logic)
- **Maven 3.8+**
- **Lombok** enabled in your IDE (IntelliJ or Eclipse)

### Common Commands
- **Compile**: `mvn compile`
- **Run Backend**: `mvn spring-boot:run`
- **Run Tests**: `mvn test`
- **Package**: `mvn package`

---

## 📋 Coding Standards

To maintain a clean and consistent codebase, please follow these guidelines:

- **Language**: Use modern Java features (Java 17).
- **Style**: Follow standard Java naming conventions (PascalCase for classes, camelCase for methods/variables).
- **Boilerplate**: Use **Lombok** (`@Getter`, `@Setter`, `@Builder`, etc.) to minimize boilerplate code.
- **REST**: Ensure all new API endpoints follow consistent JSON naming conventions (camelCase).
- **Service Layer**: Keep business logic in the `Service` layer, not in `Controllers`.
- **Infrastructure**: All Docker interactions should go through `DockerService`.

---

## 📁 Repository Best Practices

- **Branching**: Use prefixes for branch names (`feature/`, `bugfix/`, `hotfix/`).
- **Commits**: Follow [Conventional Commits](https://www.conventionalcommits.org/) (e.g., `feat:`, `fix:`, `docs:`, `refactor:`).
- **Testing**: Ensure that all existing tests pass before submitting a Pull Request. If you add a new service, provide a corresponding JUnit test.

## 🤝 Need Help?

If you have questions or encounter issues, please open an **Issue** on the repository or contact the maintainers.

---
Thank you for helping us make MiniCloud a better cloud simulation platform for everyone!
