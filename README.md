# 🚀 Job Portal Platform

A modern, AI-powered job portal built with Spring Boot, PostgreSQL, and React. Features secure authentication, role-based access control, and AI-driven resume parsing and candidate matching.

## ✨ Features

- **🔐 Secure Authentication** - JWT-based authentication with role-based access control
- **👥 User Management** - Support for USER, RECRUITER, and ADMIN roles
- **💼 Job Management** - Create, update, and manage job postings
- **📄 Resume Processing** - AI-powered resume parsing and analysis
- **🎯 Smart Matching** - AI-driven candidate-job matching algorithm
- **📱 Responsive Design** - Mobile-friendly interface with modern UI
- **🔒 Security** - BCrypt password hashing, CORS protection, secure endpoints

## 🛠️ Tech Stack

**Backend:**
- Java 21, Spring Boot 3.2+
- Spring Security, Spring Data JPA
- JWT Authentication, PostgreSQL
- Apache Tika (document parsing)
- Mock AI Service (pluggable for OpenAI)

**Frontend:**
- HTML5, Tailwind CSS, Vanilla JavaScript
- Responsive design, modern UI components
- Nginx (production deployment)

**Infrastructure:**
- Docker & Docker Compose
- Render (cloud deployment)
- PostgreSQL (persistent database)

## 🚀 Quick Start

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Spring-JobPortal
   ```

2. **Configure environment**
   ```bash
   cp .env.example .env
   # Edit .env with your database and email settings
   ```

3. **Database Migration (if upgrading from older version)**
   ```bash
   # Run the migration script to update users table structure
   psql -d your_database -f Backend/database_migration.sql
   ```

4. **Start with Docker**
   ```bash
   docker-compose up -d
   ```

5. **Access the application**
   - Frontend: http://localhost/
   - Backend API: http://localhost:8080/

### Production Deployment (Render)

#### Backend Deployment
1. Create a **Web Service** on Render
2. Connect your GitHub repository
3. Set **Root Directory** to `Backend`
4. Configure environment variables:
   ```
   SPRING_DATASOURCE_URL=<postgresql-url>
   JWT_SECRET=<your-secret-key>
   SPRING_MAIL_HOST=smtp.gmail.com
   SPRING_MAIL_USERNAME=<your-email>
   SPRING_MAIL_PASSWORD=<app-password>
   ```

#### Frontend Deployment
1. Create a **Static Site** on Render
2. Set **Root Directory** to `Frontend`
3. Update `Frontend/js/config.js` with your backend URL
4. Deploy and configure CORS in backend

## 🛡️ Role-Based Access

| Role | Permissions |
|------|-------------|
| **USER** | View jobs, upload resume, access AI matching |
| **RECRUITER** | Post jobs, access candidate matching |
| **ADMIN** | Full access: manage jobs, users, and system |

## 📊 Live Demo

- **Frontend:** [https://job-portal-ai-frontend.onrender.com](https://job-portal-ai-frontend.onrender.com)
- **Backend:** [https://job-portal-ai-fk12.onrender.com](https://job-portal-ai-fk12.onrender.com)

## 🗄️ Database Schema

### User Management
- **Primary Key:** Email address (unique identifier)
- **Username:** Unique display name
- **Password:** BCrypt hashed
- **Role:** USER, RECRUITER, or ADMIN

### Key Changes (v2.0+)
- Email is now the primary key instead of auto-increment ID
- Eliminates duplicate user issues
- Better data integrity and uniqueness
- Simplified user lookup and authentication

## 🔧 Configuration

### Environment Variables

```bash
# Database
SPRING_DATASOURCE_URL=postgresql://user:pass@host:port/db
SPRING_DATASOURCE_USERNAME=username
SPRING_DATASOURCE_PASSWORD=password

# Security
JWT_SECRET=your-jwt-secret-key

# Email (Gmail)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password

# AI (Optional)
OPENAI_API_KEY=your-openai-key  
```

### API Endpoints

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/api/users/register` | POST | User registration | No |
| `/api/users/login` | POST | User authentication | No |
| `/api/jobs/all` | GET | List all jobs | No |
| `/api/jobs/post` | POST | Create job posting | RECRUITER+ |
| `/api/resumes/upload` | POST | Upload resume | USER+ |
| `/api/resumes/jobs/{id}/candidates` | GET | Get candidates for job | RECRUITER+ |

## 📁 Project Structure

```
Spring-JobPortal/
├── Backend/                 # Spring Boot application
│   ├── src/main/java/
│   │   ├── controller/      # REST API endpoints
│   │   ├── service/         # Business logic
│   │   ├── model/          # JPA entities
│   │   ├── config/         # Security & configuration
│   │   └── dto/            # Data transfer objects
│   └── Dockerfile
├── Frontend/               # Static web application
│   ├── js/                # JavaScript modules
│   ├── *.html             # Web pages
│   └── Dockerfile
└── docker-compose.yml     # Local development setup
```

## 🔒 Security Features

- **JWT Authentication** - Stateless, secure token-based auth
- **BCrypt Password Hashing** - Industry-standard password security
- **Role-Based Access Control** - Granular permissions per endpoint
- **CORS Protection** - Secure cross-origin resource sharing
- **Input Validation** - Comprehensive request validation

## 🤖 AI Integration

The platform includes a mock AI service for:
- **Resume Parsing** - Extract skills, experience, education
- **Candidate Matching** - AI-driven job-candidate scoring
- **Pluggable Architecture** - Easy integration with OpenAI/Spring AI

## 📈 Performance & Scalability

- **Docker Containerization** - Consistent deployment across environments
- **PostgreSQL Database** - Robust, scalable data storage
- **Nginx Reverse Proxy** - Efficient static file serving
- **JWT Stateless Auth** - Scalable authentication without sessions

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection**
   - Verify PostgreSQL URL and credentials
   - Check network connectivity

2. **CORS Errors**
   - Ensure frontend URL is in allowed origins
   - Verify backend CORS configuration

3. **Authentication Issues**
   - Check JWT secret configuration
   - Verify user credentials in database

### Logs & Debugging

- Backend logs: Check Render service logs
- Frontend errors: Browser developer console
- Database issues: PostgreSQL connection logs

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review the deployment documentation

---

**Built with ❤️ using Spring Boot, PostgreSQL, and modern web technologies**
