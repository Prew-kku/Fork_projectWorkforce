package com.clinicappointment.controller;

import com.clinicappointment.entity.Appointment;
import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Patient;
import com.clinicappointment.repository.AppointmentRepository;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public PatientController(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // ตรวจสอบว่าผู้ใช้ล็อกอินในฐานะ Patient หรือไม่
        if (!"PATIENT".equals(session.getAttribute("role"))) {
            return "redirect:/login";
        }
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "patient-home";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long doctorId,
                                  @RequestParam String datetime,
                                  HttpSession session) {
        
        // ดึง patientId และ role จาก session
        Long patientId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // ตรวจสอบสิทธิ์ ถ้าไม่มี session หรือไม่ใช่ Patient ให้กลับไปหน้า login
        if (patientId == null || !"PATIENT".equals(role)) {
            return "redirect:/login";
        }
        
        // ค้นหา Patient จาก ID ที่ได้จาก session
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found for ID: " + patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentDate(LocalDateTime.parse(datetime));
        appt.setStatus("BOOKED");

        appointmentRepository.save(appt);

        return "redirect:/patient/appointments";
    }

    @GetMapping("/appointments")
    public String myAppointments(Model model, HttpSession session) {
        // ดึง patientId และ role จาก session
        Long patientId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // ตรวจสอบสิทธิ์
        if (patientId == null || !"PATIENT".equals(role)) {
            return "redirect:/login";
        }
        
        // ค้นหา Patient จาก ID ที่ได้จาก session
        Patient patient = patientRepository.findById(patientId)
                 .orElseThrow(() -> new RuntimeException("Patient not found for ID: " + patientId));
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        model.addAttribute("appointments", appointments);
        return "appointment-list";
    }
}

