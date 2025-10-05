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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // === เพิ่ม import ===

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
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }
        
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        model.addAttribute("patient", patient);
        
        if (patient.isNameSet()) {
            List<Doctor> doctors = doctorRepository.findAll();
            model.addAttribute("doctors", doctors);
        }
        
        return "patient-home";
    }

    @PostMapping("/set-name")
    public String setName(@RequestParam String newName, HttpSession session) {
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        
        patient.setName(newName);
        patient.setNameSet(true);
        patientRepository.save(patient);

        return "redirect:/patient/home";
    }

    @PostMapping("/book")
    public String bookAppointment(@RequestParam Long doctorId,
                                  @RequestParam String datetime,
                                  @RequestParam String symptoms,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) { // === เพิ่ม RedirectAttributes ===
        Long patientId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (patientId == null || !"PATIENT".equals(role)) {
            return "redirect:/login";
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentDate(LocalDateTime.parse(datetime));
        appt.setStatus("BOOKED");
        appt.setSymptoms(symptoms);

        appointmentRepository.save(appt);
        
        // === เพิ่มข้อความตอบกลับ ===
        redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");

        return "redirect:/patient/appointments";
    }

    @GetMapping("/appointments")
    public String myAppointments(Model model, HttpSession session) {
        Long patientId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (patientId == null || !"PATIENT".equals(role)) {
            return "redirect:/login";
        }
        Patient patient = patientRepository.findById(patientId)
                 .orElseThrow(() -> new RuntimeException("Patient not found with ID: " + patientId));
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        model.addAttribute("appointments", appointments);
        return "appointment-list";
    }
    
    @PostMapping("/appointments/cancel")
    public String cancelAppointment(@RequestParam Long appointmentId, 
                                    HttpSession session, 
                                    RedirectAttributes redirectAttributes) { // === เพิ่ม RedirectAttributes ===
        Long patientId = (Long) session.getAttribute("userId");
        if (patientId == null) {
            return "redirect:/login";
        }

        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            if (appointment.getPatient().getId().equals(patientId)) {
                appointment.setStatus("CANCELLED");
                appointmentRepository.save(appointment);
                
                // === เพิ่มข้อความตอบกลับ ===
                redirectAttributes.addFlashAttribute("successMessage", "Appointment has been cancelled.");
            }
        });

        return "redirect:/patient/appointments";
    }
}
