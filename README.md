# 📚 Book Management System

A full-stack Book Management System built with **Spring Boot (Java 21)**, **React + TypeScript (Vite)**, and **PostgreSQL**. It provides complete CRUD operations on book records, including book cover image upload/retrieval via multipart file handling.

---

## Tech Stack

**Frontend:** React.js, TypeScript, Vite, Axios, HTML5, CSS3
**Backend:** Java 21, Spring Boot 3, Spring Web, Spring Data JPA, Hibernate, Lombok, Validation API
**Database:** PostgreSQL
**Tools:** Maven, Git, Docker Compose, Postman

---

## Project Structure

```
book-management-system/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/com/bookapi/bookmanagement/
│   │   ├── controller/                # REST controllers (Book, Image)
│   │   ├── service/ & service/impl/   # Business logic layer
│   │   ├── repository/                # Spring Data JPA repositories
│   │   ├── entity/                    # JPA entities
│   │   ├── dto/                       # Request/response DTOs
│   │   ├── exception/                 # Custom exceptions + global handler
│   │   └── config/                    # CORS & static resource config
│   ├── src/main/resources/application.properties
│   ├── uploads/book-covers/           # Stored cover images (filesystem)
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/                          # React + TypeScript SPA
│   ├── src/
│   │   ├── components/                # Navbar, BookCard, BookForm, Alert, Loader, ConfirmDialog
│   │   ├── pages/                     # BookListPage, AddBookPage, EditBookPage, SearchByIsbnPage
│   │   ├── services/                  # apiClient, bookService, errorHandler
│   │   ├── types/                     # TypeScript interfaces
│   │   └── styles/App.css             # Plain CSS3 styling
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── docker-compose.yml                 # Postgres + backend + frontend orchestration
└── Book-Management-System.postman_collection.json
```

---

## Key Features

- ✅ Add new books with cover image upload
- ✅ View all books in a responsive grid
- ✅ Search books by ISBN
- ✅ Update existing book details (with optional image replacement)
- ✅ Delete books (with confirmation dialog)
- ✅ Upload & retrieve book cover images
- ✅ Layered backend architecture: Controller → Service → Repository → Entity, with DTOs at the boundary
- ✅ Server-side validation (Jakarta Validation) with field-level error messages
- ✅ Centralized exception handling (`@RestControllerAdvice`)
- ✅ CORS configured for the React dev server
- ✅ Docker Compose for one-command local setup

---

## Backend API Reference

Base URL: `http://localhost:8080`

| Method | Endpoint                  | Description                              | Body / Notes |
|--------|----------------------------|-------------------------------------------|---------------|
| POST   | `/api/books`               | Create a new book                         | `multipart/form-data`: `book` (JSON string), `image` (optional file) |
| GET    | `/api/books`                | Retrieve all books                        | — |
| GET    | `/api/books/{id}`           | Retrieve a book by numeric ID              | — |
| GET    | `/api/books/isbn/{isbn}`    | Retrieve a book by ISBN                    | — |
| PUT    | `/api/books/{id}`           | Update an existing book                    | `multipart/form-data`: `book` (JSON string), `image` (optional file) |
| DELETE | `/api/books/{id}`           | Delete a book                              | — |
| POST   | `/api/books/{id}/image`     | Upload/replace a book's cover image only   | `multipart/form-data`: `image` |
| GET    | `/api/images/{filename}`    | Fetch a stored cover image directly        | Also served statically at `/uploads/book-covers/{filename}` |

### Example: `book` JSON part

```json
{
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "isbn": "9780134685991",
  "publisher": "Addison-Wesley",
  "genre": "Programming",
  "price": 45.99,
  "quantity": 10,
  "description": "Best practices for the Java platform."
}
```

### Example: success response envelope

```json
{
  "success": true,
  "message": "Book created successfully",
  "data": {
    "id": 1,
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "isbn": "9780134685991",
    "publisher": "Addison-Wesley",
    "genre": "Programming",
    "price": 45.99,
    "quantity": 10,
    "description": "Best practices for the Java platform.",
    "imageUrl": "http://localhost:8080/uploads/book-covers/3f1c...-cover.jpg",
    "createdAt": "2026-06-28T10:15:30",
    "updatedAt": "2026-06-28T10:15:30"
  },
  "timestamp": "2026-06-28T10:15:30.123"
}
```

### Example: validation error response (HTTP 400)

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "title": "Title is required",
    "price": "Price must be zero or positive"
  },
  "timestamp": "2026-06-28T10:16:02.456"
}
```

A ready-to-import **Postman collection** is included at the project root: `Book-Management-System.postman_collection.json`.

---

## Running Locally (without Docker)

### Prerequisites
- Java 21+
- Maven 3.9+
- Node.js 18+
- PostgreSQL 14+ running locally

### 1. Database
Create the database (the app will auto-create tables via `ddl-auto=update`):
```sql
CREATE DATABASE bookdb;
```

### 2. Backend
```bash
cd backend
# Optionally override DB credentials via env vars, e.g.:
# export DB_USERNAME=postgres DB_PASSWORD=postgres
mvn spring-boot:run
```
The API starts on **http://localhost:8080**.

### 3. Frontend
```bash
cd frontend
cp .env.example .env   # adjust VITE_API_BASE_URL if needed
npm install
npm run dev
```
The app starts on **http://localhost:5173**.

---

## Running with Docker Compose

From the project root:

```bash
docker compose up --build
```

This spins up:
- **PostgreSQL** on port `5432`
- **Spring Boot backend** on port `8080`
- **React frontend (served via nginx)** on port `5173`

Uploaded cover images persist in a named Docker volume (`book-covers`) so they survive container restarts.

To stop:
```bash
docker compose down
```

To stop **and** wipe the database/image volumes:
```bash
docker compose down -v
```

---

## Configuration Reference

### Backend (`application.properties`, overridable via env vars)

| Property | Env Var | Default |
|---|---|---|
| Datasource URL | `DB_HOST`, `DB_PORT`, `DB_NAME` | `localhost:5432/bookdb` |
| Datasource username | `DB_USERNAME` | `postgres` |
| Datasource password | `DB_PASSWORD` | `postgres` |
| Upload directory | `UPLOAD_DIR` | `uploads/book-covers` |
| Public base URL for images | `APP_BASE_URL` | `http://localhost:8080` |
| Allowed CORS origin(s) | `CORS_ALLOWED_ORIGINS` | `http://localhost:5173` |

### Frontend (`.env`)

| Variable | Default |
|---|---|
| `VITE_API_BASE_URL` | `http://localhost:8080` |

---

## Notes on Image Storage

Cover images are stored **on the backend's filesystem** (under `app.upload.dir`), with only the generated filename persisted on the `Book` entity (`image_path` column). The service layer builds a full public URL (`imageUrl`) on every response so the frontend never needs to know the storage details — only the returned URL.

Allowed image types: JPEG, PNG, WEBP. Max upload size: 5MB (configurable via `spring.servlet.multipart.max-file-size`).

---

## License

This project was generated as a learning/portfolio reference implementation. Feel free to use and adapt it.
