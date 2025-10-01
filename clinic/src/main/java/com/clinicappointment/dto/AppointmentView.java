package com.clinicappointment.dto;

/**
 * นี่คือคลาสสำหรับส่งข้อมูล Appointment ไปยังหน้าเว็บ (View) โดยเฉพาะ
 * เราจะแปลงข้อมูลที่ซับซ้อน เช่น วันที่ ให้เป็น String ที่พร้อมแสดงผลได้ทันที
 */
public class AppointmentView {

    private Long id;
    private String patientName;
    private String formattedAppointmentDate; // วันที่จะถูกเก็บเป็น String ที่จัดรูปแบบแล้ว
    private String status;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public String getFormattedAppointmentDate() {
        return formattedAppointmentDate;
    }
    public void setFormattedAppointmentDate(String formattedAppointmentDate) {
        this.formattedAppointmentDate = formattedAppointmentDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}