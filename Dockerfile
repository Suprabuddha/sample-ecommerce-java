# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM public.ecr.aws/amazonlinux/amazonlinux:2023 AS builder

RUN dnf update -y && \
    dnf install -y java-21-amazon-corretto-devel maven findutils shadow-utils && \
    dnf clean all

# Explicitly set JAVA_HOME to Corretto 21
ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Create appuser
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /builder

# Copy project files
COPY --chown=appuser:appuser pom.xml .
COPY --chown=appuser:appuser src/ src/

# Build the application (skip tests)
RUN mvn package -DskipTests -B -T 1C

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM public.ecr.aws/amazonlinux/amazonlinux:2023

RUN dnf update -y && \
    dnf install -y java-21-amazon-corretto-headless findutils shadow-utils && \
    dnf clean all

ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Create appuser
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --chown=appuser:appuser --from=builder /builder/target/legacy-ecommerce-1.0.0.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
