package com.attendance.attendance_tracker.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.attendance_tracker.dto.AttendanceRequestDTO;
import com.attendance.attendance_tracker.dto.AttendanceResponseDTO;
import com.attendance.attendance_tracker.entity.AttendanceStatus;
import com.attendance.attendance_tracker.service.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
@Tag(name = "Attendance Management", description = "Track attendance records, filter them, and generate percentage and summary reports.")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "Create attendance record", description = "Create a student attendance entry for a specific subject and date.")
    @PostMapping
    public ResponseEntity<AttendanceResponseDTO> createAttendance(@Valid @RequestBody AttendanceRequestDTO dto) {
        AttendanceResponseDTO response = attendanceService.createAttendance(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves attendance records. All filter parameters are optional.
     * If no query parameters are provided, the endpoint returns all attendance records.
     */
    @Operation(summary = "List attendance records", description = "Retrieve attendance records and optionally filter them by student, subject, date, or status.")
    @GetMapping
    public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendance(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate attendanceDate,
            @RequestParam(required = false) AttendanceStatus status) {
        return ResponseEntity.ok(attendanceService.searchAttendance(studentId, subjectId, attendanceDate, status));
    }

    @Operation(summary = "Get student attendance percentage", description = "Return the aggregate attendance percentage for a student across all subjects.")
    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<com.attendance.attendance_tracker.dto.AttendancePercentageResponseDTO> getStudentAttendancePercentage(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendancePercentage(studentId));
    }

    @Operation(summary = "Get subject-specific percentage", description = "Return a student's percentage for a single subject.")
    @GetMapping("/student/{studentId}/subject/{subjectId}/percentage")
    public ResponseEntity<com.attendance.attendance_tracker.dto.AttendanceSubjectPercentageResponseDTO> getStudentSubjectAttendancePercentage(
            @PathVariable Long studentId, @PathVariable Long subjectId) {
        return ResponseEntity.ok(attendanceService.getStudentSubjectAttendancePercentage(studentId, subjectId));
    }

    @Operation(summary = "Student attendance report", description = "Generate a detailed attendance report for a specific student.")
    @GetMapping("/report/student/{studentId}")
    public ResponseEntity<com.attendance.attendance_tracker.dto.StudentAttendanceReportDTO> getAttendanceReportByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendanceReport(studentId));
    }

    @Operation(summary = "Subject attendance report", description = "Generate a subject-level attendance summary across all students.")
    @GetMapping("/report/subject/{subjectId}")
    public ResponseEntity<com.attendance.attendance_tracker.dto.SubjectAttendanceReportDTO> getAttendanceReportBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(attendanceService.getSubjectAttendanceReport(subjectId));
    }

    @Operation(summary = "Daily attendance report", description = "Generate a daily attendance summary for all students and subjects on a specific date.")
    @GetMapping("/report/date/{date}")
    public ResponseEntity<com.attendance.attendance_tracker.dto.DailyAttendanceReportDTO> getAttendanceReportByDate(
            @PathVariable @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate date) {
        return ResponseEntity.ok(attendanceService.getDailyAttendanceReport(date));
    }

    @Operation(summary = "Get attendance by ID", description = "Fetch a single attendance record by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @Operation(summary = "Update attendance record", description = "Update an attendance record, including duplicate prevention checks.")
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(@PathVariable Long id,
                                                                  @Valid @RequestBody AttendanceRequestDTO dto) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, dto));
    }

    @Operation(summary = "Delete attendance record", description = "Delete a single attendance record by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
