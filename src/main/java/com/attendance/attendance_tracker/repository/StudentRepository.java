package com.attendance.attendance_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.attendance_tracker.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmail(String email);

    boolean existsByRollNumberAndIdNot(String rollNumber, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Student> findByRollNumberContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String rollNumber, String firstName, String lastName);
}
