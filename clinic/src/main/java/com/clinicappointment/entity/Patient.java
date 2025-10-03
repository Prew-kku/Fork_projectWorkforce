package com.clinicappointment.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    
    // --- ส่วนที่เพิ่มเข้ามา ---
    private boolean nameSet = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }

    // *** เพิ่ม Getter/Setter สำหรับ nameSet ***
    public boolean isNameSet() { return nameSet; }
    public void setNameSet(boolean nameSet) { this.nameSet = nameSet; }
}

