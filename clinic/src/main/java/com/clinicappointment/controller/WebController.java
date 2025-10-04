package com.clinicappointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller นี้มีหน้าที่จัดการกับ URL หลักของเว็บไซต์
 */
@Controller
public class WebController {

    /**
     * เมธอดนี้จะทำงานเมื่อมีคนเข้ามาที่ URL ราก (/) ของเว็บ
     * และจะแสดงหน้า Home หลักของเว็บไซต์ (home.html)
     * @return ชื่อของ view ที่จะแสดงผล
     */
    @GetMapping("/")
    public String showHomePage() {
        return "home"; // ชี้ไปที่ home.html
    }
}

