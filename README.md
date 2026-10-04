# 🔐 JWT Spring Boot Security Demo

Spring Boot REST API secured with stateless JWT authentication and role-based authorization, backed by MySQL.

## 🛠️ Tech Stack

- ☕ Java 21
- 🍃 Spring Boot 3.5.7 (Web, Data JPA, Security)
- 🔑 JJWT 0.11.5
- 🐬 MySQL
- ✨ Lombok
- 📦 Maven

## ✨ Features

- 📝 User registration and login
- 🎫 JWT issued on login, validated on every request via a security filter
- 🚫 Stateless sessions (no server-side session storage)
- 👮 Role-based access control on endpoints
- 🔒 Password hashing with BCrypt
- 💾 JPA persistence with MySQL

## 🔄 Authentication Flow

1. 👤 Client sends credentials to the login endpoint.
2. ✅ Server validates them and returns a signed JWT.
3. 📨 Client sends the token on subsequent requests:
   `Authorization: Bearer <token>`
4. 🛡️ A JWT filter validates the token, loads the user, and sets the security context.
5. 🚦 Access is granted or denied based on the user's role.

## 🚀 Getting Started

### 📋 Prerequisites

- JDK 21
- MySQL 8+
- Maven (or use the included `mvnw`)

### ⚙️ Setup

1. 📥 Clone the repo:
   ```bash
   git clone https://github.com/MuraliNexus/JWT-spring-boot-security-demo.git
   cd JWT-spring-boot-security-demo
   ```

2. 🗄️ Create the database:
   ```sql
   CREATE DATABASE jwt_demo;
   ```

3. 🔧 Configure `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/jwt_demo
   spring.datasource.username=<your_username>
   spring.datasource.password=<your_password>
   spring.jpa.hibernate.ddl-auto=update

   jwt.secret=<long-random-base64-secret>
   jwt.expiration=3600000
   ```
   ⚠️ Don't commit real credentials or a real secret. Use environment variables or a git-ignored profile.

4. ▶️ Run:
   ```bash
   ./mvnw spring-boot:run
   ```
   App starts on `http://localhost:8080`.

## 📡 API Endpoints

> Adjust paths to match your controllers.

| Method | Endpoint             | Access      | Description          |
| ------ | -------------------- | ----------- | -------------------- |
| POST   | `/api/auth/register` | 🌍 Public   | Register a new user  |
| POST   | `/api/auth/login`    | 🌍 Public   | Login, returns JWT   |
| GET    | `/api/user/**`       | 👤 USER, ADMIN | User-level resources |
| GET    | `/api/admin/**`      | 👑 ADMIN    | Admin-only resources |

### 💡 Example

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"secret"}'

# Call a protected endpoint
curl http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer <token>"
```

## 📁 Project Structure

```
src/main/java/com/example/...
├── config/       # Security configuration
├── controller/   # REST controllers
├── entity/       # JPA entities
├── repository/   # Spring Data repositories
├── security/     # JWT util and filter
└── service/      # Business logic
```

## 🧪 Testing

```bash
./mvnw test
```


