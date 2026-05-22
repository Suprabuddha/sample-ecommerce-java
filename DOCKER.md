# Docker Instructions — Legacy Ecommerce Application

## Overview

This is a Spring Boot 3.2.12 e-commerce application using Java 21 (Amazon Corretto) and an H2 in-memory database. It is packaged as an executable fat JAR and served on port `8080` with context path `/legacy-ecommerce`.

## Build

Run the following command from the **root of the extracted source directory** (the directory containing `pom.xml` and `Dockerfile`):

```bash
# From the root of the extracted source archive:
docker build -t legacy-ecommerce .
```

## Run

```bash
docker run -p 8080:8080 legacy-ecommerce
```

## Application URLs

Once running, the application is accessible at:

| URL | Description |
|-----|-------------|
| `http://localhost:8080/legacy-ecommerce/` | Home page |
| `http://localhost:8080/legacy-ecommerce/products` | Product catalog |
| `http://localhost:8080/legacy-ecommerce/cart` | Shopping cart |
| `http://localhost:8080/legacy-ecommerce/orders` | Order history |
| `http://localhost:8080/legacy-ecommerce/h2-console` | H2 database console |
| `http://localhost:8080/legacy-ecommerce/api/products` | Products REST API |
| `http://localhost:8080/legacy-ecommerce/api/orders` | Orders REST API |

## Environment Variables

The following environment variables can be used to override default configuration at runtime:

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | HTTP port the application listens on | `8080` |
| `SPRING_DATASOURCE_URL` | JDBC URL for the database | `jdbc:h2:mem:ecommerce` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `sa` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | *(empty)* |
| `SPRING_SECURITY_USER_NAME` | Default admin username | `admin` |
| `SPRING_SECURITY_USER_PASSWORD` | Default admin password | `admin123` |
| `APP_ENCRYPTION_KEY` | Application encryption key | `defaultkey123` |
| `APP_JWT_SECRET` | JWT signing secret | `mysecretkey` |

### Example with custom port

```bash
docker run -p 9090:9090 -e SERVER_PORT=9090 legacy-ecommerce
```

## ⚠️ Secret Handling

The following secrets are currently hardcoded in `src/main/resources/application.properties` and should be rotated and injected securely in production:

- `spring.security.user.password` (line 58)
- `app.encryption.key` (line 72)
- `app.jwt.secret` (line 73)

For production deployments, use:
- **ECS**: [AWS Secrets Manager integration](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/secrets-envvar-secrets-manager.html)
- **EKS**: [Secrets Manager or KMS encryption](https://docs.aws.amazon.com/eks/latest/userguide/security-k8s.html)

## Notes

- The H2 in-memory database is ephemeral — all data is lost when the container stops. For persistent storage, configure an external database (e.g., PostgreSQL or MySQL) via `SPRING_DATASOURCE_URL`.
- The application runs as a non-root `appuser` for security.
- Logs are written to stdout/stderr and are compatible with ECS/CloudWatch log collection.
