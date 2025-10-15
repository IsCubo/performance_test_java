# 📚 LibroNova - Library Management System

![Java](https://img.shields.io/badge/Java-17-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Maven](https://img.shields.io/badge/Maven-Project-red)
![Swing](https://img.shields.io/badge/UI-Swing-green)

Comprehensive library management system developed in Java with a layered architecture, designed to efficiently and professionally manage books, members, loans, and system users.

---

## 🎯 Main Features

### 📖 Book Management
- ✅ Full book registration (ISBN, title, author, category, copies)
- ✅ Bibliographic information updates
- ✅ Real-time availability control
- ✅ Activation/deactivation of books
- ✅ Filtering by author and category
- ✅ Export catalog to CSV

### 👥 Member Management
- ✅ Library member registration
- ✅ Personal data updates
- ✅ Status control (ACTIVE/INACTIVE)
- ✅ Validation of active members for loans

### 📋 Loan System
- ✅ Loan registration with ACID transactions
- ✅ Automatic stock control
- ✅ Due date calculation
- ✅ Returns with automatic fine calculation
- ✅ Overdue loan reporting
- ✅ Export overdue loans to CSV

### 👤 System User Management
- ✅ Access control with roles (ADMIN/ASSISTANT)
- ✅ User creation with default values (Decorator Pattern)
- ✅ Secure authentication
- ✅ Management exclusive to administrators

### 📊 Data Export
- ✅ Export complete book catalog
- ✅ Export overdue loans
- ✅ CSV format with UTF-8 encoding

---

## 🏗️ System Architecture

LibroNova implements a layered architecture with clear separation of responsibilities:

```
┌─────────────────────────────────────────┐
│           VIEW LAYER (Swing)            │
│  LoginView, MainView, Dialogs, Panels  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         CONTROLLER LAYER                │
│  AuthController, BookController, etc.   │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          SERVICE LAYER                  │
│  Business Logic + Validations           │
│  + Decorator Pattern (UserService)      │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│           DAO LAYER                     │
│  Data Access with JDBC                  │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│         DATABASE (MySQL)                │
│  libronova schema                       │
└─────────────────────────────────────────┘
```


### 🎨 Design Patterns Implemented

1. **DAO Pattern** – Separation of data access  
2. **Service Layer Pattern** – Encapsulation of business logic  
3. **Decorator Pattern** – `UserWithDefaultsDecorator` for default value assignment  
4. **Strategy Pattern** – `CsvExporter` with generic mapper functions  
5. **MVC Pattern** – Separation of views, controllers, and models  
6. **Singleton Pattern** – `LoggerManager`, `PropertiesLoader`

---

## 🛠️ Technologies Used

| Technology | Version | Purpose                    |
|------------|---------|----------------------------|
| **Java**   | 17      | Main programming language  |
| **Maven**  | 3.x     | Dependency management      |
| **MySQL**  | 8.0+    | Relational database        |
| **JDBC**   | 8.0.33  | Database connectivity      |
| **Swing**  | Built-in| Graphical User Interface   |
| **JUnit**  | 5.9.3   | Unit testing               |

---

## 📦 Installation and Setup

### Prerequisites

```bash
- Java JDK 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Recommended IDE: NetBeans, IntelliJ IDEA or Eclipse

```

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/IsCubo/performance_test_java.git
cd performance_test_java

```

### 2️⃣ Configure the Database

Run the following SQL script to create the database schema:

```CREATE DATABASE libronova CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE libronova;

-- Books table
CREATE TABLE books (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    reference_price DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Members table
CREATE TABLE members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- System users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'ASSISTANT') DEFAULT 'ASSISTANT',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Loans table
CREATE TABLE loans (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_isbn VARCHAR(20) NOT NULL,
    member_id INT NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine DECIMAL(10,2) DEFAULT 0.00,
    is_returned BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (book_isbn) REFERENCES books(isbn),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Insert default admin user
INSERT INTO users (username, password, role, is_active) 
VALUES ('admin', 'admin123', 'ADMIN', TRUE);

```

### 3️⃣ Configure Properties File

Editar el archivo `src/main/resources/config.properties`:

```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/libronova
db.user=root
db.password=YOUR_PASSWORD_HERE

# Business rules
diasPrestamo=7
multaPorDia=1500

```

### 4️⃣ Build and Run

```bash
# Compile project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="com.mycompany.libronova.LibroNova"

```

Or from your IDE:
- Open the project
- Run main class: LibroNova.java

---

## 🚀 Using the System

### Login
1. Launch the application
2. Enter credentials:
   - Default username: admin
   - Default password: admin123

### Book Management
1. Menu: Management → Manage Books
2. Add book: Click "Add New Book"
3. Edit book: Select row → "Edit Selected"
4. Toggle status: Select row → "Toggle Status"

### Member Management
1. Toggle status: Select row → "Toggle Status"
2. Add member: Click "Add New Member"
3. Edit member: Select row → "Edit Selected"

### Create Loans
1. Menu: Loans → New Loan
2. Enter book ISBN and member ID
3. System validates:
   - Stock availability
   - Member active status
4. Default due date: 7 days

### Register Return
1. Menu: Loans → Register Return
2. Enter loan ID
3. System calculates fines automatically

### Export Data
1. Menu: File → Export
2. Choose::
   - "Export Book Catalog"
   - "Export Overdue Loans"

---

## 📁 Estructura del Proyecto

```
LibroNova/
├── src/
│   ├── main/
│   │   ├── java/com/mycompany/libronova/
│   │   │   ├── controller/          # Controladores
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── BookController.java
│   │   │   │   ├── MemberController.java
│   │   │   │   ├── UserController.java
│   │   │   │   ├── LoanController.java
│   │   │   │   └── ExportController.java
│   │   │   ├── dao/                 # Interfaces DAO
│   │   │   │   ├── BookDAO.java
│   │   │   │   ├── MemberDAO.java
│   │   │   │   ├── UserDAO.java
│   │   │   │   └── LoanDAO.java
│   │   │   ├── dao/impl/            # Implementaciones DAO
│   │   │   │   ├── BookDAOImpl.java
│   │   │   │   ├── MemberDAOImpl.java
│   │   │   │   ├── UserDAOImpl.java
│   │   │   │   └── LoanDAOImpl.java
│   │   │   ├── exception/           # Excepciones personalizadas
│   │   │   │   ├── LibroNovaException.java
│   │   │   │   ├── ErrorCode.java
│   │   │   │   ├── IsbnDuplicateException.java
│   │   │   │   ├── InsufficientStockException.java
│   │   │   │   └── InactiveMemberException.java
│   │   │   ├── model/               # Entidades del dominio
│   │   │   │   ├── Book.java
│   │   │   │   ├── Member.java
│   │   │   │   ├── User.java
│   │   │   │   ├── Loan.java
│   │   │   │   └── enums/
│   │   │   │       ├── MemberStatus.java
│   │   │   │       └── UserRole.java
│   │   │   ├── service/             # Interfaces de servicio
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── BookService.java
│   │   │   │   ├── MemberService.java
│   │   │   │   ├── UserService.java
│   │   │   │   ├── LoanService.java
│   │   │   │   └── ExportService.java
│   │   │   ├── service/impl/        # Implementaciones
│   │   │   │   ├── AuthServiceImpl.java
│   │   │   │   ├── BookServiceImpl.java
│   │   │   │   ├── MemberServiceImpl.java
│   │   │   │   ├── UserServiceImpl.java
│   │   │   │   ├── LoanServiceImpl.java
│   │   │   │   └── ExportServiceImpl.java
│   │   │   ├── service/decorator/   # Decoradores
│   │   │   │   └── UserWithDefaultsDecorator.java
│   │   │   ├── util/                # Utilidades
│   │   │   │   ├── DatabaseConnector.java
│   │   │   │   ├── LoggerManager.java
│   │   │   │   ├── PropertiesLoader.java
│   │   │   │   └── CsvExporter.java
│   │   │   ├── view/                # Vistas Swing
│   │   │   │   ├── LoginView.java
│   │   │   │   ├── MainView.java
│   │   │   │   ├── dialogs/
│   │   │   │   │   ├── BookFormDialog.java
│   │   │   │   │   ├── MemberFormDialog.java
│   │   │   │   │   └── UserFormDialog.java
│   │   │   │   └── panels/
│   │   │   │       ├── BookManagementPanel.java
│   │   │   │       ├── MemberManagementPanel.java
│   │   │   │       └── UserManagementPanel.java
│   │   │   └── LibroNova.java       # Clase principal
│   │   └── resources/
│   │       └── config.properties    # Configuración
│   └── test/                        # Tests unitarios
├── pom.xml                          # Configuración Maven
└── README.md
```

---

# 🔐 Roles and Permissions

| Feature               | ADMIN | ASSISTANT |
|-----------------------|:-----:|:---------:|
| View books            | ✅    | ✅        |
| Add/Edit books        | ✅    | ✅        |
| View members          | ✅    | ✅        |
| Add/Edit members      | ✅    | ✅        |
| Create loans          | ✅    | ✅        |
| Register returns      | ✅    | ✅        |
| Export data           | ✅    | ✅        |
| **Manage users**      | ✅    | ❌        |

---

# 🎯 Business Rules

## Loans
- **Duration:** 7 days (configurable in `config.properties`)
- **Late fee per day:** $1,500 COP (configurable)
- **Validations:**
  - Book must have available copies
  - Member must be ACTIVE
  - Unique ISBN validated on book registration

## Transactions
- Loans and returns are **atomic transactions**
- On failure, **automatic rollback** occurs
- Stock is updated automatically

---

# 📊 Logging

The system generates logs in two locations:
- **File:** `app.log` (in the root directory)
- **Console:** Real-time output

HTTP-style log format:

```
INFO: HTTP TRACE :: Method: POST, Path: /books, Status: 201 CREATED
INFO: HTTP TRACE :: Method: GET, Path: /books, Status: 200 OK
```

---

## 🧪 Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests con reporte de cobertura
mvn test jacoco:report
```

---

## 🐛 Manejo de Errores

El sistema implementa una jerarquía de excepciones personalizada:

| Código | Tipo | Descripción |
|--------|------|-------------|
| 4001 | ISBN_DUPLICATE | ISBN ya existe en el sistema |
| 4002 | STOCK_INSUFFICIENT | No hay copias disponibles |
| 4003 | PARTNER_INACTIVE | El miembro no está activo |
| 4004 | INVALID_DATA | Datos inválidos o incompletos |
| 4011 | INVALID_CREDENTIALS | Usuario o contraseña incorrectos |
| 5001 | DB_CONNECTION | Error de conexión a BD |
| 5002 | QUERY_ERROR | Error en consulta SQL |
| 5003 | TRANSACTION_ERROR | Error en transacción |
| 6002 | CSV_EXPORT_ERROR | Error al exportar CSV |

---

## 🤝 Contribución

Para contribuir al proyecto:

1. Fork el repositorio
2. Crear una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit los cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear un Pull Request

---

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 👨‍💻 Autor

**Desarrollado por:** Coder  
**Repositorio:** [github.com/IsCubo/performance_test_java](https://github.com/IsCubo/performance_test_java)

---

## 📞 Soporte

Si encuentras algún problema o tienes sugerencias:
- Crear un **Issue** en GitHub
- Contactar al desarrollador

---

## 🎓 Notas Académicas

Este proyecto fue desarrollado como parte de un ejercicio de prueba de rendimiento en Java, implementando:
- ✅ Arquitectura en capas
- ✅ Patrones de diseño GoF
- ✅ SOLID principles
- ✅ Clean Code practices
- ✅ Transacciones ACID
- ✅ Manejo profesional de excepciones

---

## 🔄 Changelog

### v1.0.0 (Actual)
- ✅ Sistema completo de gestión bibliotecaria
- ✅ Autenticación con roles
- ✅ CRUD completo de libros, miembros y usuarios
- ✅ Sistema de préstamos con cálculo de multas
- ✅ Exportación a CSV
- ✅ UI Swing profesional
- ✅ Logging robusto

---

¡Gracias por usar LibroNova! 📚✨