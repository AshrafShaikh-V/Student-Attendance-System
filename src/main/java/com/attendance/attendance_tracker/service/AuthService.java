package com.attendance.attendance_tracker.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.attendance_tracker.dto.LoginRequestDTO;
import com.attendance.attendance_tracker.dto.LoginResponseDTO;
import com.attendance.attendance_tracker.entity.Teacher;
import com.attendance.attendance_tracker.repository.TeacherRepository;
import com.attendance.attendance_tracker.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {
        Teacher teacher = teacherRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), teacher.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtService.issueToken(teacher);
        return LoginResponseDTO.builder()
            .accessToken(token)
            .tokenType("Bearer")
            .expiresInSeconds(jwtService.getTtlSeconds())
            .email(teacher.getEmail())
            .role(teacher.getRole())
            .build();
    }
}
