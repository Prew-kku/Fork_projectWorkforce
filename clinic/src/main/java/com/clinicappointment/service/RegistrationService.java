// โค้ดสำหรับแทนที่ไฟล์ RegistrationService.java ทั้งหมด
package com.clinicappointment.service;

import com.clinicappointment.entity.Patient;
import com.clinicappointment.entity.Role;
import com.clinicappointment.entity.User;
import com.clinicappointment.repository.PatientRepository;
import com.clinicappointment.repository.RoleRepository;
import com.clinicappointment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // 1. Import PasswordEncoder
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder; // 2. ประกาศตัวแปร PasswordEncoder

    // 3. เพิ่ม PasswordEncoder เข้าไปใน Constructor
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository,
                               PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder; // 4. กำหนดค่า
    }

    @Transactional
    public void registerPatient(String username, String password, String phone, String email) {
        // ตรวจสอบ Username ซ้ำ
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        // ตรวจสอบ Email ซ้ำ
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // ค้นหา Role ของ Patient
        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(patientRole);

        // สร้าง User object
        User user = new User();
        user.setUsername(username);
        // 5. เข้ารหัสรหัสผ่านก่อนตั้งค่า!
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(roles);

        // สร้าง Patient object และผูกกับ User
        Patient patient = new Patient();
        patient.setPhone(phone);
        patient.setUser(user);
        
        // เราไม่จำเป็นต้อง save user ก่อน เพราะ Patient ถูกตั้งค่า cascade = CascadeType.ALL
        // การ save Patient จะทำให้ User ถูก save ไปด้วยโดยอัตโนมัติ
        patientRepository.save(patient);
    }
}