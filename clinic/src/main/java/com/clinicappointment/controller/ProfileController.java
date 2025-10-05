package com.clinicappointment.controller;

import com.clinicappointment.entity.Patient;
import com.clinicappointment.service.UserService; // *** Import UserService ที่เราสร้าง ***
import com.clinicappointment.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile") // *** จัดกลุ่ม URL ที่เกี่ยวกับโปรไฟล์ไว้ที่นี่ ***
public class ProfileController {

    private final PatientRepository patientRepository;
    private final UserService userService;

    public ProfileController(PatientRepository patientRepository, UserService userService) {
        this.patientRepository = patientRepository;
        this.userService = userService;
    }

    // --- เมธอดสำหรับแสดงหน้าโปรไฟล์ ---
    @GetMapping("/patient")
    public String showPatientProfile(HttpSession session, Model model) {
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        model.addAttribute("patient", patient);
        return "patient-profile"; // *** ชี้ไปที่ไฟล์ HTML ใหม่ที่เราจะสร้าง ***
    }

    // --- เมธอดสำหรับอัปเดตข้อมูลส่วนตัว (ชื่อ, เบอร์โทร) ---
    @PostMapping("/patient/update")
    public String updatePatientProfile(@RequestParam String name, @RequestParam String phone,
                                       HttpSession session, RedirectAttributes redirectAttributes) {
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        patient.setName(name);
        patient.setPhone(phone);
        patientRepository.save(patient);

        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/profile/patient";
    }

    // --- เมธอดสำหรับเปลี่ยนรหัสผ่าน ---
    @PostMapping("/password/change")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                 @RequestParam String confirmPassword, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match.");
            return "redirect:/profile/patient";
        }

        try {
            // ดึง User ID จาก Patient object เพื่อส่งไปให้ UserService
            Long userId = patientRepository.findById(patientId).get().getUser().getId();
            userService.changePassword(userId, oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            // ดักจับ Error ที่ส่งมาจาก UserService (เช่น รหัสผ่านเก่าผิด)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/profile/patient";
    }
}
