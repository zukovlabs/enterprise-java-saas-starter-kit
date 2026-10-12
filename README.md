# Enterprise SaaS Starter — Community Edition

![Version](https://img.shields.io/badge/version-1.0.0--community-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green.svg)
![Angular](https://img.shields.io/badge/Angular-22-red.svg)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL_17-336791.svg)
![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)
![License](https://img.shields.io/badge/License-MIT-brightgreen.svg)
[![Website](https://img.shields.io/badge/Website-zukovlabs.com-blue.svg)](https://zukovlabs.com)

A **production-ready fullstack boilerplate** for building SaaS applications — Java 21 + Spring Boot 3.4.1 backend, Angular 22 frontend, PostgreSQL database, fully containerized with Docker Compose. Clone, configure, and ship.

> 🚀 **Latest Update:** The database has been migrated to **PostgreSQL 17** (Previously **MSSQL 2022**). To celebrate, use code <code><b>POSTGRES17</b></code> for **€50 off** the [PRO version](https://zukovlabs.lemonsqueezy.com/checkout/buy/0dbe2f00-c90f-414b-b2d1-4e589f487058) — valid until **December 31, 2026**.

> ⭐ **Found this useful? Drop a star** — it helps other Java developers find this project.  
> 🌐 **Full Features & Live Demo:** [zukovlabs.com](https://zukovlabs.com)

---

## See the PRO Version in Action (2-minute Demo)

[![Watch the Demo](https://img.youtube.com/vi/el6x6VmtAH8/maxresdefault.jpg)](https://www.youtube.com/watch?v=el6x6VmtAH8)
*▶ Click to watch the full walkthrough on YouTube*
---

## Table of Contents

- [Upgrade to PRO](#-upgrade-to-pro)
- [Free vs. PRO Comparison](#-free-vs-pro-comparison)
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [Database & Migrations](#database--migrations)
- [API Reference](#api-reference)
- [Project Structure](#project-structure)
- [License](#license)

---

## ⚡ Upgrade to PRO

> **Skip 200+ hours of engineering work.**

The Community Edition gives you a solid foundation. The **PRO version** ships everything you need to launch a real, monetized SaaS product — without building it from scratch.

### What you get with PRO that you cannot build in a weekend:

| Area | What PRO Delivers                                                                                                                                         |
|------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Payments** | Full Stripe integration: Checkout Sessions, Webhook handling with signature verification, Billing Portal, and automatic post-payment login via Magic Link |
| **Passwordless Auth** | Magic Link (one-time email links, 15-min TTL) — a proven conversion booster for SaaS onboarding                                                           |
| **Email Service** | Thymeleaf HTML email templates: welcome, magic link, payment success — production-ready and styled                                                        |
| **Security** | IP-based sliding-window rate limiter on all auth endpoints — brute-force and flood protection                                                             |
| **Data Layer** | Server-side pagination (`Spring Pageable` + `MatPaginator`) — Community Edition loads all rows client-side; PRO paginates at the database level for scale |
| **Access Control** | Strict RBAC with three roles (`ADMIN` / `MANAGER` / `USER`) enforced via `@PreAuthorize` at the method level                                                  |
| **Multi-Tenancy** | Entity ownership enforcement — users can only see and delete their own records. `403 FORBIDDEN` if violated                                               |
| **Signal Caching** | Angular `signal<T>` cache in services — two components, one HTTP request                                                                                  |
| **Test Suite** | 13 test files, 88 cases: Mockito strict stubs, `ArgumentCaptor` assertions, Vitest frontend tests, real `ObjectMapper` for Stripe JSON fallback path     |

### The math is simple:

- **Payments:** Stripe integration (webhooks, billing portal, edge cases): **~40 hours**
- **Auth & Comms:** Magic Link logic, Email Service, Thymeleaf templates: **~40 hours**
- **Security & Core:** Rate limiting, strict RBAC, data ownership (403): **~40 hours**
- **Frontend UX:** Dashboard charts, Server-side pagination, Signal Cache: **~40 hours**
- **Quality Assurance:** 88 strict test cases, Mockito, Vitest: **~40 hours**
- **Total: 200+ hours of deep engineering** — or one license purchase.

### 👉 [Get PRO Version — €149 (Use code ANGULAR22 at checkout for €50 off!) →](https://zukovlabs.lemonsqueezy.com/checkout/buy/0dbe2f00-c90f-414b-b2d1-4e589f487058)

---

## Free vs. PRO Comparison

| Feature                                                                 | Community (Free) | PRO |
|-------------------------------------------------------------------------|:---:|:---:|
| **Java 21 + Spring Boot 3.4.1**                                         | ✅ | ✅ |
| **Angular 22 Standalone Components**                                    | ✅ | ✅ |
| **Database + Flyway Migrations**                                        | ✅ PostgreSQL 17 | ✅ PostgreSQL 17 |
| **Multi-stage Docker Builds**                                           | ✅ | ✅ |
| **Nginx SPA Hosting + Security Headers**                                | ✅ | ✅ |
| **Database Healthcheck (no startup race)**                              | ✅ | ✅ |
| **JWT Auth (Access + Refresh Tokens)**                                  | ✅ Basic | ✅ Enhanced |
| **Refresh Token Rotation**                                              | ✅ | ✅ |
| **BCrypt Password Hashing**                                             | ✅ | ✅ |
| **User Profile Management**                                             | ✅ | ✅ |
| **Basic Customer CRUD**                                                 | ✅ | ✅ |
| **Dashboard Stats API**                                                 | ✅ | ✅ |
| **Angular Material UI**                                                 | ✅ | ✅ |
| **401 Race Condition Protection (Interceptor)**                         | ✅ | ✅ |
| **Auth Guard (JWT `exp` validation + redirect)**                        | ✅ | ✅ |
| —                                                                       | — | — |
| **Stripe Checkout + Webhook Handling**                                  | ❌ | ✅ |
| **Stripe Billing Portal (self-service)**                                | ❌ | ✅ |
| **Magic Link (Passwordless Auth)**                                      | ❌ | ✅ |
| **Post-Payment Auto-Login via Magic Link**                              | ❌ | ✅ |
| **Email Service (Thymeleaf HTML templates)**                            | ❌ | ✅ |
| **IP Rate Limiting (brute-force protection)**                           | ❌ | ✅ |
| **Server-Side Pagination (Spring `Pageable` backend + `MatPaginator`)** | ❌ | ✅ |
| **Strict RBAC (ADMIN / MANAGER / USER)**                                | ❌ | ✅ |
| **Entity Ownership Enforcement (403)**                                  | ❌ | ✅ |
| **Signal Cache (no duplicate HTTP calls)**                              | ❌ | ✅ |
| **Dashboard Charts (Chart.js)**                                         | ❌ | ✅ |
| **13 Test Files / 88 Test Cases**                                       | ❌ | ✅ |
| **Mockito Strict Stubs + ArgumentCaptor**                               | ❌ | ✅ |
| **Vitest Frontend Test Suite**                                          | ❌ | ✅ |
| **Fail-Fast JWT Secret Validation**                                     | ❌ | ✅ |
| **Commercial Use License**                                              | ✅ MIT | ✅ Commercial |

### 👉 [Upgrade to PRO (Use code ANGULAR22 at checkout for €50 off!) →](https://zukovlabs.lemonsqueezy.com/checkout/buy/0dbe2f00-c90f-414b-b2d1-4e589f487058)

---

## Overview

The Community Edition is a clean, runnable fullstack starting point — no toy demo, no half-implemented code. It ships with:

- **Stateless JWT authentication** with access and refresh token rotation
- **Basic Customer CRM** — create, list, edit, and delete customer records
- **User profile management** — update name and change password
- **Dashboard stats** endpoint — aggregate data from the backend
- **Consistent error handling** — every API error is an RFC 7807 `ProblemDetail` with field-level validation messages
- **One-command Docker deploy** — `docker compose up -d --build` starts the entire stack
- **Nginx-served Angular SPA** with `/api/*` proxy to the Spring Boot backend, gzip compression, and security headers
- **It compiles successfully, passes standard health checks, and provides a secure starting point.**

### Architecture

```
┌────────────────────────────────────────────────────┐
│                   Docker Compose                   │
│                                                    │
│  ┌──────────┐    ┌───────────────┐    ┌─────────┐  │
│  │ Postgres │───▶│  Spring Boot  │◀───│  Nginx  │  │
│  │  :5432   │    │    :8080      │    │  :8081  │  │
│  └──────────┘    └───────────────┘    └────┬────┘  │
│   healthcheck      Flyway migrations        │       │
│   passes first     on startup           Angular SPA │
└────────────────────────────────────────────────────┘
                         ▲
                    Browser Client
```

**Request flow:**

```
Request → JwtAuthenticationFilter → SecurityConfig rules → Controller → Service → Repository
```

---

## Tech Stack

| Component | Technology | Version         |
|-----------|-----------|-----------------|
| **Language** | Java | 21 LTS          |
| **Framework** | Spring Boot | 3.4.1           |
| **Security** | Spring Security | 6.x             |
| **ORM** | Hibernate / JPA | —               |
| **JWT** | JJWT | 0.12.3 (HS512)  |
| **Migrations** | Flyway | —               |
| **Build Tool** | Maven | 3.9.6           |
| **Frontend** | Angular | 22 (Standalone) |
| **UI Library** | Angular Material | 22              |
| **Reactive** | RxJS | 7.8             |
| **Database** | PostgreSQL | 17              |
| **Containerization** | Docker + Compose | —               |
| **Web Server** | Nginx | Alpine          |

---

## Prerequisites

Install these before you start:

| Tool | Purpose | Min Version |
|------|---------|-------------|
| **Docker Desktop** | Run the full stack | Latest      |
| **Java 21 JDK** | Local backend development | 21 LTS      |
| **Node.js** | Local frontend development | 22+         |

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/zukovlabs/enterprise-java-saas-starter-kit.git
cd enterprise-java-saas-starter-kit
```

### 2. Configure Environment Variables

Copy the example file and set your values:

```bash
copy .env.example .env
```

Open `.env` and set at minimum:

```env
JWT_SECRET=your-256-bit-hex-secret-here
DB_PASSWORD=YourStrongPassword1!
CORS_ALLOWED_ORIGINS=http://localhost:8081
```

> Generate a strong JWT secret: `openssl rand -hex 32`

### 3. Start the Stack

```bash
docker compose up -d --build
```

Docker Compose will:

1. Start the PostgreSQL container and wait for the healthcheck to pass
2. Start the Spring Boot backend — Flyway migrations run automatically on startup
3. Start the Angular frontend served via Nginx

### 4. Open the Application

| Service | URL |
|---------|-----|
| **Frontend** | http://localhost:8081 |
| **Backend API** | http://localhost:8080 |
| **Database** | `localhost:5432` |

### 5. Log In with the Demo Accounts

Flyway seeds two accounts on first startup:

| Email | Password | Role |
|-------|----------|------|
| `admin@saaskit.com` | `Admin1234!` | ADMIN |
| `user@saaskit.com` | `User1234!` | USER |

### 6. Useful Commands

```bash
# Watch backend logs
docker compose logs -f backend

# Stop the stack
docker compose down

# Stop and remove volumes (fresh DB)
docker compose down -v

# Rebuild a single service
docker compose up -d --build backend
```

### 7. Local Development (Optional)

If you want to edit the code with full IDE support (IntelliSense, types), you should install the dependencies locally on your host machine:

**Frontend:**
```bash
cd frontend
npm install
npm start # runs Angular dev server on http://localhost:4200
```

---

## Environment Variables

All variables are sourced from the `.env` file (see `.env.example` for a full template with comments).

| Variable | Required | Default | Description |
|----------|:--------:|---------|-------------|
| `APP_URL` | ✅ | `http://localhost:8081` | Base URL of the application |
| `JWT_SECRET` | ✅ | — | 256-bit hex string for HS512 signing |
| `JWT_EXPIRATION_MS` | — | `86400000` (24h) | Access token TTL in milliseconds |
| `JWT_REFRESH_EXPIRATION_MS` | — | `604800000` (7d) | Refresh token TTL in milliseconds |
| `DB_PASSWORD` | ✅ | `StrongP@ssw0rd!` | PostgreSQL `postgres` user password |
| `CORS_ALLOWED_ORIGINS` | ✅ | `http://localhost:4200,http://localhost:8081,http://localhost` | Comma-separated allowed CORS origins |

---

## Database & Migrations 

Schema is managed by **Flyway** — migrations run automatically on every backend startup.

**Migration files:** `src/main/resources/db/migration/`

| File | Description |
|------|-------------|
| `V1__init_schema.sql` | Creates `users` and `customers` tables. Seeds two demo accounts and sample customers. Refresh tokens are stateless JWTs — no database table required. |

**Default development credentials:**

| Field | Value |
|-------|-------|
| Host | `localhost:5432` |
| User | `postgres` |
| Password | `StrongP@ssw0rd!` (override via `DB_PASSWORD`) |
| Database | `saas_db` |

> **Note:** Demo accounts seeded by Flyway use `{noop}` plain-text passwords for developer convenience. All passwords created via `/api/auth/register` are BCrypt-encoded automatically.

---

## API Reference

### Authentication — `/api/auth`

| Method | Endpoint | Auth | Description |
|--------|----------|:----:|-------------|
| `POST` | `/api/auth/register` | — | Register a new user |
| `POST` | `/api/auth/login` | — | Login, returns `accessToken` + `refreshToken` |
| `POST` | `/api/auth/refresh` | — | Rotate tokens using a valid refresh token |

**Login response:**
```json
{
  "token": "eyJ...",
  "refreshToken": "eyJ..."
}
```

All subsequent requests must include:
```
Authorization: Bearer <token>
```

### Users — `/api/user`

| Method | Endpoint | Auth | Description |
|--------|----------|:----:|-------------|
| `GET` | `/api/user/profile` | ✅ | Get the authenticated user's profile |
| `PUT` | `/api/user/profile` | ✅ | Update first name and last name |
| `PUT` | `/api/user/password` | ✅ | Change password (requires current password) |

### Customers — `/api/customers`

| Method | Endpoint | Auth | Description |
|--------|----------|:----:|-------------|
| `GET` | `/api/customers` | ✅ | List all customers (ordered by ID) |
| `POST` | `/api/customers` | ✅ | Create a new customer — `201 Created` + `Location` header |
| `PUT` | `/api/customers/{id}` | ✅ | Update an existing customer |
| `DELETE` | `/api/customers/{id}` | ✅ | Delete a customer by ID — `204 No Content` |

**Customer request body** (validated with Bean Validation):
```json
{
  "firstName": "Jeff",
  "lastName": "Bezos",
  "email": "jeff@amazon.com",
  "company": "Amazon",
  "status": "Active"
}
```
`status` is one of `New`, `Lead`, `Active`, `Inactive` (defaults to `New`). Emails are unique across customers.

### Dashboard — `/api/dashboard`

| Method | Endpoint | Auth | Description |
|--------|----------|:----:|-------------|
| `GET` | `/api/dashboard/stats` | ✅ | Get aggregate stats (total and active customer counts) |

### Error Responses

Every error is returned as [RFC 7807 Problem Details](https://www.rfc-editor.org/rfc/rfc7807) (`application/problem+json`), produced by a single `GlobalExceptionHandler`:

```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more fields are invalid",
  "instance": "/api/customers",
  "errors": {
    "email": "Invalid email format",
    "status": "Status must be one of: New, Lead, Active, Inactive"
  }
}
```

| Status | When |
|:------:|------|
| `400` | Validation failed (`errors` lists each invalid field), malformed JSON, wrong current password |
| `401` | Missing/invalid/expired access token, bad login credentials, invalid refresh token |
| `404` | Resource does not exist |
| `409` | Duplicate email (customer or user registration) |
| `500` | Unexpected error — details are logged server-side, never leaked to the client |

A `401` on a protected endpoint triggers the Angular interceptor to refresh the token and retry the request automatically.

---

## Project Structure

```
enterprise-saas-starter-core/
│
├── src/main/java/com/saaskit/starter/
│   ├── SaasApplication.java            # Spring Boot entry point (@SpringBootApplication)
│   ├── config/
│   │   ├── ApplicationConfig.java      # Auth provider, password encoder, UserDetailsService
│   │   └── SecurityConfig.java         # Filter chain, CORS, public vs. protected endpoints
│   │
│   ├── controller/
│   │   ├── AuthController.java         # /api/auth/** (register, login, refresh)
│   │   ├── CustomerController.java     # /api/customers (list, create, update, delete)
│   │   ├── DashboardController.java    # /api/dashboard/stats
│   │   └── UserController.java         # /api/user/** (profile, password)
│   │
│   ├── service/                        # Business logic + transaction boundaries
│   │   ├── AuthService.java            # Registration, login, JWT refresh logic
│   │   ├── CustomerService.java        # Customer CRUD, unique email checks, default status
│   │   ├── DashboardService.java       # Aggregate customer stats
│   │   └── UserService.java            # Profile & password management
│   │
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java # @RestControllerAdvice → RFC 7807 ProblemDetail for every error
│   │   ├── ResourceNotFoundException.java   # → 404
│   │   ├── DuplicateResourceException.java  # → 409
│   │   └── InvalidTokenException.java       # → 401
│   │
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── CustomerRepository.java
│   │
│   ├── model/
│   │   ├── User.java                   # User entity (email, password, role)
│   │   └── Customer.java               # Customer entity
│   │
│   ├── dto/                            # Java Records — validated request/response DTOs
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   ├── RefreshTokenRequest.java
│   │   ├── AuthResponse.java
│   │   ├── CustomerRequest.java
│   │   ├── CustomerResponse.java
│   │   ├── DashboardStats.java
│   │   ├── UpdateProfileRequest.java
│   │   └── ChangePasswordRequest.java
│   │
│   └── security/
│       ├── JwtUtils.java               # HS512 token generation, parsing, expiry check
│       ├── JwtAuthenticationFilter.java# Extracts + validates JWT, sets SecurityContext
│       └── ProblemDetailAuthenticationEntryPoint.java # 401 ProblemDetail for unauthenticated requests
│
├── src/main/resources/
│   ├── application.yml                 # Full configuration (environment-variable driven)
│   └── db/migration/
│       └── V1__init_schema.sql         # Schema creation + demo data seed
│
├── src/test/java/                      # Backend unit tests (JUnit 5 + Mockito)
│
├── frontend/
│   ├── src/app/
│   │   ├── pages/
│   │   │   ├── landing/                # Public marketing / home page
│   │   │   ├── login/                  # Email + password login form
│   │   │   ├── register/               # Registration form
│   │   │   ├── dashboard/              # Stats overview page
│   │   │   ├── customers/              # Customer list + create/delete
│   │   │   │   └── customer-dialog/    # Add / edit customer dialog component
│   │   │   └── settings/               # Profile & password management
│   │   │
│   │   ├── services/
│   │   │   ├── auth.service.ts         # Login, register, refresh, logout
│   │   │   ├── user.service.ts         # Profile CRUD
│   │   │   ├── customer.service.ts     # Customer API calls
│   │   │   └── dashboard.service.ts    # Stats API call
│   │   │
│   │   ├── components/
│   │   │   └── confirm-dialog/         # Reusable confirmation dialog
│   │   │
│   │   ├── layout/main-layout/         # App shell: sidebar + header
│   │   ├── models/                     # TypeScript interfaces (customer, dashboard-stats)
│   │   ├── auth.guard.ts               # Checks token presence in localStorage before route activation
│   │   └── auth.interceptor.ts         # Injects Bearer token + handles 401 with token refresh
│   │
│   ├── nginx.conf                      # SPA routing, /api reverse proxy, gzip, security headers
│   └── Dockerfile                      # Multi-stage: Node 24 → Nginx Alpine
│
├── Dockerfile                          # Multi-stage: Maven 3.9.6 + JDK 21 → JRE 21 Alpine
├── compose.yaml                        # DB (healthcheck) → Backend → Frontend
├── .env.example                        # All variables with descriptions
└── pom.xml                             # Maven dependencies
```

---

## Want More? Upgrade to PRO

The Community Edition is intentionally minimal. The **PRO version** is what you actually ship to customers.

**PRO adds:**
- Stripe payments (Checkout, Webhooks, Billing Portal)
- Magic Link passwordless authentication
- HTML email service with Thymeleaf templates
- IP rate limiting on auth endpoints
- Server-side pagination (no full-table loads)
- Strict three-tier RBAC (`ADMIN` / `MANAGER` / `USER`)
- Entity ownership enforcement with `403` responses
- Angular Signal cache (zero duplicate HTTP calls)
- Dashboard charts with Chart.js
- 13 test files, 88 cases — Mockito strict stubs, Vitest frontend, `ArgumentCaptor` assertions

### 👉 [See full details at zukovlabs.com →](https://zukovlabs.com)

---

## Contributing

Contributions, bug reports, and feature requests are welcome. Open an issue or submit a pull request.

---

## Support & Questions

Have a question, found a bug, or need help?

* **Technical issues:** Please [open a GitHub Issue](https://github.com/zukovlabs/enterprise-java-saas-starter-kit/issues)
* **Business or private inquiries:** Drop me an email at [support@zukovlabs.com](mailto:romanzukov1983@gmail.com)

---

## License

This project is licensed under the **MIT License** — free to use, modify, and distribute. See [LICENSE](./LICENSE) for details.

**Enterprise SaaS Starter PRO** is a separate commercial product with its own license terms.

---

*Built by ZukovLabs*

