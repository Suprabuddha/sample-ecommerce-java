# Docker Build & Run Instructions

## Overview

**Application:** Legacy Ecommerce (Spring Boot)  
**Language:** Java 8 (Amazon Corretto)  
**Framework:** Spring Boot 2.1.18  
**Port:** 8080 (context path: `/legacy-ecommerce`)  
**Database:** H2 in-memory (no external database required)

---

## Build

From the root of the extracted source directory:

```bash
docker build -t legacy-ecommerce .
```

---

## Run

```bash
docker run -p 8080:8080 legacy-ecommerce
```

The application will be available at:
- **Home:** http://localhost:8080/legacy-ecommerce/
- **Products:** http://localhost:8080/legacy-ecommerce/products
- **Cart:** http://localhost:8080/legacy-ecommerce/cart
- **Orders:** http://localhost:8080/legacy-ecommerce/orders
- **H2 Console:** http://localhost:8080/legacy-ecommerce/h2-console
- **REST API:** http://localhost:8080/legacy-ecommerce/api/products

---

## Environment Variables

The following environment variables can be passed at runtime to override defaults. These are provided via ECS task definitions or Kubernetes manifests in production.

| Variable | Description | Default |
|---|---|---|
| `SERVER_PORT` | HTTP port the application listens on | `8080` |
| `SPRING_SECURITY_USER_NAME` | Default admin username | `admin` |
| `SPRING_SECURITY_USER_PASSWORD` | Default admin password (**secret**) | `admin123` |
| `APP_ENCRYPTION_KEY` | Application encryption key (**secret**) | `defaultkey123` |
| `APP_JWT_SECRET` | JWT signing secret (**secret**) | `mysecretkey` |

Example with overrides:

```bash
docker run -p 8080:8080 \
  -e SPRING_SECURITY_USER_PASSWORD=my-secure-password \
  -e APP_ENCRYPTION_KEY=my-encryption-key \
  -e APP_JWT_SECRET=my-jwt-secret \
  legacy-ecommerce
```

---

## Secret Management

**⚠️ Important:** The default values for `SPRING_SECURITY_USER_PASSWORD`, `APP_ENCRYPTION_KEY`, and `APP_JWT_SECRET` are hardcoded in `application.properties` and must be rotated before production use.

For production deployments, inject secrets securely using:
- **ECS:** [AWS Secrets Manager integration](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/secrets-envvar-secrets-manager.html)
- **EKS:** [Secrets Manager or KMS encryption](https://docs.aws.amazon.com/eks/latest/userguide/security-k8s.html)

---

## Notes

- The H2 in-memory database is initialized fresh on every container start. Data does not persist across restarts.
- The application runs as a non-root user (`appuser`) for security.
- Logs are written to stdout/stderr for container log aggregation compatibility.
