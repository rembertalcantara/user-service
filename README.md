# User Service

Microservice responsible for user profile aggregation and management.

Built with:

- Java 25
- Spring Boot 3
- Spring Cloud OpenFeign
- Spring Security
- Spring Cache
- Docker
- Kubernetes

---

# Architecture

```text
Client -> user-service -> External API / WireMock
```

The service retrieves user profile information from external APIs using OpenFeign clients.

---

# Technologies

| Technology | Version |
|---|---------|
| Java | 25      |
| Spring Boot | 3.x     |
| Maven | 3.x     |
| Docker | Latest  |
| Kubernetes | 1.28+   |

---

# Project Structure

```text
src
 ├── main
 │   ├── java
 │   │   └── pt/com/aubay/userservice
 │   │       ├── config
 │   │       ├── controller
 │   │       ├── client
 │   │       ├── service
 │   │       ├── advice
 │   │       └── model
 │   └── resources
 │       └── application.yml
 │
 └── test
```

---

# Environment Variables

```bash
export BASIC_USER=admin
export BASIC_PASSWORD=admin123
export USER_PROFILE_BASE_URL=http://wiremock:8080
```

---

# Running Locally

## Build

```bash
mvn clean package
```

## Run

```bash
mvn spring-boot:run
```

Application URL:

```text
http://localhost:8081
```

---

# Health Check

```http
GET /actuator/health
```

Example:

```bash
curl http://localhost:8081/actuator/health
```

---

# API Endpoints

## Get User Profile

```http
GET /api/users/{id}/profile
```

Example:

```bash
curl -u admin:admin123 \
http://localhost:8081/api/users/1/profile
```

Example response:

```json
{
  "userId": "1",
  "demographics": {
    "age": 30,
    "country": "PT"
  },
  "preferences": {
    "favoriteCategory": "electronics"
  }
}
```

---

# WireMock

The project uses external service WireMock to simulate external APIs.

## Example Stub

```json
{
  "request": {
    "method": "GET",
    "url": "/api/users/1/profile"
  },
  "response": {
    "status": 200,
    "headers": {
      "Content-Type": "application/json"
    },
    "jsonBody": {
      "userId": "1",
      "demographics": {
        "age": 30,
        "country": "PT"
      },
      "preferences": {
        "favoriteCategory": "electronics"
      }
    }
  }
}
```

---

# Docker

## Build Image

```bash
docker build -t user-service:latest .
```

## Run Container

```bash
docker run -p 8081:8081 \
-e BASIC_USER=admin \
-e BASIC_PASSWORD=admin123 \
-e USER_PROFILE_BASE_URL=http://host.docker.internal:8080 \
user-service:latest
```

---

# Kubernetes

## Apply Manifests

```bash
kubectl apply -f k8s.yaml
```

## Check Pods

```bash
kubectl get pods -n recommendation
```

## View Logs

```bash
kubectl logs -f deploy/user-service -n recommendation
```

## Port Forward

```bash
kubectl port-forward svc/user-service 8081:8081 -n recommendation
```

---

# Testing

## Run Tests

```bash
mvn test
```

## Coverage

```bash
mvn clean verify
```

---

# Security

The project uses:

- Spring Security
- Basic Authentication
- Protected Actuator endpoints
- Kubernetes Secrets

---

# Future Improvements

- JWT Authentication
- OAuth2 / Keycloak
- Resilience4j Circuit Breaker
- Distributed Tracing
- Prometheus + Grafana
- GitHub Actions CI/CD
- Testcontainers
