# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM public.ecr.aws/amazonlinux/amazonlinux:2023 AS builder

# Install Java 8 (Corretto), Maven, and findutils
RUN dnf update -y && \
    dnf install -y java-1.8.0-amazon-corretto-devel maven findutils tar gzip && \
    dnf clean all

WORKDIR /builder

# Copy dependency descriptors first for layer caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src/ src/

# Build the application (skip tests)
RUN mvn package -DskipTests -B -T 1C

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM public.ecr.aws/amazonlinux/amazonlinux:2023

# Install Java 8 runtime and findutils
RUN dnf update -y && \
    dnf install -y java-1.8.0-amazon-corretto findutils && \
    dnf clean all

# Create non-root user
RUN dnf install -y shadow-utils && \
    groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Copy the built JAR from builder stage
COPY --chown=appuser:appuser --from=builder /builder/target/legacy-ecommerce-1.0.0.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
