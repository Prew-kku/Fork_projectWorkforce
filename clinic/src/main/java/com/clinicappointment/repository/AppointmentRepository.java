package com.clinicappointment.repository;
import com.clinicappointment.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	List<Appointment> findByDoctor(Doctor doctor);
	List<Appointment> findByPatient(Patient patient);
}
