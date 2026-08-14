package com.attendance.attendance_tracker.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.attendance.attendance_tracker.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

/**
 * Loads a teacher by email for authentication.
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return teacherRepository.findByEmail(email)
            .map(AppUserPrincipal::from)
            .orElseThrow(() -> new UsernameNotFoundException("Teacher not found: " + email));
    }
}
