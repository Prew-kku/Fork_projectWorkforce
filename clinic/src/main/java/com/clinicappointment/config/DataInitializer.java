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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository; // *** เพิ่ม PatientRepository ***
    private final PasswordEncoder passwordEncoder;

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
        if (userRepository.findByUsername("doctor1").isEmpty()) {
            // ถ้ายังไม่มี ให้สร้างใหม่พร้อมรหัสผ่านที่เข้ารหัสแล้ว
            createDoctorAccount("doctor1", "1234", doctorRole);
        } else {
            // ถ้ามีอยู่แล้ว ให้ตรวจสอบและอัปเดตรหัสผ่านถ้ายังเป็น Plain Text
            updatePasswordIfNotEncoded("doctor1", "1234");
        }

        // --- จัดการบัญชี Patient เริ่มต้น ---
        // โค้ดนี้จะช่วย "ซ่อม" บัญชี patient ที่อาจมีรหัสผ่านเก่าอยู่
        updatePasswordIfNotEncoded("patient1", "1234");
        updatePasswordIfNotEncoded("patient2", "1234");
        // คุณสามารถเพิ่มบรรทัดสำหรับ patient คนอื่นๆ ที่มีปัญหาได้ที่นี่
    }
    
    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(name);
                    return roleRepository.save(newRole);
                });
    }

    // *** เมธอดใหม่สำหรับสร้างบัญชีหมอ ***
    private void createDoctorAccount(String username, String password, Role doctorRole) {
        User doctorUser = new User();
        doctorUser.setUsername(username);
        doctorUser.setPassword(passwordEncoder.encode(password));
        doctorUser.setEmail(username + "@clinic.com");
        doctorUser.setRoles(Set.of(doctorRole));

        Doctor doctor = new Doctor();
        doctor.setName("Dr. John Smith");
        doctor.setSpecialization("Cardiology");
        doctor.setUser(doctorUser); 

        doctorRepository.save(doctor);
        System.out.println("Created initial DOCTOR account: " + username);
    }

    // *** เมธอดใหม่สำหรับตรวจสอบและอัปเดตรหัสผ่าน ***
    private void updatePasswordIfNotEncoded(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // ตรวจสอบว่ารหัสผ่านยังไม่ได้ถูกเข้ารหัส (BCrypt hash จะขึ้นต้นด้วย $2a$, $2b$, หรือ $2y$)
            // หรือตรวจสอบว่าเป็นรหัสผ่านเก่า "1234" หรือไม่
            if (!user.getPassword().startsWith("$2") && user.getPassword().equals(rawPassword)) {
                System.out.println("Updating plain text password for user: " + username);
                user.setPassword(passwordEncoder.encode(rawPassword));
                userRepository.save(user);
            }
        }
    }
}

