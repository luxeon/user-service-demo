# User Service

This project is a Spring Boot application for managing users.

## OpenAPI Code Generation

This project uses the `openapi-generator-maven-plugin` to generate Spring Boot controller interfaces and Data Transfer Objects (DTOs) from an OpenAPI specification file.

### How it Works

1.  The API contract is defined in `contract/api.yml`.
2.  During the Maven build process (specifically, the `generate-sources` phase), the `openapi-generator-maven-plugin` reads this YAML file.
3.  It then generates Java source code based on the specification. This includes:
    *   A Java interface for the REST controller (e.g., `UserApi`).
    *   Java records for all defined request and response DTOs (e.g., `UserCreateRequest`, `UserResponse`).

### Generated Classes Location

The generated source files are placed in the following directory:

`target/generated-sources/openapi/src/main/java/`
