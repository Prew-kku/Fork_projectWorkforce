package com.clinicappointment.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(unique = true)
    private String email;

    // === START: โค้ดที่เพิ่มเข้ามา ===
    // เพิ่มคอลัมน์สำหรับเก็บสถานะการเปิดใช้งานบัญชี
    // กำหนดค่าเริ่มต้นเป็น true เพื่อให้บัญชีที่สร้างใหม่ทั้งหมดใช้งานได้ทันที
    private boolean enabled = true;
    // === END: โค้ดที่เพิ่มเข้ามา ===

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    // === START: Getter & Setter ที่เพิ่มเข้ามา ===
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    // === END: Getter & Setter ที่เพิ่มเข้ามา ===
}
