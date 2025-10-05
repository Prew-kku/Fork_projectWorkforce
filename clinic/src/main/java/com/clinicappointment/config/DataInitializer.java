package com.clinicappointment.config;

import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Patient;
import com.clinicappointment.entity.Role;
import com.clinicappointment.entity.User;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import com.clinicappointment.repository.RoleRepository;
import com.clinicappointment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository; // *** เพิ่ม PatientRepository กลับเข้ามา ***

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository,
                           DoctorRepository doctorRepository, PasswordEncoder passwordEncoder,
                           PatientRepository patientRepository) { // *** เพิ่มใน Constructor ***
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.patientRepository = patientRepository; // *** เพิ่มการกำหนดค่า ***
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role patientRole = createRoleIfNotFound("ROLE_PATIENT");
        Role doctorRole = createRoleIfNotFound("ROLE_DOCTOR");

        // --- จัดการบัญชี Doctor ---
        createDoctorIfNotFound("doctor1", "1234", "Dr. John Smith", "General Practitioner", doctorRole);
        createDoctorIfNotFound("doctor2", "1234", "Dr. Emily Johnson", "Pediatrician", doctorRole);
        createDoctorIfNotFound("doctor3", "1234", "Dr. Michael Lee", "Internist", doctorRole);

        // === START: โค้ดที่เพิ่มเข้ามา ===
        // --- สร้างบัญชี Patient สำหรับทดสอบ ---
        createPatientIfNotFound("Test_patient", "1234", patientRole);
        // === END: โค้ดที่เพิ่มเข้ามา ===

        // --- จัดการบัญชี Patient ที่มีอยู่แล้ว ---
        // โค้ดนี้จะช่วย "ซ่อม" บัญชี patient ที่อาจมีรหัสผ่านเก่าอยู่ (ส่วนนี้ยังทำงานเหมือนเดิม)
        updatePasswordIfNotEncoded("patient1", "1234");
        updatePasswordIfNotEncoded("patient2", "1234");
        updatePasswordIfNotEncoded("Kren", "1234");
    }
    
    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(name);
                    return roleRepository.save(newRole);
                });
    }

    private void createDoctorIfNotFound(String username, String password, String fullName, String specialization, Role doctorRole) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User doctorUser = new User();
            doctorUser.setUsername(username);
            doctorUser.setPassword(passwordEncoder.encode(password));
            doctorUser.setEmail(username + "@clinic.com");
            doctorUser.setRoles(Set.of(doctorRole));

            Doctor doctor = new Doctor();
            doctor.setName(fullName);
            doctor.setSpecialization(specialization);
            doctor.setUser(doctorUser); 

            doctorRepository.save(doctor);
            System.out.println("Created initial DOCTOR account: " + fullName + " (" + username + ")");
        }
    }

    // === START: เมธอดใหม่สำหรับสร้างบัญชี Patient ===
    private void createPatientIfNotFound(String username, String password, Role patientRole) {
        if (userRepository.findByUsername(username).isEmpty()) {
            // 1. สร้าง User สำหรับคนไข้
            User patientUser = new User();
            patientUser.setUsername(username);
            patientUser.setPassword(passwordEncoder.encode(password));
            patientUser.setEmail(username + "@clinic.com");
            patientUser.setRoles(Set.of(patientRole));

            // 2. สร้าง Patient และผูกกับ User
            Patient patient = new Patient();
            patient.setPhone("000-000-0000"); // ใส่เบอร์โทรศัพท์ชั่วคราว
            // เรายังไม่ตั้งชื่อ (name) และ isNameSet จะเป็น false โดยอัตโนมัติ
            // เพื่อให้ flow การตั้งชื่อครั้งแรกยังทำงานได้เหมือนเดิม
            patient.setUser(patientUser);

            // 3. บันทึกข้อมูลคนไข้
            patientRepository.save(patient);
            System.out.println("Created initial TEST PATIENT account: " + username);
        }
    }
    // === END: เมธอดใหม่สำหรับสร้างบัญชี Patient ===

    private void updatePasswordIfNotEncoded(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.getPassword().startsWith("$2") && user.getPassword().equals(rawPassword)) {
                System.out.println("Updating plain text password for user: " + username);
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }
        }
    }
}

