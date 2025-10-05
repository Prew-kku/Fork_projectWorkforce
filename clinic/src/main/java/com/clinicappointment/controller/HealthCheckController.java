package com.clinicappointment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller นี้ถูกสร้างขึ้นมาเพื่อเป็น "จุดตรวจสุขภาพ" แบบเบาๆ (Lightweight)
 * สำหรับให้ Render เข้ามาตรวจสอบว่า Web Server (Tomcat) เริ่มทำงานแล้วหรือยัง
 * โดยจะไม่รอการเชื่อมต่อฐานข้อมูล ทำให้ตอบกลับได้รวดเร็วมาก
 */
@RestController
public class HealthCheckController {

    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheck() {
        // แค่ตอบกลับด้วยข้อความ "OK" และสถานะ 200
        // เพื่อบอก Render ว่า "ฉันพร้อมแล้ว!"
        return ResponseEntity.ok("OK");
    }
}
