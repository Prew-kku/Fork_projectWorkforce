package com.clinicappointment.service;

import com.clinicappointment.entity.Patient;
import com.clinicappointment.entity.Role;
import com.clinicappointment.entity.User;
import com.clinicappointment.repository.PatientRepository;
import com.clinicappointment.repository.RoleRepository;
import com.clinicappointment.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;

    public RegistrationService(UserRepository userRepository, PatientRepository patientRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void registerPatient(String username, String password, String phone, String email) {
        // 1. ตรวจสอบว่า username ซ้ำหรือไม่
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists!");
        }

        // *** เพิ่ม: ตรวจสอบว่า email ซ้ำหรือไม่ ***
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email is already in use!");
        }
        
        // 2. ค้นหา Role "ROLE_PATIENT"
        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("Error: Role 'ROLE_PATIENT' is not found in the database."));

        // 3. สร้าง User object ใหม่
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRoles(new HashSet<>(Collections.singletonList(patientRole)));

        // 4. สร้าง Patient object ใหม่
        Patient patient = new Patient();
        patient.setName(username);
        patient.setPhone(phone);
        
        // 5. เชื่อมโยง Patient กับ User
        patient.setUser(user);

        // 6. บันทึกข้อมูล Patient
        patientRepository.save(patient);
    }
}

