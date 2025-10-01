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
        return "login"; // ไปหน้า login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          HttpSession session,
                          Model model) {

        // 1. ลองค้นหาผู้ใช้จากตาราง Patient
        Optional<Patient> patientOpt = patientRepo.findByUser_Username(username);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            // ตรวจสอบรหัสผ่านจาก User entity ที่เชื่อมกัน
            if (patient.getUser().getPassword().equals(password)) {
                session.setAttribute("userId", patient.getId());
                session.setAttribute("role", "PATIENT");
                return "redirect:/patient/home";
            }
        }

        // 2. ถ้าไม่เจอใน Patient, ลองค้นหาจากตาราง Doctor
        Optional<Doctor> doctorOpt = doctorRepo.findByUser_Username(username);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            // ตรวจสอบรหัสผ่านจาก User entity ที่เชื่อมกัน
            if (doctor.getUser().getPassword().equals(password)) {
                session.setAttribute("userId", doctor.getId());
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
        session.invalidate(); // ล้างข้อมูล session ทั้งหมด
        return "redirect:/login";
    }
}
