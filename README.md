💰 Expense Tracker - Full Stack Web Application
A modern full-stack expense tracking application built with Spring Boot 3.5.3. Features include secure authentication, intelligent expense management, beautiful glassmorphism UI, and role-based access control.


📋 Table of Contents
• Features
• Technology Stack
• Architecture
• Database Schema
• Installation & Setup
• API Documentation
• Screenshots
• Security
• Contributing

🎯 Features
Core Features
• 🔐 Secure Login & Registration with BCrypt
• 💰 Add, edit, delete expenses
• 📊 Dashboard with real-time stats
• 🎭 Role-based access (USER / ADMIN)
• 📱 Responsive UI with modern glassmorphism design
UI/UX Highlights
• 🌈 Interactive cards and animations
• 📅 Smart date picker (Today, Yesterday, Custom)
• 📁 Category icons for visual expense grouping
• ✅ Flash messages, form validation, loading states
Backend Capabilities
• 🧠 Auto-categorization based on keywords
• 🧾 Financial account tracking
• 📦 REST APIs for frontend/mobile integration
• 🛡️ CSRF protection and session management

🏗️ Technology Stack
Backend
• Spring Boot 3.5.3, Spring Security, JPA, MySQL 8.0, H2 (test), Maven
Frontend
• Thymeleaf, HTML5, CSS3, JavaScript, Font Awesome
Tools
• Spring DevTools, Spring Boot Test, JUnit, Mockito

🧱 Architecture
text
CopyEdit
com.example.expensetracker/
├── controller/       → Web endpoints (auth, user, admin)
├── model/            → Entities: User, Expense, FinancialAccount
├── repository/       → Data access layers
├── service/          → Business logic
├── config/           → Spring Security setup
├── utils/            → Helpers (e.g., number formatting)
└── ExpenseTrackerApplication.java

🗄️ Database Schema
user
sql
CopyEdit
id | username | email | password | role (USER/ADMIN)
financial_account
sql
CopyEdit
id | name | balance | budget_limit | user_id (FK)
expense
sql
CopyEdit
id | description | amount | date | category | account_id (FK)

⚙️ Installation & Setup
Prerequisites
• Java 17+
• MySQL 8.0+
• Maven 3.6+
Steps
bash
CopyEdit
git clone https://github.com/yourusername/expense-tracker.git
cd expense-tracker

# Create the database in MySQL
CREATE DATABASE expense_tracker;

# Configure your DB credentials
# Edit src/main/resources/application.properties

./mvnw clean package
./mvnw spring-boot:run
Access app at http://localhost:8080

📚 API Documentation
Authentication
• POST /signup – Register
• POST /login – Login
• GET /logout – Logout
Expenses
• GET /dashboard – Dashboard view
• POST /saveExpense – Add expense
• GET /editExpense/{id} – Edit form
• POST /updateExpense – Update expense
• GET /deleteExpense/{id} – Delete
Admin
• GET /admin/dashboard – Admin view
• POST /admin/users/{id}/toggle-role – Change user role
• POST /admin/users/{id}/delete – Delete user
REST APIs (JSON)
• POST /api/signup – Register via JSON
• POST /api/login – Login via JSON
• POST /api/addExpense – Create expense
• GET /api/expense/{id} – Fetch expense
• PUT /api/expense/{id} – Update expense
• DELETE /api/expense/{id} – Delete expense

📸 Screenshots

Login Page
(assets/login.png)

Dashboard 
(assets/dashboard.png)

Add Expense
(assets/addExpense.png)


🔐 Security
• BCrypt password hashing
• Role-based access (USER vs ADMIN)
• CSRF protection
• Session management
• Input validation
• SQL/XSS protection with JPA and Thymeleaf escaping

🤝 Contributing
Steps
1. Fork the repo
2. Create a new branch: feature/my-feature
3. Commit changes
4. Push and open a pull request

