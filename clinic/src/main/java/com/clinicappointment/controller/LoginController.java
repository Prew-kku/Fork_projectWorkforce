package com.clinicappointment.controller;

import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Patient;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
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

    public LoginController(PatientRepository patientRepo, DoctorRepository doctorRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
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

        // ✅ ตรวจสอบ input ว่างเปล่า
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Username and password are required");
            return "login";
        }

        // 1. ลองค้นหาผู้ใช้จากตาราง Patient
        Optional<Patient> patientOpt = patientRepo.findByUser_Username(username.trim());
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            // ✅ ตรวจสอบว่า User object ไม่เป็น null
            if (patient.getUser() != null && 
                patient.getUser().getPassword().equals(password)) {
                session.setAttribute("userId", patient.getId());
                session.setAttribute("username", patient.getName());
                session.setAttribute("role", "PATIENT");
                return "redirect:/patient/home";
            }
        }

        // 2. ถ้าไม่เจอใน Patient, ลองค้นหาจากตาราง Doctor
        Optional<Doctor> doctorOpt = doctorRepo.findByUser_Username(username.trim());
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            // ✅ ตรวจสอบว่า User object ไม่เป็น null
            if (doctor.getUser() != null && 
                doctor.getUser().getPassword().equals(password)) {
                session.setAttribute("userId", doctor.getId());
                session.setAttribute("username", doctor.getName());
                session.setAttribute("role", "DOCTOR");
                return "redirect:/doctor/home";
            }
        }

        // 3. ถ้าไม่เจอใครเลย หรือรหัสผ่านผิด
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}