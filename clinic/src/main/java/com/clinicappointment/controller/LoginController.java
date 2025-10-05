package com.clinicappointment.controller;

import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Patient;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {
    private final PatientRepository patientRepo;
    private final DoctorRepository doctorRepo;
    private final PasswordEncoder passwordEncoder;

    public LoginController(PatientRepository patientRepo, DoctorRepository doctorRepo, PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Username and password are required");
            return "login";
        }

        String trimmedUsername = username.trim();

        // 1. ลองค้นหาจาก Patient
        Optional<Patient> patientOpt = patientRepo.findByUser_Username(trimmedUsername);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            if (patient.getUser() != null &&
                passwordEncoder.matches(password, patient.getUser().getPassword())) {

                // === START: โค้ดที่เพิ่มเข้ามา ===
                // *** ตรวจสอบว่าบัญชีถูกเปิดใช้งานอยู่หรือไม่ ***
                if (patient.getUser().isEnabled()) {
                    // ถ้าเปิดใช้งานอยู่ ก็ให้ล็อกอินตามปกติ
                    session.setAttribute("userId", patient.getId());
                    session.setAttribute("username", patient.getUser().getUsername());
                    session.setAttribute("role", "PATIENT");
                    return "redirect:/patient/home";
                } else {
                    // ถ้าบัญชีถูกปิดใช้งาน ให้แสดงข้อความ Error
                    model.addAttribute("error", "This account has been deactivated.");
                    return "login";
                }
                // === END: โค้ดที่เพิ่มเข้ามา ===
            }
        }

        // 2. ลองค้นหาจาก Doctor
        Optional<Doctor> doctorOpt = doctorRepo.findByUser_Username(trimmedUsername);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            if (doctor.getUser() != null &&
                passwordEncoder.matches(password, doctor.getUser().getPassword())) {

                // === START: โค้ดที่เพิ่มเข้ามา ===
                // *** ตรวจสอบว่าบัญชีถูกเปิดใช้งานอยู่หรือไม่ ***
                if (doctor.getUser().isEnabled()) {
                    session.setAttribute("userId", doctor.getId());
                    session.setAttribute("username", doctor.getName());
                    session.setAttribute("role", "DOCTOR");
                    return "redirect:/doctor/home";
                } else {
                    // ถ้าบัญชีถูกปิดใช้งาน ให้แสดงข้อความ Error
                    model.addAttribute("error", "This account has been deactivated.");
                    return "login";
                }
                // === END: โค้ดที่เพิ่มเข้ามา ===
            }
        }

        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}