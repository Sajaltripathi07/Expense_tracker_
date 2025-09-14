# 💰 Expense Tracker

A modern, full-stack expense tracking application built with Spring Boot 3.5.3, MySQL, Redis caching, and Docker. Features a clean web UI with Thymeleaf and REST APIs with JWT authentication.

## 🎯 Features

- **🔐 Authentication**: JWT-based API authentication + session-based web UI
- **👤 User Management**: Role-based access control (USER/ADMIN)
- **💰 Expense Management**: Full CRUD operations with auto-categorization
- **📊 Dashboard**: Real-time metrics and category summaries
- **⚡ Performance**: Redis caching for frequently accessed data
- **🐳 Containerized**: Complete Docker setup with health checks
- **📱 Responsive UI**: Clean, modern interface with Thymeleaf

## 🏗️ Technology Stack

- **Backend**: Spring Boot 3.5.3, Spring Security, Spring Data JPA
- **Database**: MySQL 8.0
- **Cache**: Redis 7
- **UI**: Thymeleaf, HTML5, CSS3, JavaScript
- **Build**: Maven
- **Container**: Docker, Docker Compose

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose
- Java 17+ (for local development)

### Running with Docker (Recommended)

1. **Prerequisites:**
   - Docker Desktop installed and running
   - No conflicting services on ports 3308, 6379, or 8080

2. **Clone and navigate to the project:**
   ```bash
   cd ExpenseTrackerend
   ```

3. **Configure environment variables:**
   ```bash
   # Copy the example environment file
   copy env.example .env
   
   # The .env file contains all configuration values
   # Default values are already configured for local development
   ```

4. **Start the application stack:**
   ```bash
   # Build and start all services
   docker-compose up --build
   
   # Or run in background (detached mode)
   docker-compose up --build -d
   ```

5. **Access the application:**
   - **Web UI**: http://localhost:8080
   - **Health Check**: http://localhost:8080/actuator/health
   - **MySQL Database**: localhost:3308 (external access)
   - **Redis Cache**: localhost:6379

The Docker setup includes:
- **MySQL Database** (port 3308 externally, 3306 internally)
- **Redis Cache** (port 6379)  
- **Expense Tracker App** (port 8080)

### Local Development

1. **Start MySQL and Redis:**
   ```bash
   docker-compose up mysql redis -d
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

## 🔌 API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login (returns JWT)
- `GET /api/auth/me` - Get current user info

### Expenses
- `GET /api/expenses` - List user's expenses
- `POST /api/expenses` - Create new expense
- `GET /api/expenses/{id}` - Get specific expense
- `PUT /api/expenses/{id}` - Update expense
- `DELETE /api/expenses/{id}` - Delete expense

### Dashboard
- `GET /api/dashboard/summary` - Get dashboard metrics

**Note**: API endpoints require JWT token in `Authorization: Bearer <token>` header.

## ⚡ Caching

The application uses Redis for caching frequently accessed data:
- User expenses
- Dashboard totals and summaries
- Category breakdowns

Cache is automatically invalidated when data changes.

## 🔐 Security

- **Password Hashing**: BCrypt with salt
- **JWT Tokens**: Stateless authentication for APIs
- **Session Management**: Traditional sessions for web UI
- **CORS**: Configured for API access
- **Input Validation**: Server-side validation on all inputs

## 🗂️ Project Structure

```
src/main/java/com/example/expensetracker/
├── config/          # Security, JWT, Redis configuration
├── controller/      # Web and API controllers
├── model/          # JPA entities
├── repository/     # Data access layer
├── service/        # Business logic with caching
└── utils/          # Utility classes

src/main/resources/
├── templates/      # Thymeleaf templates
├── static/         # CSS, images, JS
└── application.properties
```

## 🐳 Docker Configuration

- **Multi-stage build** for optimized image size
- **Non-root user** for security
- **Health checks** for all services
- **Volume persistence** for data
- **Environment-based configuration**

### Configuration

The application is pre-configured with sensible defaults:
- **Database**: `expense_tracker` with root user
- **Ports**: MySQL (3308 externally), Redis (6379), App (8080)
- **JWT**: Pre-configured secret for development
- **Environment**: Uses `.env` file for configuration

**Security Note**: Change the default passwords and JWT secret for production use!

#### **Port Configuration:**
- **MySQL**: Port 3308 (external) → 3306 (internal Docker)
- **Redis**: Port 6379
- **Application**: Port 8080

#### **Database Access:**
- **From Application**: `mysql:3306` (internal Docker network)
- **From Host Machine**: `localhost:3308` (external access)
- **Credentials**: root / `12@rusellll`

## 📊 Database Schema

- **Users**: Authentication and user management
- **Financial Accounts**: User's financial accounts
- **Expenses**: Individual expense records
- **Roles**: User role management

## 🚨 Troubleshooting

### Common Issues and Solutions

#### **Issue: "Docker Desktop is not running"**
- **Solution**: Start Docker Desktop and wait for it to fully load
- **Check**: Run `docker --version` to verify Docker is running

#### **Issue: "Port already in use"**
- **Solution**: Stop other applications using ports 3308, 6379, or 8080
- **Check**: Run `netstat -ano | findstr :3308` to see what's using the port

#### **Issue: "Build failed - Maven wrapper not found"**
- **Solution**: The Dockerfile has been updated to use system Maven instead of Maven wrapper
- **Check**: Ensure all source files are present in the project

#### **Issue: "MySQL connection failed"**
- **Solution**: Wait for MySQL to fully initialize (check health checks)
- **Check**: Look for "Ready to accept connections" in MySQL logs

#### **Issue: "Found orphan containers"**
- **Solution**: Run `docker-compose down --remove-orphans` to clean up old containers

### Useful Commands

```bash
# Check running containers
docker ps

# View logs
docker-compose logs
docker-compose logs expense-tracker
docker-compose logs mysql
docker-compose logs redis

# Stop all services
docker-compose down

# Clean up everything (including volumes)
docker-compose down -v

# Restart services
docker-compose restart
```

## 🚀 Deployment

The application is ready for deployment with:
- Environment variable configuration
- Health check endpoints
- Production-ready Docker setup
- Database migration support

## 📄 License

This project is for educational and demonstration purposes.