/*package com.clinicappointment.controller;

import com.clinicappointment.entity.Appointment;
import com.clinicappointment.repository.AppointmentRepository;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class AppointmentController {
    private final AppointmentRepository appointmentRepo;
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;

    public AppointmentController(AppointmentRepository appointmentRepo,
                                 DoctorRepository doctorRepo,
                                 PatientRepository patientRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentRepo.findAll());
        return "appointment-list";
    }

    @PostMapping("/appointments")
    public String bookAppointment(@RequestParam Long doctorId,
                                  @RequestParam Long patientId,
                                  @RequestParam String datetime) {
        Appointment appt = new Appointment();
        appt.setDoctor(doctorRepo.findById(doctorId).orElse(null));
        appt.setPatient(patientRepo.findById(patientId).orElse(null));
        appt.setAppointmentDate(LocalDateTime.parse(datetime));
        appt.setStatus("Pending");
        appointmentRepo.save(appt);
        return "redirect:/appointments";
    }
}*/
