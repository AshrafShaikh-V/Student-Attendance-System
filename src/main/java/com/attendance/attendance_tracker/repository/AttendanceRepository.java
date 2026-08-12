package com.attendance.attendance_tracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.attendance.attendance_tracker.entity.Attendance;
import com.attendance.attendance_tracker.entity.AttendanceStatus;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Fetches all attendance records along with their associated student and subject
     * in a single query, avoiding the N+1 SELECT problem caused by LAZY loading.
     */
    @Query("SELECT a FROM Attendance a JOIN FETCH a.student s JOIN FETCH a.subject sub")
    List<Attendance> findAllWithDetails();

    @Query("SELECT a FROM Attendance a JOIN FETCH a.student s JOIN FETCH a.subject sub " +
            "WHERE (:studentId IS NULL OR a.student.id = :studentId) " +
            "AND (:subjectId IS NULL OR a.subject.id = :subjectId) " +
            "AND (:attendanceDate IS NULL OR a.attendanceDate = :attendanceDate) " +
            "AND (:status IS NULL OR a.status = :status)")
    List<Attendance> searchAttendance(
            @Param("studentId") Long studentId,
            @Param("subjectId") Long subjectId,
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("status") AttendanceStatus status);

    boolean existsByStudentIdAndSubjectIdAndAttendanceDate(Long studentId, Long subjectId,
            LocalDate attendanceDate);

    boolean existsByStudentIdAndSubjectIdAndAttendanceDateAndIdNot(Long studentId, Long subjectId,
            LocalDate attendanceDate, Long id);
}
