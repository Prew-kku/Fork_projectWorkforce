package com.clinicappointment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod") // บอกให้คลาสนี้ทำงานเฉพาะตอนที่ Profile เป็น "prod" (บน Render) เท่านั้น
public class DataSourceConfig {

    // ดึงค่า JDBC_DATABASE_URL จาก Environment Variable ของ Render
    @Value("${JDBC_DATABASE_URL}")
    private String databaseUrl;

    @Bean
    public DataSource dataSource() {
        // นี่คือส่วนที่สำคัญที่สุด:
        // เราจะแก้ไข URL ที่ได้จาก Render โดยการแทนที่ "postgres://"
        // ด้วย "jdbc:postgresql://" ซึ่งเป็นรูปแบบที่ไดรเวอร์ Java ต้องการ
        String correctedUrl = databaseUrl.replace("postgres://", "jdbc:postgresql://");

        return DataSourceBuilder.create()
                .url(correctedUrl) // ใช้ URL ที่แก้ไขแล้ว
                .build();
    }
}
