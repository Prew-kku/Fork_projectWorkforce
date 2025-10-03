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
import org.springframework.web.bind.annotation.RequestMapping;

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
        // 1. ตรวจสอบสิทธิ์จาก HttpSession
        Long doctorId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        // ถ้ายังไม่ได้ล็อกอิน หรือไม่ใช่หมอ ให้กลับไปหน้า login
        if (doctorId == null || !"DOCTOR".equals(role)) {
            return "redirect:/login";
        }

        // 2. ค้นหาข้อมูลหมอจาก ID ที่เก็บไว้ใน Session
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));

        // 3. ค้นหารายการนัดหมายทั้งหมดของหมอคนนี้
        List<Appointment> appointmentsFromDb = appointmentRepository.findByDoctor(doctor);
        
        // 4. แปลงข้อมูลเป็น DTO สำหรับแสดงผล (เพื่อจัดรูปแบบวันที่)
        List<AppointmentView> appointmentViews = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (Appointment appt : appointmentsFromDb) {
            AppointmentView view = new AppointmentView();
            view.setId(appt.getId());
            view.setPatientName(appt.getPatient().getName());
            view.setStatus(appt.getStatus());
            if (appt.getAppointmentDate() != null) {
                view.setFormattedAppointmentDate(appt.getAppointmentDate().format(formatter));
            } else {
                view.setFormattedAppointmentDate("N/A");
            }
            appointmentViews.add(view);
        }

        // 5. ส่งข้อมูลไปแสดงผลที่หน้าเว็บ
        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("appointments", appointmentViews);
        return "doctor-dashboard";
    }
}

