package com.clinicappointment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller นี้มีหน้าที่จัดการกับ URL หลักของเว็บไซต์
 */
@Controller
public class WebController {

    /**
     * เมธอดนี้จะทำงานเมื่อมีคนเข้ามาที่ URL ราก (root) ของเว็บ (เช่น http://localhost:8080/)
     * และจะทำการ redirect ผู้ใช้ไปยังหน้า login ทันที
     * @return คำสั่งให้ redirect ไปที่ /login
     */
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
}

