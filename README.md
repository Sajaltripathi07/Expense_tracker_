# 💰 Expense Tracker - Full Stack Web Application

A modern, full-stack expense tracking application built with Spring Boot 3.5.3, featuring beautiful glassmorphism UI, secure user authentication, comprehensive expense management, and role-based access control.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)
![Java](https://img.shields.io/badge/Java-17-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.0-purple)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.0-red)

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Database Schema](#-database-schema)
- [Installation & Setup](#-installation--setup)
- [API Documentation](#-api-documentation)
- [Screenshots](#-screenshots)
- [Deployment](#-deployment)
- [Contributing](#-contributing)

## 🎯 Features

### ✨ Core Features
- **🔐 Secure Authentication** - BCrypt password encryption, session management
- **👥 User Management** - Registration, login, profile management
- **💰 Expense Management** - Add, edit, delete expenses with validation
- **📊 Dashboard Analytics** - Real-time statistics and visualizations
- **🎭 Role-based Access** - USER and ADMIN roles with different permissions
- **📱 Responsive Design** - Mobile-first approach with glassmorphism UI
- **🔄 Real-time Updates** - Instant data changes and live statistics

### 🎨 UI/UX Features
- **Glassmorphism Design** - Modern glass-like effects with backdrop blur
- **Interactive Cards** - Hover animations and smooth transitions
- **Smart Date Selection** - Today/Yesterday/Custom date options
- **Category Icons** - Visual category selection with Font Awesome icons
- **Form Validation** - Client and server-side validation
- **Flash Messages** - Success/error notifications
- **Loading States** - Smooth loading animations

### 🔧 Technical Features
- **Spring Security** - Authentication, authorization, CSRF protection
- **Spring Data JPA** - Database operations with MySQL
- **Thymeleaf Templates** - Server-side rendering with dynamic content
- **RESTful APIs** - JSON endpoints for mobile integration
- **Auto-categorization** - Smart expense categorization based on description
- **Financial Account Management** - Automatic account creation and balance tracking

## 🏗️ Technology Stack

### Backend
- **Spring Boot 3.5.3** - Main application framework
- **Spring Security 6.0** - Authentication and authorization
- **Spring Data JPA** - Database operations and ORM
- **MySQL 8.0** - Primary database
- **H2 Database** - In-memory database for testing
- **Maven** - Build tool and dependency management
- **BCrypt** - Password encryption

### Frontend
- **Thymeleaf 3.0** - Server-side template engine
- **HTML5/CSS3** - Structure and styling
- **JavaScript** - Interactive features and form handling
- **Font Awesome 6.0** - Icons and visual elements
- **Responsive Design** - Mobile-first CSS framework

### Development Tools
- **Spring Boot DevTools** - Hot reload and development utilities
- **Spring Boot Test** - Unit and integration testing
- **Spring Security Test** - Security testing utilities

## 🏛️ Architecture

### Project Structure
```
src/main/java/com/example/expensetracker/
├── ExpenseTrackerApplication.java          # Main application class
├── config/
│   └── SecurityConfig.java               # Spring Security configuration
├── controller/
│   ├── AuthController.java               # Authentication endpoints
│   ├── ExpenseController.java            # Expense management endpoints
│   └── AdminController.java              # Admin-specific endpoints
├── model/
│   ├── User.java                        # User entity
│   ├── Expense.java                     # Expense entity
│   ├── FinancialAccount.java            # Account entity
│   └── Role.java                        # Role enum (USER/ADMIN)
├── repository/
│   ├── UserRepository.java              # User data access
│   ├── ExpenseRepository.java           # Expense data access
│   └── FinancialAccountRepository.java  # Account data access
├── service/
│   ├── ExpenseService.java              # Business logic for expenses
│   └── CustomUserDetailsService.java    # Spring Security user details
└── utils/
    └── NumberFormatter.java             # Utility for number formatting
```

### Key Components

#### 1. **ExpenseTrackerApplication.java**
- Main Spring Boot application class
- Auto-configuration and component scanning
- Application entry point

#### 2. **SecurityConfig.java**
- **CSRF Protection** - Disabled for API endpoints
- **URL Authorization** - Role-based access control
- **Form Login** - Custom login page and processing
- **Password Encoding** - BCrypt password hashing
- **Session Management** - Secure user sessions

#### 3. **Controllers**
- **AuthController** - User registration, login, logout
- **ExpenseController** - CRUD operations for expenses
- **AdminController** - User management and admin features

#### 4. **Models**
- **User** - User entity with role-based access
- **Expense** - Expense entity with categorization
- **FinancialAccount** - Account management with balance tracking
- **Role** - Enum for USER/ADMIN roles

#### 5. **Services**
- **ExpenseService** - Business logic, auto-categorization, balance updates
- **CustomUserDetailsService** - Spring Security integration

## 🗄️ Database Schema

### User Table
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER'
);
```

### FinancialAccount Table
```sql
CREATE TABLE financial_account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0.00,
    budget_limit DECIMAL(10,2) DEFAULT 10000.00,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### Expense Table
```sql
CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    category VARCHAR(50) NOT NULL,
    account_id BIGINT,
    FOREIGN KEY (account_id) REFERENCES financial_account(id)
);
```

## 🚀 Installation & Setup

### Prerequisites
- **Java 17** or higher
- **MySQL 8.0** or higher
- **Maven 3.6** or higher

### Step-by-Step Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/expense-tracker.git
   cd expense-tracker
   ```

2. **Create Database**
   ```sql
   CREATE DATABASE expense_tracker;
   ```

3. **Configure Application Properties**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build the Application**
   ```bash
   ./mvnw clean package
   ```

5. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Access the Application**
   Open [http://localhost:8080](http://localhost:8080)

## 📚 API Documentation

### Authentication Endpoints

#### POST `/signup`
- **Purpose**: User registration
- **Parameters**: username, email, password, confirmPassword
- **Response**: Redirect with success/error message

#### POST `/login`
- **Purpose**: User authentication (handled by Spring Security)
- **Parameters**: username, password
- **Response**: Redirect to dashboard on success

#### GET `/logout`
- **Purpose**: User logout
- **Response**: Redirect to auth page

### Expense Management Endpoints

#### GET `/dashboard`
- **Purpose**: Main dashboard with user-specific data
- **Response**: HTML page with expense statistics

#### GET `/addExpense`
- **Purpose**: Show add expense form
- **Response**: HTML form page

#### POST `/saveExpense`
- **Purpose**: Save new expense
- **Parameters**: description, amount, date, category
- **Response**: Redirect with success/error message

#### GET `/editExpense/{id}`
- **Purpose**: Show edit expense form
- **Response**: HTML form page

#### POST `/updateExpense`
- **Purpose**: Update existing expense
- **Parameters**: id, description, amount, date, category
- **Response**: Redirect with success/error message

#### GET `/deleteExpense/{id}`
- **Purpose**: Delete expense
- **Response**: Redirect with success/error message

### Admin Endpoints (Admin Only)

#### GET `/admin/dashboard`
- **Purpose**: Admin dashboard with system-wide statistics
- **Response**: HTML page with admin overview

#### GET `/admin/users`
- **Purpose**: User management page
- **Response**: HTML page with user list

#### POST `/admin/users/{id}/delete`
- **Purpose**: Delete user (admin only)
- **Response**: Redirect with success/error message

#### POST `/admin/users/{id}/toggle-role`
- **Purpose**: Change user role (admin only)
- **Response**: Redirect with success/error message

### REST API Endpoints

#### POST `/api/signup`
- **Purpose**: JSON-based user registration
- **Request Body**: JSON with username, email, password, confirmPassword
- **Response**: JSON success/error message

#### POST `/api/login`
- **Purpose**: JSON-based authentication
- **Request Body**: JSON with username, password
- **Response**: JSON success/error message

#### POST `/api/addExpense`
- **Purpose**: JSON-based expense creation
- **Request Body**: JSON expense object
- **Response**: JSON success/error message

#### GET `/api/expense/{id}`
- **Purpose**: Get expense by ID
- **Response**: JSON expense object

#### PUT `/api/expense/{id}`
- **Purpose**: Update expense by ID
- **Request Body**: JSON expense object
- **Response**: JSON success/error message

#### DELETE `/api/expense/{id}`
- **Purpose**: Delete expense by ID
- **Response**: JSON success/error message

## 📱 Screenshots

### Dashboard
![Dashboard](https://via.placeholder.com/1200x600/667eea/ffffff?text=Dashboard+Screenshot)
*Beautiful dashboard with statistics cards, recent expenses, and activity timeline*

### Add Expense Form
![Add Expense](https://via.placeholder.com/1200x600/764ba2/ffffff?text=Add+Expense+Form)
*Modern add expense form with smart date selection and category icons*

### Login/Auth Page
![Login](https://via.placeholder.com/1200x600/28a745/ffffff?text=Login+Page)
*Secure authentication page with toggle between login and signup*

### Admin Dashboard
![Admin Dashboard](https://via.placeholder.com/1200x600/dc3545/ffffff?text=Admin+Dashboard)
*Admin dashboard with user management and system statistics*

### Mobile Responsive
![Mobile](https://via.placeholder.com/600x800/ffc107/ffffff?text=Mobile+View)
*Responsive design that works perfectly on mobile devices*

## 🚀 Deployment

### Quick Deploy Options

#### Railway (Recommended)
1. **Sign up** at [railway.app](https://railway.app)
2. **Connect GitHub** repository
3. **Create new project** → Deploy from GitHub
4. **Add MySQL database** in Railway dashboard
5. **Set environment variables**:
   - `DATABASE_URL` = Your MySQL connection string
   - `DB_USERNAME` = Your database username
   - `DB_PASSWORD` = Your database password
   - `SPRING_PROFILES_ACTIVE` = prod
6. **Deploy** - Railway auto-builds and deploys!

#### Render
1. **Sign up** at [render.com](https://render.com)
2. **Create Web Service**
3. **Connect GitHub** repository
4. **Configure**:
   - **Build Command**: `./mvnw clean package`
   - **Start Command**: `java -jar target/Expense-Tracker-end-0.0.1-SNAPSHOT.jar`
5. **Add environment variables** (same as Railway)
6. **Deploy**!

### Environment Variables
```bash
DATABASE_URL=jdbc:mysql://your-host:3306/your-database
DB_USERNAME=your-username
DB_PASSWORD=your-password
SPRING_PROFILES_ACTIVE=prod
PORT=8080
```

### Production Configuration
The application includes production-ready configuration in `application-prod.properties`:
- **Optimized logging** levels
- **Template caching** enabled
- **Environment variable** support
- **Security hardening**

## 🧪 Testing

### Run Tests
```bash
./mvnw test
```

### Manual Testing Checklist
- [ ] **User Registration** - Create new account
- [ ] **User Login** - Test authentication
- [ ] **Add Expense** - Create test expenses
- [ ] **Edit Expense** - Update existing expenses
- [ ] **Delete Expense** - Remove expenses
- [ ] **Admin Features** - Test admin role (if admin)
- [ ] **Mobile Responsive** - Test on different screen sizes

## 🔐 Security Features

### Authentication & Authorization
- **BCrypt Password Hashing** - Secure password storage
- **Session Management** - Secure user sessions
- **Role-based Access Control** - USER/ADMIN permissions
- **CSRF Protection** - Cross-site request forgery protection
- **Input Validation** - Server-side validation

### Data Protection
- **User Data Isolation** - Users can only access their own data
- **Admin Access Control** - Admin features protected by role checks
- **SQL Injection Prevention** - JPA/Hibernate protection
- **XSS Protection** - Thymeleaf auto-escaping

## 🎨 UI/UX Features

### Design System
- **Glassmorphism** - Modern glass-like effects
- **Gradient Backgrounds** - Purple/blue theme
- **Backdrop Blur** - Advanced CSS effects
- **Smooth Animations** - Hover effects and transitions
- **Responsive Grid** - Mobile-first layout

### Interactive Elements
- **Smart Date Selection** - Today/Yesterday/Custom options
- **Category Grid** - Visual category selection with icons
- **Form Validation** - Real-time validation feedback
- **Loading States** - Smooth loading animations
- **Flash Messages** - Success/error notifications

### Accessibility
- **Semantic HTML** - Proper heading structure
- **ARIA Labels** - Screen reader support
- **Keyboard Navigation** - Full keyboard accessibility
- **Color Contrast** - WCAG compliant colors

## 📊 Business Logic

### Expense Management
- **Auto-categorization** - Smart categorization based on description
- **Balance Tracking** - Automatic account balance updates
- **Date Handling** - Flexible date selection and validation
- **Amount Validation** - Positive number validation

### Analytics & Reporting
- **Total Expenses** - Sum of all user expenses
- **Highest Expense** - Maximum expense amount
- **Category Summary** - Expenses grouped by category
- **Recent Activity** - Latest expense timeline
- **User Statistics** - Individual user analytics

### Admin Features
- **User Management** - View all users in system
- **Role Management** - Toggle user roles (USER/ADMIN)
- **User Deletion** - Remove users (with safety checks)
- **System Statistics** - Overall application metrics

## 🤝 Contributing

### Development Setup
1. **Fork** the repository
2. **Create** feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** changes (`git commit -m 'Add AmazingFeature'`)
4. **Push** to branch (`git push origin feature/AmazingFeature`)
5. **Open** Pull Request

### Code Style
- **Java** - Follow Spring Boot conventions
- **HTML/CSS** - Use semantic HTML and modern CSS
- **JavaScript** - ES6+ with proper error handling
- **Documentation** - Include comments for complex logic

### Testing Guidelines
- **Unit Tests** - Test service layer methods
- **Integration Tests** - Test controller endpoints
- **Security Tests** - Test authentication and authorization
- **UI Tests** - Test critical user flows

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Spring Boot** - Amazing framework and ecosystem
- **Font Awesome** - Beautiful icons and visual elements
- **Glassmorphism** - Modern design inspiration
- **MySQL** - Reliable database system
- **Thymeleaf** - Powerful template engine

## 📞 Support

### Getting Help
- **GitHub Issues** - Report bugs or request features
- **Documentation** - Check [DEPLOYMENT.md](DEPLOYMENT.md) for deployment help
- **Configuration** - Review application properties for setup

### Common Issues
1. **Database Connection** - Check MySQL credentials and connection
2. **Build Failures** - Ensure Java 17+ and Maven are installed
3. **Port Conflicts** - Change server.port in application.properties
4. **Security Issues** - Verify CSRF and authentication configuration

---

**Made with ❤️ using Spring Boot and modern web technologies**

⭐ **Star this repository if you found it helpful!**

🔗 **Live Demo**: [Your deployed URL here]
📧 **Contact**: [Your email here] 