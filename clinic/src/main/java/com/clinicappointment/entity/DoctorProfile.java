package com.clinicappointment.entity;

import jakarta.persistence.*;

@Entity
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseNumber;
    private String biography;

    @OneToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
}
