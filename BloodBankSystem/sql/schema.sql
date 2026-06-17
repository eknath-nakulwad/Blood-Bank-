-- ============================================================
-- Blood Bank & Donor Management System - Database Schema
-- Technology: MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS blood_bank_db;
USE blood_bank_db;

-- ========================
-- STAFF TABLE
-- ========================
CREATE TABLE IF NOT EXISTS staff (
    employee_number VARCHAR(20) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    date_of_joining DATE NOT NULL,
    address         VARCHAR(255),
    gender          ENUM('Male','Female','Other') NOT NULL,
    qualification   VARCHAR(100),
    contact_no      VARCHAR(15) UNIQUE NOT NULL,
    designation     VARCHAR(100),
    email_id        VARCHAR(100) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    role            ENUM('Admin','Staff') DEFAULT 'Staff',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- DONOR TABLE
-- ========================
CREATE TABLE IF NOT EXISTS donor (
    donor_number    VARCHAR(20) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    dob             DATE NOT NULL,
    gender          ENUM('Male','Female','Other') NOT NULL,
    address         VARCHAR(255),
    city            VARCHAR(50),
    pin             VARCHAR(10),
    state           VARCHAR(50),
    contact_no      VARCHAR(15) UNIQUE NOT NULL,
    email_id        VARCHAR(100),
    blood_group     ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') NOT NULL,
    fathers_name    VARCHAR(100),
    occupation      VARCHAR(100),
    last_donated    DATE,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- BLOOD INVENTORY TABLE
-- ========================
CREATE TABLE IF NOT EXISTS blood_inventory (
    blood_unit_no   VARCHAR(20) PRIMARY KEY,
    blood_group     ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') NOT NULL,
    no_of_units     INT NOT NULL DEFAULT 0,
    pack_type       ENUM('Single','Double','Triple') DEFAULT 'Single',
    collection_date DATE,
    expiry_date     DATE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- PATIENT TABLE
-- ========================
CREATE TABLE IF NOT EXISTS patient (
    patient_no      VARCHAR(20) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    dob             DATE,
    address         VARCHAR(255),
    city            VARCHAR(50),
    contact_no      VARCHAR(15),
    pin             VARCHAR(10),
    blood_group     ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') NOT NULL,
    request_date    DATE NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- BLOOD DONATION (TRANSACTION)
-- ========================
CREATE TABLE IF NOT EXISTS blood_donation (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    donation_date   DATE NOT NULL,
    donor_number    VARCHAR(20) NOT NULL,
    blood_unit_no   VARCHAR(20) NOT NULL,
    no_of_units     INT NOT NULL DEFAULT 1,
    age_at_donation INT,
    time_elapsed    INT COMMENT 'Days since last donation',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (donor_number)  REFERENCES donor(donor_number),
    FOREIGN KEY (blood_unit_no) REFERENCES blood_inventory(blood_unit_no)
);

-- ========================
-- BLOOD ISSUE (TRANSACTION)
-- ========================
CREATE TABLE IF NOT EXISTS blood_issue (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    issue_date      DATE NOT NULL,
    patient_no      VARCHAR(20) NOT NULL,
    blood_unit_no   VARCHAR(20) NOT NULL,
    blood_group     ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') NOT NULL,
    no_of_units     INT NOT NULL,
    issue_type      ENUM('Plasma','Platelets','RBC','Whole Blood') NOT NULL,
    issued_by       VARCHAR(20) NOT NULL COMMENT 'employee_number',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_no)    REFERENCES patient(patient_no),
    FOREIGN KEY (blood_unit_no) REFERENCES blood_inventory(blood_unit_no),
    FOREIGN KEY (issued_by)     REFERENCES staff(employee_number)
);

-- ========================
-- BLOOD DONATION CAMP
-- ========================
CREATE TABLE IF NOT EXISTS donation_camp (
    camp_id         INT AUTO_INCREMENT PRIMARY KEY,
    organization    VARCHAR(200) NOT NULL,
    camp_date       DATE NOT NULL,
    location        VARCHAR(255),
    units_collected INT DEFAULT 0,
    organized_by    VARCHAR(20) COMMENT 'employee_number',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================
-- SEED: Default Admin
-- Password: Admin@123 (SHA-256 hashed below — replace with real hash in production)
-- ========================
INSERT IGNORE INTO staff (employee_number, name, date_of_joining, gender, contact_no, email_id, password_hash, designation, role)
VALUES ('EMP001', 'Admin User', CURDATE(), 'Male', '9999999999', 'admin@bloodbank.com',
        SHA2('Admin@123', 256), 'System Administrator', 'Admin');

-- ========================
-- SEED: Sample Blood Inventory
-- ========================
INSERT IGNORE INTO blood_inventory (blood_unit_no, blood_group, no_of_units, pack_type, collection_date, expiry_date) VALUES
('BU001','A+',10,'Single',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU002','A-', 5,'Single',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU003','B+', 8,'Double',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU004','B-', 3,'Single',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU005','AB+',6,'Triple',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU006','AB-',2,'Single',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU007','O+',15,'Double',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY)),
('BU008','O-', 4,'Single',CURDATE(), DATE_ADD(CURDATE(), INTERVAL 35 DAY));
