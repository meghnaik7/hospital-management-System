# Hospital Management System

A beginner-friendly **REST API** backend built with **Spring Boot**, featuring **JWT-based authentication** and **role-based access control (RBAC)**.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| Database | H2 In-Memory |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |
| Java | Java 17+ |

---

## Features

- JWT authentication (register / login)
- Role-based access control — **ADMIN**, **DOCTOR**, **PATIENT**
- Full CRUD for Doctors, Patients, Appointments
- Appointment status management (SCHEDULED → COMPLETED / CANCELLED)
- Global exception handling with consistent JSON error responses
- H2 console for easy database inspection (dev)

---

## Project Structure

```
src/main/java/com/hospital/
├── controller/
│   ├── AuthController.java         # POST /api/auth/register, /login
│   ├── DoctorController.java       # CRUD /api/doctors
│   ├── PatientController.java      # CRUD /api/patients
│   ├── AppointmentController.java  # CRUD /api/appointments
│   └── AdminController.java        # GET/DELETE /api/admin/users
├── dto/                            # Request / Response DTOs
├── entity/
│   ├── User.java
│   ├── Doctor.java
│   ├── Patient.java
│   ├── Appointment.java
│   └── Role.java (ADMIN, DOCTOR, PATIENT)
├── exception/
│   └── GlobalExceptionHandler.java
├── repository/                     # Spring Data JPA repositories
├── security/
│   ├── JwtService.java             # Token generation & validation
│   ├── JwtAuthFilter.java          # Request filter
│   └── SecurityConfig.java         # Security rules
└── service/                        # Business logic
```

---

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Run the application

```bash
git clone https://github.com/<your-username>/hospital-management.git
cd hospital-management
mvn spring-boot:run
```

The server starts on **http://localhost:8080**

---

## API Reference

### Auth (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT |

**Register request body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123",
  "role": "PATIENT"
}
```
> `role` must be one of: `ADMIN`, `DOCTOR`, `PATIENT`

**Login request body:**
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

**Auth response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "john@example.com",
  "role": "PATIENT"
}
```

> For all protected endpoints, add this header:
> ```
> Authorization: Bearer <token>
> ```

---

### Doctors

| Method | Endpoint | Roles Allowed |
|--------|----------|---------------|
| GET | `/api/doctors` | Any authenticated |
| GET | `/api/doctors/{id}` | Any authenticated |
| POST | `/api/doctors` | ADMIN, DOCTOR |
| PUT | `/api/doctors/{id}` | ADMIN, DOCTOR |
| DELETE | `/api/doctors/{id}` | ADMIN |

**Create/Update doctor body:**
```json
{
  "specialization": "Cardiology",
  "phone": "9876543210",
  "experienceYears": 10,
  "userId": 2
}
```

---

### Patients

| Method | Endpoint | Roles Allowed |
|--------|----------|---------------|
| GET | `/api/patients` | ADMIN, DOCTOR |
| GET | `/api/patients/{id}` | Any authenticated |
| POST | `/api/patients` | ADMIN, PATIENT |
| PUT | `/api/patients/{id}` | ADMIN, PATIENT |
| DELETE | `/api/patients/{id}` | ADMIN |

**Create/Update patient body:**
```json
{
  "phone": "1234567890",
  "age": 30,
  "address": "123 Main St",
  "medicalHistory": "None",
  "userId": 3
}
```

---

### Appointments

| Method | Endpoint | Roles Allowed |
|--------|----------|---------------|
| GET | `/api/appointments` | ADMIN, DOCTOR |
| GET | `/api/appointments/{id}` | Any authenticated |
| GET | `/api/appointments/patient/{patientId}` | Any authenticated |
| GET | `/api/appointments/doctor/{doctorId}` | ADMIN, DOCTOR |
| POST | `/api/appointments` | ADMIN, PATIENT |
| PATCH | `/api/appointments/{id}/status?status=COMPLETED` | ADMIN, DOCTOR |
| DELETE | `/api/appointments/{id}` | ADMIN |

**Book appointment body:**
```json
{
  "appointmentDate": "2026-12-01T10:00:00",
  "reason": "Chest pain checkup",
  "patientId": 1,
  "doctorId": 1
}
```

**Appointment status values:** `SCHEDULED` | `COMPLETED` | `CANCELLED`

---

### Admin

| Method | Endpoint | Roles Allowed |
|--------|----------|---------------|
| GET | `/api/admin/users` | ADMIN |
| DELETE | `/api/admin/users/{id}` | ADMIN |

---

## Role Permissions Summary

| Endpoint | ADMIN | DOCTOR | PATIENT |
|----------|-------|--------|---------|
| Register / Login | ✅ | ✅ | ✅ |
| View doctors | ✅ | ✅ | ✅ |
| Manage doctors | ✅ | ✅ own | ❌ |
| Delete doctor | ✅ | ❌ | ❌ |
| View all patients | ✅ | ✅ | ❌ |
| Manage patients | ✅ | ❌ | ✅ own |
| View all appointments | ✅ | ✅ | ❌ |
| Book appointment | ✅ | ❌ | ✅ |
| Update appt status | ✅ | ✅ | ❌ |
| Admin panel | ✅ | ❌ | ❌ |

---

## H2 Database Console

Access the in-memory database at:

```
http://localhost:8080/h2-console
```

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:hospitaldb` |
| Username | `sa` |
| Password | *(leave blank)* |

> Note: Data resets every time the application restarts (in-memory DB).

---

## Sample Workflow

```
1. POST /api/auth/register  → create ADMIN account
2. POST /api/auth/register  → create DOCTOR account
3. POST /api/auth/register  → create PATIENT account
4. POST /api/auth/login     → get JWT token
5. POST /api/doctors        → create doctor profile (use DOCTOR token)
6. POST /api/patients       → create patient profile (use PATIENT token)
7. POST /api/appointments   → book appointment (use PATIENT token)
8. PATCH /api/appointments/1/status?status=COMPLETED  → mark done (use DOCTOR token)
```

---

## License

This project is open source and available under the [MIT License](LICENSE).
