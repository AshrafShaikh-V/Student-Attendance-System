package com.attendance.attendance_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.attendance.attendance_tracker.entity.Subject;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsBySubjectCode(String subjectCode);

    boolean existsBySubjectCodeAndIdNot(String subjectCode, Long id);

    List<Subject> findBySubjectCodeContainingIgnoreCaseOrSubjectNameContainingIgnoreCase(
            String subjectCode, String subjectName);
}
