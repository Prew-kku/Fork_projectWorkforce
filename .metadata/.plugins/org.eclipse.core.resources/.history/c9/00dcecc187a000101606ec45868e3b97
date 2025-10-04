package com.clinicappointment.config;

import com.clinicappointment.entity.Role;
import com.clinicappointment.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * คลาสนี้จะทำงานตอนที่แอปพลิเคชันเริ่มทำงาน
 * เพื่อตรวจสอบและสร้างข้อมูลเริ่มต้นที่จำเป็น (เช่น Roles)
 */
@Component
@Profile("prod") // บอกให้ทำงานเฉพาะบน Render เท่านั้น
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ตรวจสอบและสร้าง ROLE_PATIENT อย่างปลอดภัย
        if (roleRepository.findByName("ROLE_PATIENT").isEmpty()) {
            Role patientRole = new Role();
            patientRole.setName("ROLE_PATIENT");
            roleRepository.save(patientRole);
        }

        // ตรวจสอบและสร้าง ROLE_DOCTOR อย่างปลอดภัย
        if (roleRepository.findByName("ROLE_DOCTOR").isEmpty()) {
            Role doctorRole = new Role();
            doctorRole.setName("ROLE_DOCTOR");
            roleRepository.save(doctorRole);
        }
    }
}
