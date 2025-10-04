package com.clinicappointment.config;

import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Patient;
import com.clinicappointment.entity.Role;
import com.clinicappointment.entity.User;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import com.clinicappointment.repository.RoleRepository;
import com.clinicappointment.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * คลาสนี้จะทำงานตอนที่แอปพลิเคชันเริ่มทำงานในโหมด "prod" (บน Render)
 * เพื่อตรวจสอบและสร้างข้อมูลเริ่มต้นที่จำเป็นทั้งหมด
 */
@Component
@Profile("prod")
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    // แก้ไข Constructor เพื่อรับ Repository ที่จำเป็นทั้งหมด
    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional // ทำให้การทำงานทั้งหมดนี้เป็นหนึ่งเดียว ถ้าพลาดจะย้อนกลับทั้งหมด
    public void run(ApplicationArguments args) throws Exception {
        // --- 1. สร้าง Roles ---
        Role patientRole = createRoleIfNotFound("ROLE_PATIENT");
        Role doctorRole = createRoleIfNotFound("ROLE_DOCTOR");

        // --- 2. สร้างบัญชีหมอเริ่มต้น (ถ้ายังไม่มี) ---
        if (userRepository.findByUsername("doctor1").isEmpty()) {
            User doctorUser = new User();
            doctorUser.setUsername("doctor1");
            doctorUser.setPassword("1234"); // รหัสผ่านแบบ Plain text ตามโปรเจกต์ปัจจุบัน
            doctorUser.setEmail("doctor1@clinic.com");
            doctorUser.setRoles(Set.of(doctorRole));

            Doctor doctor = new Doctor();
            doctor.setName("Dr. John Smith");
            doctor.setSpecialization("Cardiology");
            doctor.setUser(doctorUser);

            doctorRepository.save(doctor);
        }

        // --- 3. สร้างบัญชีคนไข้เริ่มต้น (ถ้ายังไม่มี) ---
        if (userRepository.findByUsername("patient1").isEmpty()) {
            User patientUser = new User();
            patientUser.setUsername("Test");
            patientUser.setPassword("1234");
            patientUser.setEmail("patient1@clinic.com");
            patientUser.setRoles(Set.of(patientRole));

            Patient patient = new Patient();
            patient.setName("Test"); // ชื่อเริ่มต้น (คนไข้ต้องไปตั้งชื่อเองทีหลัง)
            patient.setPhone("0812345678");
            patient.setNameSet(false); // ตั้งค่าให้ยังไม่ได้ตั้งชื่อ
            patient.setUser(patientUser);

            patientRepository.save(patient);
        }
    }

    // เมธอดเสริมเพื่อช่วยให้โค้ดไม่ซ้ำซ้อน
    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            return roleRepository.save(role);
        });
    }
}

