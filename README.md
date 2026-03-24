# 🛡️ First Quarkus DAO (gRPC Server)

This is a backend service built with **Quarkus**, designed to provide high-performance data access for products and customers. It acts as a **gRPC Server**, serving requests from the API gateway service.

The project implements the **DAO (Data Access Object)** pattern with a fully **Reactive stack** to ensure non-blocking data processing and high scalability.

## 🛠 Tech Stack
*   **Java 21**
*   **Quarkus** (gRPC Server)
*   **Hibernate Reactive** (Non-blocking ORM)
*   **Reactive PostgreSQL Client** (High-performance DB driver)
*   **Quarkus Liquibase** (Database Schema Migrations)
*   **Protocol Buffers (proto3)** (Service Definition)

## 🔄 Reactive gRPC Architecture
This service leverages the power of Mutiny (Reactive Streams) and gRPC to provide:
*   **Non-blocking I/O**: Efficient resource usage under heavy load.
*   **Strict Typing**: Synchronized communication via shared `.proto` files.
*   **Scalability**: Optimized for cloud-native environments and high concurrency.


## 🗄 Data Model
The service manages the following entities:
*   **Products**: Storage of items, pricing, and inventory data.
*   **Customers**: Management of user profiles and personal information.

## 🌐 Environment Variables

To connect the service to a **PostgreSQL** database, you can use the following environment variables (or set them in `application.properties`):


| Variable | Description | Default Value (example) |
| :--- | :--- | :--- |
| `QUARKUS_DATASOURCE_REACTIVE_URL` | Reactive connection string | `postgresql://localhost:5432/mydb` |
| `QUARKUS_DATASOURCE_USERNAME` | Database user | `db_user` |
| `QUARKUS_DATASOURCE_PASSWORD` | Database password | `db_password` |

Example of setting via terminal:
```shell
export QUARKUS_DATASOURCE_REACTIVE_URL=vert.x-sql://db_host:5432/db_name
export QUARKUS_DATASOURCE_USERNAME=admin
export QUARKUS_DATASOURCE_PASSWORD=secret
```

## 🚀 Getting Started

### Development Mode
To start the gRPC server with **Live Coding** support:
```shell
./mvnw compile quarkus:dev
```

### 🚀 Build and Run (JVM Mode)

To create a standard JAR file and run it, use the following commands:

```shell
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

## ⚙️ Configuration

The gRPC server by default listens on port `9000`. You can adjust this in `src/main/resources/application.properties`:

```properties
quarkus.grpc.server.port=9000
```

## 📊 Monitoring

*   **Health Checks**: [http://localhost:8082/q/health](http://localhost:8082/q/health) — Monitor the server and reactive database connection.
*   **Dev UI**: [http://localhost:8082/q/dev](http://localhost:8082/q/dev) — Inspect gRPC services (available in Dev mode).


