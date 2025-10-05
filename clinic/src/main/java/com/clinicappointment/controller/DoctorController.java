package com.clinicappointment.controller;

import com.clinicappointment.dto.AppointmentView;
import com.clinicappointment.entity.Appointment;
import com.clinicappointment.entity.Doctor;
import com.clinicappointment.repository.AppointmentRepository;
import com.clinicappointment.repository.DoctorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // === เพิ่ม import ===

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorController(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Long doctorId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (doctorId == null || !"DOCTOR".equals(role)) {
            return "redirect:/login";
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        List<Appointment> appointmentsFromDb = appointmentRepository.findByDoctor(doctor);
        
        List<AppointmentView> appointmentViews = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (Appointment appt : appointmentsFromDb) {
            AppointmentView view = new AppointmentView();
            view.setId(appt.getId());
            view.setPatientName(appt.getPatient().getName());
            view.setStatus(appt.getStatus());
            view.setSymptoms(appt.getSymptoms());
            if (appt.getAppointmentDate() != null) {
                view.setFormattedAppointmentDate(appt.getAppointmentDate().format(formatter));
            } else {
                view.setFormattedAppointmentDate("N/A");
            }
            appointmentViews.add(view);
        }

        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("appointments", appointmentViews);
        return "doctor-dashboard";
    }

    @PostMapping("/update-status")
    public String updateAppointmentStatus(@RequestParam Long appointmentId, 
                                          HttpSession session, 
                                          RedirectAttributes redirectAttributes) { // === เพิ่ม RedirectAttributes ===
        Long doctorId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (doctorId == null || !"DOCTOR".equals(role)) {
            return "redirect:/login";
        }

        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            if (appointment.getDoctor().getId().equals(doctorId)) {
                appointment.setStatus("COMPLETED");
                appointmentRepository.save(appointment);
                
                // === เพิ่มข้อความตอบกลับ ===
                redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated to COMPLETED.");
            }
        });

        return "redirect:/doctor/home";
    }
    
    @PostMapping("/appointments/cancel")
    public String cancelAppointmentByDoctor(@RequestParam Long appointmentId, 
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) { // === เพิ่ม RedirectAttributes ===
        Long doctorId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (doctorId == null || !"DOCTOR".equals(role)) {
            return "redirect:/login";
        }

        appointmentRepository.findById(appointmentId).ifPresent(appointment -> {
            if (appointment.getDoctor().getId().equals(doctorId)) {
                appointment.setStatus("CANCELLED");
                appointmentRepository.save(appointment);
                
                // === เพิ่มข้อความตอบกลับ ===
                redirectAttributes.addFlashAttribute("successMessage", "Appointment has been cancelled.");
            }
        });

        return "redirect:/doctor/home";
    }
}
