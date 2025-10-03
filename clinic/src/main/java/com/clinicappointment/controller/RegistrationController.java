package com.clinicappointment.controller;

import com.clinicappointment.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String phone,
                                      @RequestParam String email,
                                      Model model) {
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        try {
            registrationService.registerPatient(username, password, phone, email);
        } catch (Exception e) { // *** แก้ไข: ดักจับ Exception ทั้งหมด ***
            // เพื่อให้สามารถแสดงข้อความ Error ได้ทุกกรณี (เช่น username ซ้ำ, email ซ้ำ, หรือหา role ไม่เจอ)
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        
        return "redirect:/login?success";
    }
}

