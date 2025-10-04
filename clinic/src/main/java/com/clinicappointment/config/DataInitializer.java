package com.clinicappointment.config;

import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Role;
import com.clinicappointment.entity.User;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.RoleRepository;
import com.clinicappointment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // <-- 1. Import Transactional

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
                           DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // <-- 2. เพิ่ม Annotation นี้
    public void run(String... args) throws Exception {
        // --- 1. สร้าง Roles พื้นฐาน (PATIENT, DOCTOR) ถ้ายังไม่มี ---
        Role patientRole = createRoleIfNotFound("ROLE_PATIENT");
        Role doctorRole = createRoleIfNotFound("ROLE_DOCTOR");

        // --- 2. สร้าง Doctor Account (doctor1) ถ้ายังไม่มี ---
        if (userRepository.findByUsername("doctor1").isEmpty()) {
            User doctorUser = new User();
            doctorUser.setUsername("doctor1");
            doctorUser.setPassword(passwordEncoder.encode("1234"));
            doctorUser.setEmail("doctor1@clinic.com");

            Set<Role> roles = new HashSet<>();
            roles.add(doctorRole);
            doctorUser.setRoles(roles);

            // ไม่ต้อง save User ก่อน เพราะเราใช้ Cascade
            // userRepository.save(doctorUser); 

            Doctor doctor = new Doctor();
            doctor.setName("Dr. John Smith");
            doctor.setSpecialization("Cardiology");
            doctor.setUser(doctorUser); 

            doctorRepository.save(doctor);
            System.out.println("Created initial DOCTOR account: doctor1");
        }
    }
    
    // ไม่ต้องใส่ @Transactional ที่เมธอด private นี้แล้ว เพราะเมธอด run คลุมทั้งหมด
    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(name);
                    return roleRepository.save(newRole);
                });
    }
}