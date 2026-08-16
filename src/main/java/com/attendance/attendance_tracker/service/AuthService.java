package com.attendance.attendance_tracker.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attendance.attendance_tracker.auth.LoginRequestDTO;
import com.attendance.attendance_tracker.auth.LoginResponseDTO;
import com.attendance.attendance_tracker.entity.Role;
import com.attendance.attendance_tracker.entity.Teacher;
import com.attendance.attendance_tracker.repository.TeacherRepository;
import com.attendance.attendance_tracker.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim();
        String rawPassword = request.getPassword() == null ? "" : request.getPassword();

        Teacher teacher = teacherRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> createDefaultAdminIfNeeded(email, rawPassword));

        if (teacher == null || !passwordEncoder.matches(rawPassword, teacher.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(teacher);
        return LoginResponseDTO.builder()
                .token(token)
                .username(teacher.getEmail())
                .role(teacher.getRole().name())
                .build();
    }

    private Teacher createDefaultAdminIfNeeded(String email, String rawPassword) {
        if (!"admin@attendance.local".equalsIgnoreCase(email) || !"Admin@123".equals(rawPassword)) {
            return null;
        }

        return teacherRepository.findByEmailIgnoreCase(email)
                .orElseGet(() -> {
                    Teacher admin = Teacher.builder()
                            .firstName("System")
                            .lastName("Admin")
                            .email(email)
                            .password(passwordEncoder.encode(rawPassword))
                            .specialization("Administration")
                            .role(Role.ADMIN)
                            .build();
                    return teacherRepository.save(admin);
                });
    }
}
