-- ล้างข้อมูลเก่าก่อน (เผื่อไว้)
DELETE FROM user_roles;
DELETE FROM appointment;
DELETE FROM doctor_profile;
DELETE FROM patient;
DELETE FROM doctor;
DELETE FROM users;
DELETE FROM roles;

-- 1. Roles
-- ใช้ id 1 สำหรับ PATIENT และ 2 สำหรับ DOCTOR
INSERT INTO roles (id, name) VALUES (1, 'ROLE_PATIENT');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_DOCTOR');

-- 2. Users (ใช้รหัสผ่าน "1234" แบบ Plain text)
INSERT INTO users (id, username, password) VALUES 
(1, 'patient1', '1234'),
(2, 'doctor1', '1234');

-- 3. User-Role Mapping
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- patient1 คือ ROLE_PATIENT
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- doctor1 คือ ROLE_DOCTOR

-- 4. Doctor Profile Data (ผูกกับ user_id = 2)
INSERT INTO doctor (id, name, specialization, user_id) VALUES
(1, 'Dr. John Smith', 'Cardiology', 2);

-- 5. Patient Profile Data (ผูกกับ user_id = 1)
INSERT INTO patient (id, name, phone, user_id) VALUES
(1, 'Alice Patient', '0812345678', 1);

