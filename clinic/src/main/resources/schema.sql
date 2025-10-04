-- ล้างตาราง (ถ้ามีอยู่) ด้วยคำสั่งของ PostgreSQL
DROP TABLE IF EXISTS user_roles, appointment, doctor_profile, patient, doctor, users, roles CASCADE;

-- 1. ตารางสำหรับ Roles
CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- 2. ตาราง Users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE
);

-- 3. ตารางเชื่อม Users กับ Roles
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id),
    role_id BIGINT NOT NULL REFERENCES roles(id),
    PRIMARY KEY (user_id, role_id)
);

-- 4. ตาราง Doctor
CREATE TABLE doctor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    specialization VARCHAR(100),
    user_id BIGINT UNIQUE REFERENCES users(id)
);

-- 5. ตาราง Doctor Profile
CREATE TABLE doctor_profile (
    id BIGSERIAL PRIMARY KEY,
    license_number VARCHAR(50),
    biography TEXT,
    doctor_id BIGINT UNIQUE REFERENCES doctor(id)
);

-- 6. ตาราง Patient
CREATE TABLE patient (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    user_id BIGINT UNIQUE REFERENCES users(id),
    name_set BOOLEAN DEFAULT FALSE NOT NULL
);

-- 7. ตาราง Appointment
CREATE TABLE appointment (
    id BIGSERIAL PRIMARY KEY,
    appointment_date TIMESTAMP,
    status VARCHAR(20),
    symptoms TEXT,
    doctor_id BIGINT REFERENCES doctor(id),
    patient_id BIGINT REFERENCES patient(id)
);

