# --- ขั้นตอนที่ 1: สร้าง (Build) โปรเจกต์ ---
# เริ่มต้นด้วยสภาพแวดล้อมที่มี Maven และ Java 17 ติดตั้งอยู่แล้ว
FROM maven:3.8.5-openjdk-17 AS build

# สร้างโฟลเดอร์ทำงานชื่อ /app
WORKDIR /app

# คัดลอกไฟล์ทั้งหมดจากโปรเจกต์ของคุณเข้ามาใน /app
COPY . .

# รันคำสั่ง Maven เพื่อสร้างไฟล์ .jar (ข้ามการ Test เพื่อความรวดเร็ว)
RUN mvn clean package -DskipTests

# --- ขั้นตอนที่ 2: รัน (Run) โปรเจกต์ ---
# เริ่มต้นใหม่ด้วยสภาพแวดล้อม Java 17 ที่มีขนาดเล็ก (เหมาะสำหรับใช้งานจริง)
FROM openjdk:17-jdk-slim

# คัดลอกเฉพาะไฟล์ .jar ที่สร้างเสร็จแล้วจากขั้นตอน Build มาไว้ใน image ใหม่
# โดยตั้งชื่อไฟล์ใหม่เป็น app.jar
COPY --from=build /app/target/*.jar app.jar

# บอกให้ Container รู้ว่าแอปพลิเคชันของเราจะทำงานที่พอร์ต 8080
EXPOSE 8080

# คำสั่งที่จะรันเมื่อ Container เริ่มทำงาน
ENTRYPOINT ["java", "-jar", "app.jar"]
