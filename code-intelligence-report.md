# Code Intelligence Report

## Build System
- Maven (pom.xml detected)
- No Gradle build files detected

## Language Versions
- Java: 17 (java.version, maven.compiler.source, maven.compiler.target)

## Spring Boot Version
- 3.2.12 (spring-boot-starter-parent)

## Jackson Usage
- Files with Jackson imports:
  - src/main/java/com/example/ecommerce/model/Order.java (com.fasterxml.jackson.annotation.JsonIgnoreProperties)
  - src/main/java/com/example/ecommerce/model/OrderItem.java (com.fasterxml.jackson.annotation.JsonIgnoreProperties)
- Custom serializers: 0
- Custom deserializers: 0
- ObjectMapper direct usage: 0

## Spring Security
- SecurityFilterChain methods: 1
- @EnableWebSecurity: 1
- Files: src/main/java/com/example/ecommerce/config/SecurityConfig.java

## Testing Infrastructure
- @MockBean count: 0
- @SpyBean count: 0
- Testcontainers usage: none
- Test files: 0

## Observability
- Micrometer: not present
- OpenTelemetry: not present
- Actuator: not present (management properties in application.properties but no actuator starter)

## Reactive / WebFlux
- Reactive files: none

## Size Metrics
- Total Java files: 16
- Total lines of code: 2356
- Module count: 1

## Build Dependencies
  org.springframework.boot:spring-boot-starter-parent
  org.springframework.boot:spring-boot-starter-web
  org.springframework.boot:spring-boot-starter-data-jpa
  org.springframework.boot:spring-boot-starter-security
  org.springframework.boot:spring-boot-starter-thymeleaf
  com.h2database:h2
  org.apache.commons:commons-lang3
  com.fasterxml.jackson.core:jackson-databind
  org.json:json
  org.apache.httpcomponents.client5:httpclient5
  jakarta.validation:jakarta.validation-api
  org.hibernate.validator:hibernate-validator
  org.springframework.boot:spring-boot-starter-test
  junit:junit
  org.springframework.boot:spring-boot-maven-plugin
  org.apache.maven.plugins:maven-compiler-plugin

## Deprecated Boot 3.x APIs
- com.fasterxml.jackson.core imports (non-annotation): 0
- javax.* Jakarta-EE imports: 0
- WebSecurityConfigurerAdapter: 0
- antMatchers: 0
- Legacy .and() security DSL: 0
