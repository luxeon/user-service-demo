# User Service

This project is a Spring Boot application for managing users.

## How to Run Locally

To run this application locally, you will need Docker and Java 21.

1.  **Start the database**

    The project uses PostgreSQL, which can be started easily with the provided `docker-compose.yml` in the `local` directory.

    ```bash
    cd local
    docker-compose up -d
    ```

2.  **Run the application**

    The application uses the `local` Spring profile to connect to the Dockerized database. Run the application with this profile active.

    From the command line using Maven:
    ```bash
    ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
    ```

    Alternatively, you can configure your IDE to use the `local` profile when running the application.

## Key Endpoints

Once the application is running, some of the useful endpoints available are:

*   **Health Check**: `/actuator/health`
*   **Swagger UI**: `/swagger-ui/index.html`

---

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

### Search Endpoint

To speed up development, a search endpoint has been implemented with a limited set of parameters. This can be extended in the future to support more complex filtering and sorting capabilities based on additional user attributes.

### AMQP/Messaging Integration

The `UserActivityListener` is designed to publish events to a message broker, but the actual AMQP message sending logic has been skipped for now. When this functionality is implemented, it must be thoroughly tested using Testcontainers to ensure the messaging integration works reliably without depending on a live message broker.

## Event-Driven Mechanism for User Activities

The application uses an event-driven mechanism to decouple business logic from side effects like sending notifications or updating other systems. This is primarily handled by two components: `UserHibernateListener` and `UserActivityListener`.

### `UserHibernateListener`

This class integrates with Hibernate's event system to listen for persistence events related to the `User` entity.

- It implements `PostInsertEventListener`, `PostUpdateEventListener`, and `PostDeleteEventListener`.
- When a user is created, updated, or deleted, the listener catches the corresponding Hibernate event.
- It then publishes a custom Spring `ApplicationEvent` (`UserCreatedEvent`, `UserUpdatedEvent`, or `UserDeletedEvent`). This event is dispatched synchronously within the same transaction as the database operation.

### `UserActivityListener`

This component listens for the application events published by the `UserHibernateListener`. Its purpose is to perform actions in response to user data changes, such as sending messages to a message queue (e.g., RabbitMQ or Kafka) to notify other services.

- It uses `@TransactionalEventListener` to subscribe to the custom user events.
- The key aspect of this listener is the use of `phase = TransactionPhase.AFTER_COMMIT`. This configuration ensures that the event handling logic is executed only *after* the database transaction has successfully committed.

#### Why `TransactionPhase.AFTER_COMMIT`?

Using the `AFTER_COMMIT` phase is crucial for ensuring data consistency, especially in a microservices architecture. If we were to send a notification before the transaction is committed, the transaction could still fail and be rolled back. This would lead to a state where other services are notified of a change that never actually happened in the `user-service` database.

By waiting for the transaction to commit, we guarantee that we are broadcasting a state that is permanently stored and consistent. This prevents race conditions and ensures the reliability of downstream processes that depend on this user data.
