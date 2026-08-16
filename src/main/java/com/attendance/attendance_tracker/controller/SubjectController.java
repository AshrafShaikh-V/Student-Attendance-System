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

import com.attendance.attendance_tracker.dto.SubjectRequestDTO;
import com.attendance.attendance_tracker.dto.SubjectResponseDTO;
import com.attendance.attendance_tracker.service.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Subject Management", description = "Create, list, update, delete, and search academic subjects.")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "Create subject", description = "Register a new academic subject with subject code and credit validation.")
    @PostMapping
    public ResponseEntity<SubjectResponseDTO> createSubject(@Valid @RequestBody SubjectRequestDTO requestDTO) {
        SubjectResponseDTO createdSubject = subjectService.createSubject(requestDTO);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @Operation(summary = "List subjects", description = "Retrieve all subject records.")
    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @Operation(summary = "Get subject by ID", description = "Fetch a single subject record by ID.")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @Operation(summary = "Update subject", description = "Update an existing subject while retaining validation constraints.")
    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequestDTO requestDTO) {
        return ResponseEntity.ok(subjectService.updateSubject(id, requestDTO));
    }

    @Operation(summary = "Delete subject", description = "Delete a subject record by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Search subjects", description = "Search for subjects using the supplied query string.")
    @GetMapping("/search")
    public ResponseEntity<List<SubjectResponseDTO>> searchSubjects(@RequestParam(required = false, defaultValue = "") String query) {
        return ResponseEntity.ok(subjectService.searchSubjects(query));
    }
}
