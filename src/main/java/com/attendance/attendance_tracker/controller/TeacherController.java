package com.attendance.attendance_tracker.controller;

import java.util.List;

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

import com.attendance.attendance_tracker.dto.TeacherRequestDTO;
import com.attendance.attendance_tracker.dto.TeacherResponseDTO;
import com.attendance.attendance_tracker.service.TeacherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Teacher Management", description = "Create, list, update, delete, and search teacher records.")
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "Create teacher", description = "Register a teacher with email and password validation.")
    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @RequestBody TeacherRequestDTO requestDTO) {
        TeacherResponseDTO createdTeacher = teacherService.createTeacher(requestDTO);
        return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

    @Operation(summary = "List teachers", description = "Retrieve all teacher profiles.")
    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }

    @Operation(summary = "Get teacher by ID", description = "Fetch a single teacher record by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @Operation(summary = "Update teacher", description = "Update an existing teacher record while enforcing validation rules.")
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequestDTO requestDTO) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, requestDTO));
    }

    @Operation(summary = "Delete teacher", description = "Delete a teacher record by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search teachers", description = "Search for teachers using the provided query string.")
    @GetMapping("/search")
    public ResponseEntity<List<TeacherResponseDTO>> searchTeachers(@RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.ok(teacherService.searchTeachers(query));
    }
}
