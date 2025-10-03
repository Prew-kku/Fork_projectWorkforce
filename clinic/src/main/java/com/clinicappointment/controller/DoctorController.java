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
        // 1. ✅ ตรวจสอบ Session อย่างละเอียด
        Long doctorId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (doctorId == null || !"DOCTOR".equals(role)) {
            session.invalidate(); // ✅ ล้าง session ที่ไม่ถูกต้อง
            return "redirect:/login";
        }

        // 2. ✅ ใช้ Optional pattern
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseGet(() -> {
                    session.invalidate();
                    return null;
                });

        if (doctor == null) {
            return "redirect:/login";
        }

        // 3. ค้นหารายการนัดหมาย
        List<Appointment> appointmentsFromDb = appointmentRepository.findByDoctor(doctor);
        
        // 4. แปลงเป็น DTO
        List<AppointmentView> appointmentViews = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        for (Appointment appt : appointmentsFromDb) {
            AppointmentView view = new AppointmentView();
            view.setId(appt.getId());
            
            // ✅ ป้องกัน NullPointerException
            if (appt.getPatient() != null) {
                view.setPatientName(appt.getPatient().getName());
            } else {
                view.setPatientName("Unknown Patient");
            }
            
            view.setStatus(appt.getStatus() != null ? appt.getStatus() : "N/A");
            
            if (appt.getAppointmentDate() != null) {
                view.setFormattedAppointmentDate(appt.getAppointmentDate().format(formatter));
            } else {
                view.setFormattedAppointmentDate("N/A");
            }
            
            appointmentViews.add(view);
        }

        // 5. ส่งข้อมูลไป View
        model.addAttribute("doctorName", doctor.getName());
        model.addAttribute("appointments", appointmentViews);
        return "doctor-dashboard";
    }
}