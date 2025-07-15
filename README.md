# Health Management System

## Overview
A comprehensive health management platform enabling users to manage appointments, interact with the community, and connect via real-time chat. Includes an admin dashboard for system management and analytics.

---

## Features
- **Appointments:** Book, cancel, and view upcoming appointments.
- **Community:** Join discussions, browse posts, and comment.
- **Chat:** Real-time conversations with other users and doctors (for telemedicine).
- **Profile Management:** View/update personal details, change passwords securely.
- **Admin Panel:** Manage clients, appointments, and posts.
- **Analytics:** User, appointment, and article statistics.
- **Doctor Schedules:** Manage and export doctor schedules.
- **Health Providers:** Manage providers and doctor assignments.
- **Medication Management:** CRUD for medications.
- **Security:** JWT authentication, role-based access, XSS protection.

---

## Technologies
- **Backend:** Spring Boot 3, Java 17, JPA/Hibernate, Liquibase, Redis, PostgreSQL, MapStruct, Spring Security, Quartz, OpenAPI/Swagger, Docker, Kubernetes
- **Frontend:** [Flutter app](https://github.com/N-D-Duy/health-management-fe)
- **Admin Dashboard:** [Bootstrap dashboard](https://github.com/N-D-Duy/admin-health-management)

---

## Architecture & Main Modules
- Modular structure: Account, User, Appointment, Article, Analytics, Health Provider, Doctor Schedule, Auth, etc.
- RESTful API with endpoints for user, appointment, article, analytics, health provider, and authentication.
- Security: JWT-based authentication, role-based access, XSS protection.
- Database migrations via Liquibase.
- Caching and scheduling with Redis and Quartz.

---

## API Endpoints (Examples)
- `/auth`: Register, login, refresh token, logout, activate account
- `/users`: CRUD for users, doctors, patients, profile update, top-rated doctors
- `/appointments`: CRUD for appointments, cancel, export, medication management
- `/articles`: CRUD for articles, voting, commenting
- `/health-providers`: CRUD for health providers, doctor assignments
- `/analytics`: Data analysis endpoints
- `/schedule`: Doctor schedule management and export

API documentation available at `/api/v1/core/api-docs/swagger-ui.html`

---

## Setup & Local Development
1. **Requirements:**
   - Java 17+
   - Gradle
   - PostgreSQL (default: `localhost:5434`, user: `postgres`, pass: `duynguyen`)
   - Redis (default: `localhost:6379`)
2. **Clone the repo:**
   ```bash
   git clone https://github.com/N-D-Duy/health-management.git
   cd health-management
   ```
3. **Configure environment:**
   - Edit `src/main/resources/application-dev.yml` for local DB/Redis settings.
   - Liquibase auto-applies schema/data on first run.
4. **Run locally:**
   ```bash
   ./gradlew bootRun
   ```

---

## Docker & Kubernetes
- **Docker:**
  - Build: `docker build -t health-management .`
  - Run: `docker run -p 8080:8080 health-management`
- **Kubernetes:**
  - Manifests in `k8s/` (deployment, service, secrets, etc.)
  - Set environment variables for DB, Redis, and mail client as in `k8s/deployment.yml`

---

## Database
- **PostgreSQL** (configurable via env or yml)
- **Liquibase** for schema and initial data:
  - Schema: `src/main/resources/changelog/ddl/changelog-0001.sql`
  - Data: `src/main/resources/changelog/data/changelog-0001-core.sql`

---

## How to Run
- **Build:**
  ```bash
  ./gradlew build
  ```
- **Run:**
  ```bash
  ./gradlew bootRun
  # or with Docker
  docker build -t health-management .
  docker run -p 8080:8080 health-management
  ```
- **API Docs:**
  Visit [http://localhost:8080/api/v1/core/api-docs/swagger-ui.html](http://localhost:8080/api/v1/core/api-docs/swagger-ui.html)

---

## Deployment
- See `k8s/` for Kubernetes deployment.
- Docker image: `docker.io/duynguyen03/health-management:v4`

---

## Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.


---

## Contact
- Author: Duy Nguyen
- Email: nguyenducduypc160903@gmail.com
- [Project Issues](https://github.com/N-D-Duy/health-management/issues)