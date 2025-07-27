\# 💰 Expense Tracker - Full Stack Web Application



A modern full-stack expense tracking application built with \*\*Spring Boot 3.5.3\*\*. Features include secure authentication, intelligent expense management, beautiful glassmorphism UI, and role-based access control.



---



\## 📋 Table of Contents



\- \[Features](#-features)

\- \[Technology Stack](#-technology-stack)

\- \[Architecture](#-architecture)

\- \[Installation \& Setup](#️-installation--setup)

\- \[API Documentation](#-api-documentation)

\- \[Screenshots](#-screenshots)

\- \[Security](#-security)



---



\## 🎯 Features



\### 🔧 Core Features

\- 🔐 Secure Login \& Registration with BCrypt  

\- 💰 Add, edit, delete expenses  

\- 📊 Dashboard with real-time stats  

\- 🎭 Role-based access (USER / ADMIN)  

\- 📱 Responsive UI with glassmorphism design  



\### 🎨 UI/UX Highlights

\- 🌈 Interactive cards and animations  

\- 📅 Smart date picker (Today, Yesterday, Custom)  

\- 📁 Category icons for visual expense grouping  

\- ✅ Flash messages, form validation, loading states  



\### 🧠 Backend Capabilities

\- 🧾 Auto-categorization based on keywords  

\- 💳 Financial account tracking  

\- 📦 REST APIs for frontend/mobile integration  

\- 🛡️ CSRF protection and session management  



---



\## 🏗️ Technology Stack



\### 🖥 Backend

\- Spring Boot 3.5.3, Spring Security, JPA  

\- MySQL 8.0, H2 (test)  

\- Maven  



\### 🌐 Frontend

\- Thymeleaf, HTML5, CSS3, JavaScript, Font Awesome  



\### 🛠 Tools

\- Spring DevTools, JUnit, Mockito  



---



\## 🧱 Architecture



com.example.expensetracker/

├── controller/ # Web endpoints (auth, user, admin)

├── model/ # Entities: User, Expense, FinancialAccount

├── repository/ # Data access layers

├── service/ # Business logic

├── config/ # Spring Security setup

├── utils/ # Helpers (e.g., number formatting)

└── ExpenseTrackerApplication.java



yaml

Copy

Edit







\## ⚙️ Installation \& Setup



\### ✅ Prerequisites

\- Java 17+

\- MySQL 8.0+

\- Maven 3.6+



\### 🚀 Setup Steps



```bash

git clone https://github.com/yourusername/expense-tracker.git

cd expense-tracker

Create the database:



sql

Copy

Edit

CREATE DATABASE expense\_tracker;

Configure DB credentials in:



css

Copy

Edit

src/main/resources/application.properties

Then run:



bash

Copy

Edit

./mvnw clean package

./mvnw spring-boot:run

Access app at: http://localhost:8080



📚 API Documentation





🌐 REST APIs (JSON)

POST /api/signup – Register



POST /api/login – Login



POST /api/addExpense – Add expense



GET /api/expense/{id} – Fetch expense



PUT /api/expense/{id} – Update expense



DELETE /api/expense/{id} – Delete



📸 Screenshots

🔐 Login Page

!\[Login](assets/login.png)



📊 Dashboard

!\[dashboard](assets/dashboard.png)



➕ Add Expense

!\[AddExpense](assets/addExpense.png)



🔐 Security

🔒 BCrypt password hashing



🔐 Role-based access (USER vs ADMIN)



🛡️ CSRF protection



🔐 Session management



🧼 Input validation



🚫 SQL/XSS protection with JPA \& Thymeleaf



