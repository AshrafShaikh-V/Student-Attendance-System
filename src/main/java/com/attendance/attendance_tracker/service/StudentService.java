package com.attendance.attendance_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.attendance_tracker.dto.StudentRequestDTO;
import com.attendance.attendance_tracker.dto.StudentResponseDTO;
import com.attendance.attendance_tracker.entity.Student;
import com.attendance.attendance_tracker.exception.DuplicateStudentException;
import com.attendance.attendance_tracker.exception.StudentNotFoundException;
import com.attendance.attendance_tracker.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        if (studentRepository.existsByRollNumber(requestDTO.getRollNumber())) {
            throw new DuplicateStudentException("Student already exists with roll number: " + requestDTO.getRollNumber());
        }
        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateStudentException("Student already exists with email: " + requestDTO.getEmail());
        }

        Student student = mapToEntity(requestDTO);
        Student savedStudent = studentRepository.save(student);
        return mapToDTO(savedStudent);
    }

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return mapToDTO(student);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        if (studentRepository.existsByRollNumberAndIdNot(requestDTO.getRollNumber(), id)) {
            throw new DuplicateStudentException("Another student exists with roll number: " + requestDTO.getRollNumber());
        }
        if (studentRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new DuplicateStudentException("Another student exists with email: " + requestDTO.getEmail());
        }

        existingStudent.setRollNumber(requestDTO.getRollNumber());
        existingStudent.setFirstName(requestDTO.getFirstName());
        existingStudent.setLastName(requestDTO.getLastName());
        existingStudent.setEmail(requestDTO.getEmail());
        existingStudent.setDepartment(requestDTO.getDepartment());
        existingStudent.setYear(requestDTO.getYear());
        existingStudent.setDivision(requestDTO.getDivision());

        Student updatedStudent = studentRepository.save(existingStudent);
        return mapToDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
    }

    public List<StudentResponseDTO> searchStudents(String query) {
        String searchTerm = query == null ? "" : query.trim();
        return studentRepository.findByRollNumberContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        searchTerm, searchTerm, searchTerm)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private Student mapToEntity(StudentRequestDTO dto) {
        return Student.builder()
                .rollNumber(dto.getRollNumber())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .year(dto.getYear())
                .division(dto.getDivision())
                .build();
    }

    private StudentResponseDTO mapToDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .rollNumber(student.getRollNumber())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .department(student.getDepartment())
                .year(student.getYear())
                .division(student.getDivision())
                .build();
    }
}
