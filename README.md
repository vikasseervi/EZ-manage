# EZ-manage

EZ-manage is a Spring Boot based backend application for user/session/security-oriented management features with MySQL persistence, caching, JWT, OAuth2 login support, and Kafka integration.

## Tech Stack

- Java 21
- Spring Boot 3.4.1
- Spring Web
- Spring Data JPA + Hibernate
- Spring Security
- Spring Session JDBC
- Spring OAuth2 Client (Google)
- JWT (jjwt)
- Apache Kafka (spring-kafka)
- MySQL
- Ehcache + Hibernate JCache (L2 cache)
- Maven Wrapper (`mvnw`)

## Repository Structure

```text
EZ-manage/
├── .mvn/
├── mvnw
├── mvnw.cmd
├── pom.xml
├── sql scipts/
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ehcache.xml
│   └── test/
└── README.md
```

> Note: the directory name is currently `sql scipts` in the repository.

## Key Capabilities (based on dependencies/config)

- REST backend with Spring MVC
- Database persistence via Spring Data JPA
- Authentication/authorization using Spring Security
- OAuth2 login flow (Google client config present)
- JWT token creation/validation support
- Session management (including JDBC session storage support)
- Level-2 caching (Ehcache + JCache)
- Kafka producer/consumer integration capability

## Prerequisites

- Java 21
- MySQL running locally/remotely
- Maven (optional; wrapper included)
- (Optional) Kafka broker for Kafka-related features
- (Optional) Google OAuth credentials for social login

## Configuration

Main config file:

- `src/main/resources/application.properties`

Important properties present in repo:

- `spring.datasource.url=jdbc:mysql://localhost:3306/ez_manage`
- `spring.datasource.username=root`
- `spring.datasource.password=root`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.security.user.name=vikas`
- `spring.security.user.password=vikas`
- Google OAuth placeholders via env vars:
  - `GOOGLE_CLIENT_ID`
  - `GOOGLE_CLIENT_SECRET`

### Recommended for local setup

1. Create MySQL database: `ez_manage`
2. Update DB username/password if needed
3. Set OAuth env variables only if using Google login
4. Keep secrets out of source control

## Run the Application

From repository root:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

Default app URL:

- `http://localhost:8080`

## Build and Test

```bash
./mvnw clean test
./mvnw clean package
```

## Caching

L2 cache is enabled in `application.properties` and configured via `ehcache.xml`.
This can reduce repeated DB hits for frequently accessed entities.

## Security Notes

Current properties include default credentials and local DB secrets for development.
Before production:

- move secrets to environment variables / secret manager
- disable verbose debug logging
- enforce strong password policies
- configure HTTPS, CORS, and token expiry/rotation policies

## Suggested Improvements

- Add API documentation (OpenAPI/Swagger)
- Add Docker Compose for app + MySQL + Kafka
- Add explicit module/package-level architecture docs
- Add integration tests for auth/session/JWT flows
- Rename `sql scipts` → `sql scripts` for clarity

## Disclaimer

This project appears development-oriented. Validate hardening, observability, and deployment settings before production use.
