package com.attendance.attendance_tracker.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.attendance_tracker.dto.TeacherRequestDTO;
import com.attendance.attendance_tracker.dto.TeacherResponseDTO;
import com.attendance.attendance_tracker.entity.Teacher;
import com.attendance.attendance_tracker.exception.DuplicateTeacherException;
import com.attendance.attendance_tracker.exception.TeacherNotFoundException;
import com.attendance.attendance_tracker.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public TeacherResponseDTO createTeacher(TeacherRequestDTO requestDTO) {
        if (teacherRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateTeacherException("Teacher already exists with email: " + requestDTO.getEmail());
        }

        Teacher teacher = mapToEntity(requestDTO);
        Teacher savedTeacher = teacherRepository.save(teacher);
        return mapToDTO(savedTeacher);
    }

    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TeacherResponseDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));
        return mapToDTO(teacher);
    }

    @Transactional
    public TeacherResponseDTO updateTeacher(Long id, TeacherRequestDTO requestDTO) {
        Teacher existingTeacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException(id));

        // Only check uniqueness when email changes to avoid extra DB call
        if (!existingTeacher.getEmail().equals(requestDTO.getEmail()) &&
                teacherRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new DuplicateTeacherException("Another teacher exists with email: " + requestDTO.getEmail());
        }

        existingTeacher.setFirstName(requestDTO.getFirstName());
        existingTeacher.setLastName(requestDTO.getLastName());
        existingTeacher.setEmail(requestDTO.getEmail());
        // Only re-hash if a new password value is provided.
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            existingTeacher.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }
        existingTeacher.setSpecialization(requestDTO.getSpecialization());

        Teacher updatedTeacher = teacherRepository.save(existingTeacher);
        return mapToDTO(updatedTeacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new TeacherNotFoundException(id);
        }
        teacherRepository.deleteById(id);
    }

    public List<TeacherResponseDTO> searchTeachers(String query) {
        String searchTerm = query == null ? "" : query.trim();
        return teacherRepository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        searchTerm, searchTerm, searchTerm)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private Teacher mapToEntity(TeacherRequestDTO dto) {
        return Teacher.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .specialization(dto.getSpecialization())
                .build();
    }

    private TeacherResponseDTO mapToDTO(Teacher teacher) {
        return TeacherResponseDTO.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .specialization(teacher.getSpecialization())
                .build();
        // Note: password is intentionally NEVER included in the response DTO.
    }
}
