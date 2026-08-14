package com.attendance.attendance_tracker.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponseDTO> createAttendance(@Valid @RequestBody AttendanceRequestDTO dto) {
        AttendanceResponseDTO response = attendanceService.createAttendance(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieves attendance records. All filter parameters are optional.
     * If no query parameters are provided, the endpoint returns all attendance records.
     */
    @GetMapping
    public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendance(
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate attendanceDate,
            @RequestParam(required = false) AttendanceStatus status) {
        return ResponseEntity.ok(attendanceService.searchAttendance(studentId, subjectId, attendanceDate, status));
    }

    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<com.attendance.attendance_tracker.dto.AttendancePercentageResponseDTO> getStudentAttendancePercentage(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getStudentAttendancePercentage(studentId));
    }

    @GetMapping("/student/{studentId}/subject/{subjectId}/percentage")
    public ResponseEntity<com.attendance.attendance_tracker.dto.AttendanceSubjectPercentageResponseDTO> getStudentSubjectAttendancePercentage(
            @PathVariable Long studentId, @PathVariable Long subjectId) {
        return ResponseEntity.ok(attendanceService.getStudentSubjectAttendancePercentage(studentId, subjectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> getAttendanceById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getAttendanceById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponseDTO> updateAttendance(@PathVariable Long id,
                                                                  @Valid @RequestBody AttendanceRequestDTO dto) {
        return ResponseEntity.ok(attendanceService.updateAttendance(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}
