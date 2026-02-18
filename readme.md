# Enterprise SaaS Starter Kit

<div align="center">
  
  ![Version](https://img.shields.io/badge/version-1.0.0-blue.svg?style=for-the-badge)
  ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.1-green.svg?style=for-the-badge&logo=springboot&logoColor=white)
  ![Angular](https://img.shields.io/badge/Angular-21-red.svg?style=for-the-badge&logo=angular&logoColor=white)
  ![Java](https://img.shields.io/badge/Java-21-orange.svg?style=for-the-badge&logo=openjdk&logoColor=white)
  ![Coverage](https://img.shields.io/badge/Tests-JUnit_5-2EA44F.svg?style=for-the-badge&logo=junit5&logoColor=white)
  ![Stripe](https://img.shields.io/badge/Stripe-Payment-635BFF.svg?style=for-the-badge&logo=stripe&logoColor=white)

  <br><br>
  
  
  ### ⚡ Save 200+ hours of architectural setup. 
  **The ultimate production-ready boilerplate for Java Developers.**
  
  <a href="https://zukovlabs.lemonsqueezy.com/checkout/buy/0dbe2f00-c90f-414b-b2d1-4e589f487058">
    <img src="https://img.shields.io/badge/Get%20Source%20Code-Access%20Private%20Repo-blue?style=for-the-badge&logo=rocket&height=40" height="50" />
  </a>

</div>

<div align="center">
  <h3>🎥 Live Demo Walkthrough</h3>
  <video src="https://github.com/user-attachments/assets/e140615e-cd26-4593-8d50-196c14ccfbd3" controls="controls" style="max-width: 100%;">
  </video>
</div>


<br><br>

<img src="assets/landing-top.png" alt="Enterprise SaaS Starter Kit" width="100%" style="border-radius: 10px; box-shadow: 0px 4px 20px rgba(0,0,0,0.1);"/>
  
  <br>

---

## 💎 Why this Starter Kit?
This is not just a template. It is a **Clean Architecture** reference implementation designed for scalability, security, and maintainability.

Don't waste weeks configuring JWT, CORS, Docker, and Stripe webhooks. We've done the heavy lifting so you can focus on your business logic.

---

## 💼 Business Logic & CRM
Everything you need to manage your SaaS users is pre-built.

### 1. Customer Management (CRM)
Full CRUD operations connected to a real MSSQL database.
<div align="center">
  <img src="assets/customers.png" width="100%" style="border-radius: 8px; border: 1px solid #e1e4e8;" />
</div>

<br>

### 2. Analytics & User Dashboard
Clean, responsive dashboard for high-level metrics.
<div align="center">
  <img src="assets/dashboard.png" width="100%" style="border-radius: 8px; border: 1px solid #e1e4e8;" />
</div>

---

## 💰 Monetization & Payments
**Stripe Integration is fully implemented.** No mock data — this connects directly to Stripe Checkout and handles Webhooks securely.

<div align="center">
  <img src="assets/landing-bottom.png" width="100%" style="border-radius: 8px;" />
</div>

* ✅ **Subscription Plans:** Basic & Pro tiers configured.
* ✅ **Secure Webhooks:** Validates Stripe signatures to prevent fraud.
* ✅ **Auto-Provisioning:** Updates user roles automatically upon payment success.

---

## 🏗️ Architecture & Standards

We follow strictly typed, enterprise-grade engineering standards.

### 🛡️ Backend: Spring Boot 3.4.1 (Java 21)
* **Clean Architecture:** Strict separation of `Controller` -> `Service` -> `Repository`.
* **Security First:** Stateless JWT Authentication with BCrypt password hashing.
* **Database Migrations:** **Flyway** included for version-controlled schema changes.
* **Validation:** Robust `@Valid` DTOs to ensure data integrity.

### 🧪 Quality Assurance (Testing)
We don't ship broken code. The kit includes a robust testing suite.
* **Unit Testing:** JUnit 5 & Mockito.
* **Critical Path Coverage:** Tests covering Authentication flows and Stripe Payment logic.
* **Isolated Tests:** Logic is verified without needing the full container stack.

### ⚡ Frontend: Angular 21
* **Standalone Components:** Modern architecture (No NgModules).
* **Type Safety:** Strict TypeScript interfaces for all DTOs.
* **Smart/Dumb Pattern:** Separation of presentation and logic.

---

## 🛠 Tech Stack Overview

| Layer | Technology | Version | Role |
|:---|:---|:---|:---|
| **Core** | Java | **21 LTS** | High-performance backend |
| **Framework** | Spring Boot | **3.4.1** | Dependency Injection & Web |
| **Security** | Spring Security | **6.x** | JWT & RBAC (Role Based Access) |
| **Data** | Hibernate / JPA | | ORM Implementation |
| **Migration** | Flyway | | Database Version Control |
| **Testing** | JUnit 5 & Mockito | **5.x** | Unit & Integration Testing |
| **Frontend** | Angular | **21** | SPA Framework |
| **UI Kit** | Angular Material | **21** | Enterprise Components |
| **Database** | MSSQL / Postgres | **2022** | Relational Data |
| **Infra** | Docker Compose | | Orchestration |

---

## 📄 License & Terms of Use

**Copyright © 2024–2026 ZukovLabs. All rights reserved.**

By purchasing and using the **Enterprise SaaS Starter Kit** (“Software”), you agree to these terms.
**“End Product”** means a compiled/hosted application created by you using the Software, delivered to your customers as a service (SaaS) or used as an internal tool.

### ✅ You MAY
* **Single Product License:** Use the Software to build and deploy **one (1) commercial SaaS product** or one (1) internal business tool per license.
* **Modify & Extend:** Modify, customize, and extend the Software for your needs.
* **Commercialize the End Product:** Sell subscriptions or access to the End Product **without royalty or revenue share**.
* **Use Across Environments:** Use the Software for development, staging, and production related to the same End Product.
* **Team Use:** Your employees and contractors may access the Software solely to develop and maintain your End Product.

### ❌ You MAY NOT
* **Resell or redistribute the source code:** You may not sell, sublicense, share, or redistribute the Software (in whole or in part) as a starter kit, boilerplate, template, framework, or otherwise publicly distribute it as open source.
* **Publish the source code publicly:** You may not publish the Software in any public repository or otherwise make the source code accessible for free.
* **Offer it as a competing template product:** You may not create or sell a product whose primary value is providing this kit (or a derivative) as a starter/boilerplate/template.

### 🔄 Updates & Support
* **Updates:** Lifetime access to updates and bug fixes for **major version 1 (1.x)**. Version 2.0+ may require a new license.
* **Support:** Includes basic support for installation and configuration issues. Custom development or consulting is **not** included.
* **Refunds:** Refunds are handled according to the policy of the platform where the Software was purchased.

### ⚖️ Disclaimer
The Software is provided “as is”, without warranty of any kind. The author(s) shall not be liable for any claim, damages, or other liability arising from the use of the Software.

### 🧾 Governing Law
These terms are governed by the laws of **Latvia**.

---

<div align="center">
  <h3>Ready to start building?</h3>
  <p>Get instant access to the private repository.</p>
  <a href="https://zukovlabs.lemonsqueezy.com/checkout/buy/0dbe2f00-c90f-414b-b2d1-4e589f487058">
    <img src="https://img.shields.io/badge/Buy%20License%20Now-Unlock%20Repo-success?style=for-the-badge&logo=github" height="50" />
  </a>
</div>
