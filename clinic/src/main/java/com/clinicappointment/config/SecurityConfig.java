// โค้ดสำหรับแทนที่ไฟล์ SecurityConfig.java ทั้งหมด
package com.clinicappointment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. ปิดการป้องกัน CSRF (สำหรับโปรเจคนี้ที่จัดการ session เอง)
            // ในระบบจริงที่ซับซ้อน ควรเปิดและจัดการ token ให้ถูกต้อง
            .csrf(csrf -> csrf.disable())

            // 2. กำหนดสิทธิ์การเข้าถึง URL ต่างๆ
            .authorizeHttpRequests(auth -> auth
                // อนุญาตให้ทุกคนเข้าถึง URL เหล่านี้ได้
                .requestMatchers(
                    "/",
                    "/login",
                    "/logout",
                    "/register",
                    "/h2-console/**" // อนุญาตให้เข้าถึง H2 Console
                ).permitAll()
                // URL อื่นๆ ทั้งหมด ต้องมีการยืนยันตัวตน (ซึ่งเราทำเองใน Controller)
                .anyRequest().permitAll() // ตั้งเป็น permitAll เพื่อให้โค้ดใน Controller จัดการ session เอง
            )
            
            // 3. ปิดการใช้ Form Login ของ Spring Security
            // เพื่อให้ Controller ของเราทำงานได้เต็มที่
            .formLogin(form -> form.disable())
            
            // 4. ตั้งค่าเพิ่มเติมสำหรับ H2 Console (เพื่อให้แสดงผลได้)
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}