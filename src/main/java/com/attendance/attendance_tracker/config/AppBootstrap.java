package com.attendance.attendance_tracker.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.attendance.attendance_tracker.entity.Role;
import com.attendance.attendance_tracker.entity.Teacher;
import com.attendance.attendance_tracker.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppBootstrap {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin-email:admin@attendance.local}")
    private String adminEmail;

    @Value("${app.bootstrap.admin-password:Admin@123}")
    private String adminPassword;

    @PostConstruct
    public void initializeAdmin() {
        if (adminEmail == null || adminEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
            return;
        }

        Teacher admin = teacherRepository.findByEmailIgnoreCase(adminEmail)
                .orElseGet(() -> Teacher.builder()
                        .firstName("System")
                        .lastName("Admin")
                        .email(adminEmail)
                        .role(Role.ADMIN)
                        .specialization("Administration")
                        .build());

        admin.setEmail(adminEmail);
        admin.setRole(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setFirstName(admin.getFirstName() == null ? "System" : admin.getFirstName());
        admin.setLastName(admin.getLastName() == null ? "Admin" : admin.getLastName());
        admin.setSpecialization(admin.getSpecialization() == null ? "Administration" : admin.getSpecialization());

        teacherRepository.save(admin);
    }
}
