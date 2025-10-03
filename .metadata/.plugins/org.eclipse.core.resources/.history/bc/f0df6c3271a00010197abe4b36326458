-- ล้างข้อมูลเก่าก่อน
DELETE FROM user_roles;
DELETE FROM patient;
DELETE FROM doctor_profile;
DELETE FROM doctor;
DELETE FROM users;
DELETE FROM roles;

-- 1. Roles (ให้ฐานข้อมูลจัดการ ID เอง)
INSERT INTO roles (name) VALUES ('ROLE_PATIENT'), ('ROLE_DOCTOR');

-- 2. Users (ให้ฐานข้อมูลจัดการ ID เอง และเพิ่ม email)
INSERT INTO users (username, password, email) VALUES 
('patient1', '1234', 'patient1@example.com'),
('doctor1', '1234', 'doctor1@example.com');

-- 3. User-Role Mapping (ใช้ ID ที่ฐานข้อมูลสร้างขึ้น)
-- สมมติว่าฐานข้อมูลจะสร้าง ID 1=PATIENT, 2=DOCTOR
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1), (2, 2);

-- 4. Doctor Profile (ใช้ user_id ที่ถูกต้อง)
INSERT INTO doctor (name, specialization, user_id) VALUES ('Dr. John Smith', 'Cardiology', 2);

-- 5. Patient Profile (ปรับปรุงใหม่!)
-- เพิ่มค่า name_set เป็น false สำหรับผู้ใช้เริ่มต้น
INSERT INTO patient (name, phone, user_id, name_set) VALUES 
('patient1', '0812345678', 1, FALSE);

