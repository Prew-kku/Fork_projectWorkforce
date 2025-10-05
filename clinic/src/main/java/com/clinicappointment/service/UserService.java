package com.clinicappointment.service;

import com.clinicappointment.entity.User;
import com.clinicappointment.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        // 1. ค้นหา User จาก ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // 2. *** ตรวจสอบรหัสผ่านเก่า ***
        // ใช้ passwordEncoder.matches() เพื่อเปรียบเทียบรหัสผ่านที่ผู้ใช้กรอกกับรหัสที่ถูกเข้ารหัสไว้ใน DB
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password.");
        }

        // 3. เข้ารหัสรหัสผ่านใหม่และบันทึก
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
