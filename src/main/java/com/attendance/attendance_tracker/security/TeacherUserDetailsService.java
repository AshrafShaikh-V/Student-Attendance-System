package com.attendance.attendance_tracker.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attendance.attendance_tracker.entity.Role;
import com.attendance.attendance_tracker.entity.Teacher;
import com.attendance.attendance_tracker.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherUserDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Teacher teacher = teacherRepository.findByEmailIgnoreCase(username)
                .orElseGet(() -> createDefaultAdminIfNeeded(username));

        if (teacher == null) {
            throw new UsernameNotFoundException("Teacher not found: " + username);
        }

        String authority = "ROLE_" + teacher.getRole().name();

        return User.withUsername(teacher.getEmail())
                .password(teacher.getPassword())
                .authorities(authority)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    private Teacher createDefaultAdminIfNeeded(String username) {
        if (!"admin@attendance.local".equalsIgnoreCase(username)) {
            return null;
        }

        return teacherRepository.findByEmailIgnoreCase(username)
                .orElseGet(() -> {
                    Teacher admin = Teacher.builder()
                            .firstName("System")
                            .lastName("Admin")
                            .email(username)
                            .password(passwordEncoder.encode("Admin@123"))
                            .specialization("Administration")
                            .role(Role.ADMIN)
                            .build();
                    return teacherRepository.save(admin);
                });
    }
}
