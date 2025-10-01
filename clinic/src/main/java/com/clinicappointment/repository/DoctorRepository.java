package com.clinicappointment.repository;

import com.clinicappointment.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // เพิ่มเมธอดนี้เพื่อให้ Spring Data JPA สร้าง query ค้นหา Doctor
    // จาก username ที่อยู่ใน User entity ที่เชื่อมกันอยู่
    Optional<Doctor> findByUser_Username(String username);
}

