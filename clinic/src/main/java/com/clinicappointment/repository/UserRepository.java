package com.clinicappointment.repository;

import com.clinicappointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    
    // *** เพิ่มเมธอดนี้เพื่อใช้ตรวจสอบอีเมลซ้ำ ***
    Optional<User> findByEmail(String email);
}

