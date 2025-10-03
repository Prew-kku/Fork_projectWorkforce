package com.clinicappointment.repository;

import com.clinicappointment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // ✅ เปลี่ยนจาก User เป็น Optional<User>
    Optional<User> findByUsername(String username);
}