# 🚀 Job Portal Platform – AI-Powered, Secure, Modern

A full-stack, containerized job portal leveraging Spring Boot, PostgreSQL, JWT, and AI-driven resume parsing/matching. Built for scalability, security, and extensibility.

---

## 🏗️ Architecture
- **Backend:** Java 21, Spring Boot 3.2+, Spring Security, Spring Data JPA, JWT, Apache Tika, Mock/Pluggable AI Service
- **Frontend:** HTML5, Tailwind CSS, Vanilla JavaScript, Nginx (Dockerized)
- **Database:** PostgreSQL (Dockerized, persistent volume)
- **Containerization:** Docker, Docker Compose, multi-stage builds

---

## ✨ Core Features
- **User Management:**
  - JWT-based authentication (stateless, secure)
  - Role-based access: USER, ADMIN, RECRUITER
  - BCrypt password hashing
  - Email notifications (JavaMailSender, SMTP)
- **Job Management:**
  - CRUD for job posts (role-restricted)
  - Public job listings
- **Resume & AI:**
  - PDF/DOCX upload, Apache Tika text extraction
  - AI-powered parsing (skills, experience, education, summary)
  - Candidate-job matching with AI-driven scoring/ranking
- **UI/UX:**
  - Responsive, mobile-friendly (Tailwind CSS)
  - Role-based UI controls (admin, recruiter, user)
  - Toast notifications, loading spinners
- **Security:**
  - CORS, CSRF disabled (API)
  - JWT validation, role checks, secure endpoints
- **DevOps:**
  - Docker Compose orchestration
  - Healthchecks, persistent DB volume
  - Environment variable config (.env)

---

## 🛠️ Tech Stack
- **Backend:** Java 21, Spring Boot, Spring Security, Spring Data JPA, JWT, Apache Tika, JavaMailSender
- **Frontend:** HTML5, Tailwind CSS, Vanilla JS, Nginx
- **Database:** PostgreSQL
- **AI:** Mock AI (pluggable for OpenAI/Spring AI)
- **Tools:** Maven, Docker, Docker Compose

---

## 🛡️ Roles & Access Control
- **USER**
  - View and search job listings
  - Upload resume and view AI summary
  - Cannot post, update, or delete jobs
- **RECRUITER**
  - Post new jobs
  - Access AI Matching to find candidates for jobs
  - Cannot update or delete jobs
- **ADMIN**
  - Full access: post, update, delete jobs
  - Access AI Matching and all admin features
  - Manage all users and data

---

## ⚡ Quick Start
1. `cp .env.example .env` – configure DB, mail, JWT, AI keys
2. `docker-compose build && docker-compose up -d`
3. Access:
   - Frontend: http://localhost/
   - Backend API: http://localhost:8080/
   - DB: localhost:5432 (service: db)

---

## 📦 Key Modules
- `Backend/controller/` – REST APIs
- `Backend/model/` – JPA entities (User, Job, Resume, Match)
- `Backend/service/` – Business logic, AI integration
- `Backend/config/` – Security, JWT, CORS, AI
- `Frontend/js/` – Modular JS (auth, jobs, utils, AI)

---

## 🔒 Security
- JWT stateless auth, role-based endpoint protection
- BCrypt password storage
- CORS for frontend-backend integration

---

## 🤖 AI Integration
- Mock AI service (default, no API key required)
- Pluggable for OpenAI/Spring AI (see AI_INTEGRATION_GUIDE.md)
- Resume parsing, skill extraction, candidate-job match scoring

---

## 📝 Deployment & Customization
- All services run in Docker containers
- Configurable via `.env` (DB, mail, JWT, AI)
- Easily extensible for new roles, features, or real AI

---

## 📚 Docs & References
- See `README.md`, `AI_INTEGRATION_GUIDE.md`, `IMPLEMENTATION_SUMMARY.md`, `QUICK_START_GUIDE.md` for full details
- Spring Boot, Spring Security, PostgreSQL, Docker, Tailwind CSS, OpenAI API

---