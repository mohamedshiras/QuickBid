# 🏷️ QuickBid - Auction Management System

[![Status](https://img.shields.io/badge/Status-Active-brightgreen)](https://github.com/mohamedshiras/QuickBid)
[![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-blue?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Language-Java-red?logo=java)](https://www.java.com)
[![Frontend](https://img.shields.io/badge/Tech-HTML%20%7C%20CSS%20%7C%20JS-orange)](https://developer.mozilla.org)
[![License-MIT](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

**QuickBid** is a comprehensive Full-Stack Auction Management System developed as part of the **Enterprise Application Development II (EAD 2)** module. It provides a robust platform for creating, managing, and participating in online auctions with real-time bidding capabilities.

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#️-tech-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Database Configuration](#database-configuration)
  - [Running the Application](#running-the-application)
- [API Documentation](#-api-documentation)
- [Usage](#-usage)
- [Screenshots](#-screenshots)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## 🧠 Overview

QuickBid is designed to streamline the auction process by providing a secure, user-friendly platform where users can:

- **List items for auction** with detailed descriptions and images
- **Place competitive bids** on active auctions
- **Track current highest bids** in real-time
- **Manage auction inventory** with full CRUD operations
- **Authenticate securely** with session management
- **View auction history** and transaction records

The application follows enterprise-level design patterns with a scalable Spring Boot backend and a responsive, modern frontend interface.

---

## ✨ Features

### User Management
- ✅ User registration and authentication
- ✅ Role-based access control (Admin/User)
- ✅ Profile management
- ✅ Session management with security

### Auction Management
- ✅ Create new auctions with details
- ✅ Edit and delete auctions (owner only)
- ✅ Set starting price and auction duration
- ✅ Upload item images
- ✅ Auction status tracking (Active/Closed)

### Bidding System
- ✅ Real-time bid placement
- ✅ Automatic highest bid tracking
- ✅ Bid validation (minimum increment)
- ✅ Bid history for each auction
- ✅ Notification system for outbid users

### Additional Features
- ✅ Search and filter auctions
- ✅ Responsive design for mobile/desktop
- ✅ RESTful API architecture
- ✅ Input validation and error handling
- ✅ Transaction logging

---

## 🛠️ Tech Stack

### Backend
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-brightgreen?logo=springboot)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-orange?logo=hibernate)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue?logo=mysql)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)

- **Spring Boot** - Backend framework
- **Spring MVC** - Web framework
- **Spring Data JPA** - Data access layer
- **Hibernate** - ORM for database mapping
- **MySQL** - Relational database
- **Spring Security** - Authentication & authorization
- **Maven** - Dependency management

### Frontend
![HTML5](https://img.shields.io/badge/HTML5-orange?logo=html5)
![CSS3](https://img.shields.io/badge/CSS3-blue?logo=css3)
![JavaScript](https://img.shields.io/badge/JavaScript-yellow?logo=javascript)

- **HTML5** - Markup
- **CSS3** - Styling
- **JavaScript** - Client-side logic
- **Bootstrap** (optional) - UI framework
- **Thymeleaf** - Template engine

---

## 🏗️ Architecture

```
┌─────────────────┐
│   Frontend UI   │
│  (HTML/CSS/JS)  │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│   Controllers   │
│   (REST/MVC)    │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│    Services     │
│ (Business Logic)│
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  Repositories   │
│  (Data Access)  │
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│   MySQL DB      │
│   (Persistence) │
└─────────────────┘
```

---

## 📁 Project Structure

```
QuickBid/
├── src/
│   ├── main/
│   │   ├── java/com/quickbid/
│   │   │   ├── controller/          # REST & Web controllers
│   │   │   │   ├── AuctionController.java
│   │   │   │   ├── BidController.java
│   │   │   │   └── UserController.java
│   │   │   ├── service/             # Business logic layer
│   │   │   │   ├── AuctionService.java
│   │   │   │   ├── BidService.java
│   │   │   │   └── UserService.java
│   │   │   ├── repository/          # Data access layer
│   │   │   │   ├── AuctionRepository.java
│   │   │   │   ├── BidRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── model/               # Entity classes
│   │   │   │   ├── Auction.java
│   │   │   │   ├── Bid.java
│   │   │   │   └── User.java
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── config/              # Configuration classes
│   │   │   ├── security/            # Security configurations
│   │   │   └── QuickBidApplication.java
│   │   └── resources/
│   │       ├── static/              # CSS, JS, images
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       ├── templates/           # HTML templates
│   │       │   ├── index.html
│   │       │   ├── auction-list.html
│   │       │   ├── auction-detail.html
│   │       │   └── user-profile.html
│   │       └── application.properties
│   └── test/
│       └── java/                    # Unit & Integration tests
├── .mvn/                            # Maven wrapper
├── .gitignore
├── pom.xml                          # Maven configuration
├── mvnw                             # Maven wrapper script
├── mvnw.cmd                         # Maven wrapper (Windows)
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:

- **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
- **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/)
- **Git** - [Download](https://git-scm.com/downloads)
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code (optional)

### Installation

1. **Clone the repository**

```bash
git clone https://github.com/mohamedshiras/QuickBid.git
cd QuickBid
```

2. **Create MySQL Database**

```sql
CREATE DATABASE quickbid_db;
```

3. **Configure Database Connection**

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/quickbid_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Server Configuration
server.port=8080

# File Upload Configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

4. **Build the Project**

```bash
./mvnw clean install
```

Or on Windows:

```cmd
mvnw.cmd clean install
```

### Running the Application

**Method 1: Using Maven**

```bash
./mvnw spring-boot:run
```

**Method 2: Using JAR file**

```bash
java -jar target/quickbid-0.0.1-SNAPSHOT.jar
```

**Method 3: Using IDE**

Run the `QuickBidApplication.java` main class from your IDE.

The application will start on `http://localhost:8080`

---

## 📡 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/logout` | User logout |

### Auction Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/auctions` | Get all auctions |
| GET | `/api/auctions/{id}` | Get auction by ID |
| POST | `/api/auctions` | Create new auction |
| PUT | `/api/auctions/{id}` | Update auction |
| DELETE | `/api/auctions/{id}` | Delete auction |

### Bid Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/bids/auction/{auctionId}` | Get bids for auction |
| POST | `/api/bids` | Place a bid |
| GET | `/api/bids/user/{userId}` | Get user's bids |

### Example Request

**Create Auction**

```bash
curl -X POST http://localhost:8080/api/auctions \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Vintage Camera",
    "description": "Classic 1960s camera in excellent condition",
    "startingPrice": 150.00,
    "endTime": "2026-02-01T18:00:00"
  }'
```

---

## 💡 Usage

1. **Register/Login** - Create an account or login with existing credentials
2. **Browse Auctions** - View all active auctions on the homepage
3. **View Details** - Click on an auction to see full details and bid history
4. **Place Bid** - Enter your bid amount (must be higher than current highest bid)
5. **Create Auction** - List your own items for auction
6. **Monitor** - Track your bids and auctions from your profile

---

## 🧪 Testing

### Run Unit Tests

```bash
./mvnw test
```

### Run Integration Tests

```bash
./mvnw verify
```

### Test Coverage

```bash
./mvnw jacoco:report
```

View coverage report at `target/site/jacoco/index.html`

---

## 🌐 Deployment

### Deploy to Heroku

1. Install Heroku CLI
2. Create Heroku app

```bash
heroku create quickbid-app
```

3. Add MySQL addon

```bash
heroku addons:create jawsdb:kitefin
```

4. Deploy

```bash
git push heroku master
```

### Deploy with Docker

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/quickbid-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:

```bash
docker build -t quickbid .
docker run -p 8080:8080 quickbid
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Contributors

- [Mohamed Shiras](https://github.com/mohamedshiras)       -  Project Lead & Developer
- [Nimesh Kolambage](https://github.com/NimeshKolambage)   -  Developer
- [Diran Dilshan](https://github.com/Diran-Dilshan)        -  Developer
- [Thaweesha Perera](https://github.com/ThaweeshaPerera7)  -  Developer

---

## 📧 Contact

**Mohamed Shiras**

- GitHub: [@mohamedshiras](https://github.com/mohamedshiras)
- Project Link: [https://github.com/mohamedshiras/QuickBid](https://github.com/mohamedshiras/QuickBid)

---

## 🙏 Acknowledgments

- Developed as part of EAD 2 (Enterprise Application Development II) module
- Spring Boot Documentation
- MySQL Documentation
- Bootstrap for UI components
- All contributors and supporters

---

## 🗺️ Roadmap

- [ ] Email notifications for bid updates
- [ ] Payment integration
- [ ] Advanced search filters
- [ ] Mobile app (Android/iOS)
- [ ] Admin dashboard with analytics
- [ ] Auction recommendations based on user interest
- [ ] WebSocket for real-time bid updates

---

<div align="center">

**⭐ If you find this project useful, please consider giving it a star! ⭐**

Made with ❤️ by [Mohamed Shiras](https://github.com/mohamedshiras)

</div>
