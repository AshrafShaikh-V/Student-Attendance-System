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

import com.attendance.attendance_tracker.dto.StudentRequestDTO;
import com.attendance.attendance_tracker.dto.StudentResponseDTO;
import com.attendance.attendance_tracker.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "Create, list, update, delete, and search student records.")
public class StudentController {

    private final StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create student", description = "Create a new student profile with validation checks for roll number, email, and academic details.")
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO createdStudent = studentService.createStudent(requestDTO);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "List students", description = "Retrieve all student records.")
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Get student by ID", description = "Fetch a single student by its unique identifier.")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update student", description = "Update an existing student record while preserving the current validation rules.")
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDTO requestDTO) {
        return ResponseEntity.ok(studentService.updateStudent(id, requestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete student", description = "Delete a student record by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @Operation(summary = "Search students", description = "Search for students using the provided query string.")
    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudents(@RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.ok(studentService.searchStudents(query));
    }
}
