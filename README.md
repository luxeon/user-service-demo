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

## Application Architecture

The application is structured into the following layers:

*   **REST Controller Layer (`@RestController`)**: Responsible for handling all incoming HTTP requests and responses. It receives DTOs as input and delegates the processing to the Facade layer. It should not contain any business logic.

*   **Facade Layer (`@Component`)**: Acts as a bridge between the Controller and Service layers. Its primary responsibility is to map domain entities to DTOs (and vice versa) using MapStruct. It can also aggregate calls to multiple services to build complex responses.

*   **Service Layer (`@Service`)**: Contains the core business logic of the application. It orchestrates interactions with the Repository layer to perform data operations and implements the application's use cases.

*   **Repository Layer (`@Repository`)**: The data access layer, responsible for all communication with the database. It uses Spring Data JPA to provide an abstraction over database interactions.

## Assumptions

### Password Hashing

For the purpose of this project, a simple MD5 hashing mechanism has been implemented for passwords. This is **not secure for production environments**. In a real-world application, a stronger, adaptive hashing algorithm like bcrypt or Argon2 should be used to ensure proper password security.

### Error Handling

The error response structure has been simplified to accelerate development. For a production environment, it is recommended to implement a more comprehensive error response strategy that includes unique error codes, detailed context, and links to documentation to improve client-side error handling and debugging.

### User Deletion

For simplicity, users are physically deleted from the database. In a real-world application, a soft-delete approach (e.g., using a `deleted` flag) is often preferred to preserve data history and integrity.
