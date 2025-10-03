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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    public PatientController(DoctorRepository doctorRepository, 
                           AppointmentRepository appointmentRepository, 
                           PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // ✅ ตรวจสอบ Session
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
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        
        // ✅ ตรวจสอบ Session
        Long patientId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (patientId == null || !"PATIENT".equals(role)) {
            return "redirect:/login";
        }
        
        // ✅ ตรวจสอบ input
        if (doctorId == null || datetime == null || datetime.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid input data");
            return "redirect:/patient/home";
        }
        
        try {
            // ✅ ใช้ Optional pattern
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));

            Doctor doctor = doctorRepository.findById(doctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));

            // ✅ ตรวจสอบวันที่
            LocalDateTime appointmentDateTime = LocalDateTime.parse(datetime);
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                redirectAttributes.addFlashAttribute("error", 
                    "Cannot book appointment in the past");
                return "redirect:/patient/home";
            }

            Appointment appt = new Appointment();
            appt.setDoctor(doctor);
            appt.setPatient(patient);
            appt.setAppointmentDate(appointmentDateTime);
            appt.setStatus("BOOKED");

            appointmentRepository.save(appt);
            
            redirectAttributes.addFlashAttribute("success", 
                "Appointment booked successfully!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                "Failed to book appointment: " + e.getMessage());
            return "redirect:/patient/home";
        }

        return "redirect:/patient/appointments";
    }

    @GetMapping("/appointments")
    public String myAppointments(Model model, HttpSession session) {
        // ✅ ตรวจสอบ Session
        Long patientId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (patientId == null || !"PATIENT".equals(role)) {
            return "redirect:/login";
        }
        
        // ✅ ใช้ Optional pattern
        Patient patient = patientRepository.findById(patientId)
                .orElseGet(() -> {
                    session.invalidate();
                    return null;
                });

        if (patient == null) {
            return "redirect:/login";
        }

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);
        model.addAttribute("appointments", appointments);
        return "appointment-list";
    }
}