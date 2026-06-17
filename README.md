# Blood Bank & Donor Management System

---

## Technology Stack
| Layer       | Technology                          |
|-------------|-------------------------------------|
| Language    | Java 11+                            |
| Database    | MySQL 8.x                           |
| Connectivity| JDBC (mysql-connector-j-8.x.jar)    |
| IDE         | VS Code / IntelliJ / Eclipse        |

---

## Project Structure
```
BloodBankSystem/
├── sql/
│   └── schema.sql                  ← DB schema + seed data
└── src/
    └── com/bloodbank/
        ├── model/                  ← POJOs (Donor, Staff, Patient, Blood*)
        │   ├── Donor.java
        │   ├── Staff.java
        │   ├── Patient.java
        │   ├── BloodInventory.java
        │   ├── BloodDonation.java
        │   └── BloodIssue.java
        ├── dao/                    ← JDBC Data Access Objects
        │   ├── DonorDAO.java
        │   ├── StaffDAO.java
        │   ├── BloodInventoryDAO.java
        │   ├── PatientDAO.java
        │   ├── BloodDonationDAO.java
        │   └── BloodIssueDAO.java
        ├── auth/
        │   └── AuthService.java    ← Role-based login (Admin / Staff)
        ├── util/
        │   ├── DBConnection.java   ← Singleton JDBC connection
        │   └── ReportService.java  ← All report types
        └── ui/
            └── MainMenu.java       ← Console-based interactive menu
```

---

## Setup Instructions

### Step 1 — MySQL Setup
```sql
-- Run in MySQL Workbench or terminal:
mysql -u root -p < sql/schema.sql
```
This creates the `blood_bank_db` database with all tables and a default Admin account.

**Default Admin Login:**
- Email: `admin@bloodbank.com`
- Password: `Admin@123`

### Step 2 — Configure DB Connection
Edit `src/com/bloodbank/util/DBConnection.java`:
```java
private static final String DB_URL  = "jdbc:mysql://localhost:3306/blood_bank_db?useSSL=false&serverTimezone=UTC";
private static final String USER     = "root";
private static final String PASSWORD = "";   // ← set your MySQL password here
```

### Step 3 — Add JDBC Driver
Download `mysql-connector-j-8.x.x.jar` from https://dev.mysql.com/downloads/connector/j/
and place it in your project's `lib/` folder.

### Step 4 — Compile & Run

**Using command line:**
```bash
# Compile
javac -cp lib/mysql-connector-j.jar -d out/ $(find src -name "*.java")

# Run
java -cp out/:lib/mysql-connector-j.jar com.bloodbank.ui.MainMenu
```

**Using IntelliJ IDEA:**
1. Open the `BloodBankSystem` folder as a project
2. Add `mysql-connector-j.jar` to Project Structure → Libraries
3. Run `com.bloodbank.ui.MainMenu`

**Using Eclipse:**
1. Import as existing Java project
2. Right-click project → Build Path → Add External JAR → select connector jar
3. Run `MainMenu.java`

---

## Features Implemented

### Master Forms
| Module           | Operations                               |
|------------------|------------------------------------------|
| Donor Form       | Register, Update, Search (name/blood), Delete |
| Staff Form       | Add, Update, List, Delete (Admin only)   |
| Blood Inventory  | Add units, View all, Summary by group    |
| Patient Form     | Register, List all                       |

### Transaction Forms
| Module           | Logic                                              |
|------------------|----------------------------------------------------|
| Blood Donation   | 90-day eligibility check, inventory increment, last-donated update — all in one DB transaction |
| Blood Issue      | Stock availability check, inventory decrement, issue record — all in one DB transaction       |

### Reports
| Report                       | Description                                 |
|------------------------------|---------------------------------------------|
| Donor Report                 | All donors with blood group & contact       |
| Staff Report                 | All employees with designation & role       |
| Issue Report                 | Issues in a date range by patient           |
| Daily Collection Report      | Units collected on a specific date          |
| Inventory Summary            | Total units per blood group                 |

### Security
- **Role-Based Access Control:** Admin and Staff roles; Staff management restricted to Admin
- **SHA-256 password hashing** applied at DB level (`SHA2(password, 256)`)
- **Session management** via `AuthService` singleton

### Data Integrity
- **JDBC Transactions** (`setAutoCommit(false)` + `commit()` / `rollback()`) on all multi-table operations
- **PreparedStatements** on every query — prevents SQL injection
- **Foreign key constraints** enforce referential integrity across all tables
- **Optimized schema** — normalized tables, indexed PKs and unique fields

---

## Database Schema Overview

```
staff          ←─── blood_issue ──→ patient
  │                     │
  │                     ↓
  └──────────→ blood_inventory ←── blood_donation ←── donor
```

---

## Future Enhancements (from project report)
1. SMS / Email notification for donor eligibility reminders
2. Mobile app (Android / iOS)
3. Geo-location mapping of blood banks
4. AI-based donor-recipient matching
5. QR-coded donor ID cards
6. Blockchain for tamper-proof medical records
7. Integration with national health portals
