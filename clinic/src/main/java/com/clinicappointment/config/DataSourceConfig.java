package com.clinicappointment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("prod") // บอกให้คลาสนี้ทำงานเฉพาะตอนที่ Profile เป็น "prod" (บน Render) เท่านั้น
public class DataSourceConfig {

    // ดึงค่า JDBC_DATABASE_URL จาก Environment Variable ของ Render
    @Value("${JDBC_DATABASE_URL}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        // 1. แปลง URL string ของ Render ให้เป็น URI object ที่สามารถแกะข้อมูลได้
        URI dbUri = new URI(databaseUrl);

        // --- ส่วนที่ปรับปรุงให้มีความเสถียรมากขึ้น ---
        // 2. ดึง username และ password ออกมาอย่างปลอดภัย
        //    เพื่อป้องกันปัญหาในกรณีที่รหัสผ่านมีอักขระพิเศษ เช่น ':' ปนมาด้วย
        String userInfo = dbUri.getUserInfo();
        String username = null;
        String password = null;
        if (userInfo != null && !userInfo.isEmpty()) {
            int delimiter = userInfo.indexOf(':');
            if (delimiter > 0) {
                username = userInfo.substring(0, delimiter);
                password = userInfo.substring(delimiter + 1);
            } else {
                username = userInfo; // รองรับกรณีที่มีแต่ username ไม่มี password
            }
        }

        // 3. สร้าง JDBC URL ที่ถูกต้องสำหรับ Java โดยไม่มี username/password ปนอยู่
        String port = "";
        if (dbUri.getPort() != -1) {
            port = ":" + dbUri.getPort();
        }
        String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + port + dbUri.getPath();

        // 4. สร้าง DataSource โดยป้อนข้อมูลแต่ละส่วนให้ชัดเจน
        return DataSourceBuilder.create()
                .url(jdbcUrl)       // บอก URL ที่ถูกต้อง
                .username(username)   // บอก Username
                .password(password)   // บอก Password
                .build();
    }
}

