package com.attendance.attendance_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.attendance.attendance_tracker.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Fetches all attendance records along with their associated student and subject
     * in a single query, avoiding the N+1 SELECT problem caused by LAZY loading.
     */
    @Query("SELECT a FROM Attendance a JOIN FETCH a.student s JOIN FETCH a.subject sub")
    List<Attendance> findAllWithDetails();
}
    boolean existsByStudentIdAndSubjectIdAndAttendanceDate(Long studentId, Long subjectId, java.time.LocalDate attendanceDate);
