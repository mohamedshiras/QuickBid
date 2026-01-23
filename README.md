# 🏷️ QuickBid — Auction Management System

[![Status](https://img.shields.io/badge/Status-Active-brightgreen)]()
[![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-blue?logo=springboot)]()
[![Java](https://img.shields.io/badge/Language-Java-red?logo=java)]()
[![Frontend](https://img.shields.io/badge/Tech-HTML%20%7C%20CSS%20%7C%20JS-orange)]()
[![License-MIT](https://img.shields.io/badge/License-MIT-yellow)]()

**QuickBid** is a Full-Stack Auction Management System developed as part of the **Enterprise Application Development II (EAD 2)** module.  
It provides a platform to create, manage, and bid on auctions with a Spring Boot backend and a responsive HTML/CSS/JavaScript frontend.

---

## 🧠 Overview

QuickBid lets users:

- **List items for auction**
- **Place bids**
- **Track current highest bids**
- Manage auction inventory
- Authenticate users and manage sessions

Built with a scalable backend architecture and a user-friendly frontend interface.

---

## 🛠️ Tech Stack

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.x-brightgreen)]()
[![Hibernate](https://img.shields.io/badge/Hibernate-ORM-orange)]()
[![MySQL](https://img.shields.io/badge/Database-MySQL-blue)]()
[![HTML5](https://img.shields.io/badge/HTML5-orange?logo=html5)]()
[![CSS3](https://img.shields.io/badge/CSS3-blue?logo=css3)]()
[![JavaScript](https://img.shields.io/badge/JavaScript-yellow?logo=javascript)]()

✔ **Spring Boot** — Backend framework  
✔ **Hibernate / JPA** — ORM for data persistence  
✔ **MySQL** — Relational database  
✔ **RESTful APIs** — Backend APIs  
✔ **HTML/CSS/JS** — Frontend UI  
✔ **Maven** — Build tooling  

---

## 📁 Project Structure

```text
QuickBid/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── .../
│   │   │       ├── controller/              # REST & Web controllers
│   │   │       ├── service/                 # Business logic
│   │   │       ├── repository/              # Data access layer
│   │   │       ├── model/                   # Entities / DTOs
│   │   │       └── QuickBidApplication.java # Spring Boot entry point
│   │   └── resources/
│   │       ├── static/                      # CSS, JS, frontend files
│   │       ├── templates/                   # HTML (Thymeleaf)
│   │       └── application.properties       # App configuration
├── .gitignore
├── pom.xml                                  # Maven configuration
└── README.md


---

## 🔍 Features

- User authentication & authorization
- Create and manage auctions
- Place and track bids in real time
- Auction listing and browsing
- Validation and error handling
- Responsive UI interface

---

## 📦 Getting Started (Local Setup)

### 🧾 Prerequisites

- Java 17+ installed
- MySQL database setup
- Maven installed
- (Optional) Postman for API testing

---

### 📥 Installation

Clone the repository:

```bash
git clone https://github.com/mohamedshiras/QuickBid.git
cd QuickBid
