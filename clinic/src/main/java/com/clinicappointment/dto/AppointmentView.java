package com.clinicappointment.dto;

public class AppointmentView {

    private Long id;
    private String patientName;
    private String formattedAppointmentDate;
    private String status;
    private String symptoms; // *** เพิ่ม Field ***

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getFormattedAppointmentDate() { return formattedAppointmentDate; }
    public void setFormattedAppointmentDate(String formattedAppointmentDate) { this.formattedAppointmentDate = formattedAppointmentDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    // *** เพิ่ม Getter/Setter ***
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
}

