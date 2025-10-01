package com.clinicappointment.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;

    // --- ส่วนที่เพิ่มเข้ามา ---
    // เพิ่มความสัมพันธ์ OneToOne ไปยัง User entity
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private DoctorProfile profile;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public DoctorProfile getProfile() { return profile; }
    public void setProfile(DoctorProfile profile) { this.profile = profile; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    // *** เพิ่ม Getter/Setter สำหรับ User ที่ขาดไป ***
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

