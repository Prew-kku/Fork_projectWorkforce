package com.clinicappointment.repository;

import com.clinicappointment.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // เมธอดสำหรับค้นหา Role จากชื่อ (เช่น "ROLE_PATIENT")
    Optional<Role> findByName(String name);
}
