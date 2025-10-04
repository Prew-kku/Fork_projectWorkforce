package com.clinicappointment.config;

import com.clinicappointment.entity.Doctor;
import com.clinicappointment.entity.Patient;
import com.clinicappointment.entity.Role;
import com.clinicappointment.entity.User;
import com.clinicappointment.repository.DoctorRepository;
import com.clinicappointment.repository.PatientRepository;
import com.clinicappointment.repository.RoleRepository;
import com.clinicappointment.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * คลาสนี้จะทำงานตอนที่แอปพลิเคชันเริ่มทำงาน
 * เพื่อตรวจสอบและสร้างข้อมูลเริ่มต้นที่จำเป็นทั้งหมด
 */
@Component
@Profile({"prod", "default"}) // ✅ เปลี่ยนตรงนี้ - ให้ทำงานทั้งใน prod และ default
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, 
                          DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("🔧 DataInitializer: Starting database initialization...");
        
        // --- 1. สร้าง Roles ---
        Role patientRole = createRoleIfNotFound("ROLE_PATIENT");
        Role doctorRole = createRoleIfNotFound("ROLE_DOCTOR");

        // --- 2. สร้างบัญชีหมอเริ่มต้น (ถ้ายังไม่มี) ---
        if (userRepository.findByUsername("doctor1").isEmpty()) {
            User doctorUser = new User();
            doctorUser.setUsername("doctor1");
            doctorUser.setPassword("1234");
            doctorUser.setEmail("doctor1@clinic.com");
            doctorUser.setRoles(Set.of(doctorRole));

            Doctor doctor = new Doctor();
            doctor.setName("Dr. John Smith");
            doctor.setSpecialization("Cardiology");
            doctor.setUser(doctorUser);

            doctorRepository.save(doctor);
            System.out.println("✅ Created default doctor: doctor1");
        }

        // --- 3. สร้างบัญชีคนไข้เริ่มต้น (ถ้ายังไม่มี) ---
        if (userRepository.findByUsername("patient1").isEmpty()) {
            User patientUser = new User();
            patientUser.setUsername("patient1");
            patientUser.setPassword("1234");
            patientUser.setEmail("patient1@clinic.com");
            patientUser.setRoles(Set.of(patientRole));

            Patient patient = new Patient();
            patient.setName("patient1");
            patient.setPhone("0812345678");
            patient.setNameSet(false);
            patient.setUser(patientUser);

            patientRepository.save(patient);
            System.out.println("✅ Created default patient: patient1");
        }
        
        System.out.println("🎉 DataInitializer: Database initialization completed!");
    }

    private Role createRoleIfNotFound(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role role = new Role();
            role.setName(name);
            Role savedRole = roleRepository.save(role);
            System.out.println("✅ Created role: " + name);
            return savedRole;
        });
    }
}