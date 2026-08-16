package com.attendance.attendance_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.attendance_tracker.entity.Role;
import com.attendance.attendance_tracker.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Teacher> findByEmail(String email);

    Optional<Teacher> findByEmailIgnoreCase(String email);

    long countByRole(Role role);

    List<Teacher> findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String email, String firstName, String lastName);
}
