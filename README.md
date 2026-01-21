# Leads Management System – Full Stack Application

## Overview

This project is a **full-stack Leads Management System** built as part of a technical assessment.

It demonstrates:

- RESTful API design
- JWT-based authentication
- CRUD operations
- Dockerized services
- Frontend–backend integration

The system allows authenticated users to **create, view, update, delete, and filter sales leads** through a simple web interface.

---

## Tech Stack

### Backend (PHP)

- PHP 8.2
- Slim Framework
- JWT Authentication
- PostgreSQL
- PDO
- Nginx
- Docker

### Frontend

- Vue 3
- Vite
- Axios
- Plain CSS

### Infrastructure

- Docker
- Docker Compose

### Project Structure

```bash
.
├── docker-compose.yml
├── php-backend/
│ ├── Dockerfile
│ ├── public/
│ ├── src/
│ ├── docker/
│ │ └── nginx.conf
│ └── docker/postgres/init.sql
├── frontend/
│ ├── Dockerfile
│ ├── src/
│ ├── index.html
│ └── package.json
└── README.md
```

---

## Running the Project

### Prerequisites

- Docker
- Docker Compose

#### Start the full stack

**From the project root:**

docker compose down -v
docker compose up --build

**This will start:**

- PostgreSQL
- PHP backend (Slim + Nginx)
- Vue frontend

Access the Application
Service URL
Frontend http://localhost:5173

PHP Backend API http://localhost:8081
Default Login Credentials
Email: admin@example.com
Password: password

---

## API Endpoints

### Authentication

**Method Endpoint Description**

- POST /api/auth/login Login and receive JWT

### Leads (JWT Protected)

**Method Endpoint Description**

- POST /api/leads Create a lead
- GET /api/leads List leads (supports date filtering)
- GET /api/leads/{id} Get lead by ID
- PUT /api/leads/{id} Update lead
- DELETE /api/leads/{id} Delete lead

**Date filtering example**

- GET /api/leads?from=2026-01-01&to=2026-01-31

---

## Frontend Features

- Login with JWT authentication
- Lead creation
- Lead listing displayed in a table
- Date range filtering
- Inline update/edit of leads
- Delete leads
- Loading indicator for async actions

---

## Database

### PostgreSQL

Schema initialized via Docker using init.sql

**Tables:**

- users
- leads

The database schema is aligned with the backend API and supports date-based filtering.

---

**Notes**

- The repository may include a Java backend implementation for reference, but the PHP backend is the active backend used in the Docker Compose setup.
- All services are containerized and can be started with a single Docker Compose command.
- Environment variables are managed via Docker and .env files (excluded from version control).

---

### Author

**Christian van Wyk**

---

**Final Notes**

- Each service is independently containerized
- Clean separation of concerns between frontend and backend
- JWT authentication and middleware used to protect API routes
- Designed to be simple, readable, and easy to run for assessors
