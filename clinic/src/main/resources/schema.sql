-- ล้างตารางเก่าทิ้งทั้งหมดเพื่อให้แน่ใจว่าสะอาด
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS appointment;
DROP TABLE IF EXISTS doctor_profile;
DROP TABLE IF EXISTS patient;
DROP TABLE IF EXISTS doctor;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- 1. ตารางสำหรับ Roles (เหมือนเดิม)
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. ตาราง Users สำหรับเก็บข้อมูล Login (เหมือนเดิม)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE
);

-- 3. ตารางเชื่อม Users กับ Roles (เหมือนเดิม)
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 4. ตาราง Doctor (เหมือนเดิม)
CREATE TABLE doctor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    specialization VARCHAR(100),
    user_id BIGINT UNIQUE,
    CONSTRAINT fk_doctor_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 5. ตาราง Doctor Profile (เหมือนเดิม)
CREATE TABLE doctor_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_number VARCHAR(50),
    biography VARCHAR(255),
    doctor_id BIGINT UNIQUE,
    CONSTRAINT fk_profile_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

-- 6. ตาราง Patient (เหมือนเดิม)
CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    user_id BIGINT UNIQUE,
    name_set BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT fk_patient_user FOREIGN KEY (user_id) REFERENCES users(id)
);
-- 7. ตาราง Appointment (ปรับปรุงใหม่!)
CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_date TIMESTAMP,
    status VARCHAR(20),
    symptoms VARCHAR(255), -- *** เพิ่มคอลัมน์สำหรับอาการเบื้องต้น ***
    doctor_id BIGINT,
    patient_id BIGINT,
    CONSTRAINT fk_appt_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
);

