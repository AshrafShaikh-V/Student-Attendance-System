package com.attendance.attendance_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.attendance_tracker.dto.SubjectRequestDTO;
import com.attendance.attendance_tracker.dto.SubjectResponseDTO;
import com.attendance.attendance_tracker.entity.Subject;
import com.attendance.attendance_tracker.exception.DuplicateSubjectException;
import com.attendance.attendance_tracker.exception.SubjectNotFoundException;
import com.attendance.attendance_tracker.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional
    public SubjectResponseDTO createSubject(SubjectRequestDTO requestDTO) {
        if (subjectRepository.existsBySubjectCode(requestDTO.getSubjectCode())) {
            throw new DuplicateSubjectException("Subject already exists with code: " + requestDTO.getSubjectCode());
        }

        Subject subject = mapToEntity(requestDTO);
        Subject savedSubject = subjectRepository.save(subject);
        return mapToDTO(savedSubject);
    }

    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public SubjectResponseDTO getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
        return mapToDTO(subject);
    }

    @Transactional
    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO requestDTO) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));

        if (subjectRepository.existsBySubjectCodeAndIdNot(requestDTO.getSubjectCode(), id)) {
            throw new DuplicateSubjectException("Another subject exists with code: " + requestDTO.getSubjectCode());
        }

        existingSubject.setSubjectCode(requestDTO.getSubjectCode());
        existingSubject.setSubjectName(requestDTO.getSubjectName());
        existingSubject.setCredits(requestDTO.getCredits());

        Subject updatedSubject = subjectRepository.save(existingSubject);
        return mapToDTO(updatedSubject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
        subjectRepository.delete(subject);
    }

    public List<SubjectResponseDTO> searchSubjects(String query) {
        String searchTerm = query == null ? "" : query.trim();
        return subjectRepository.findBySubjectCodeContainingIgnoreCaseOrSubjectNameContainingIgnoreCase(
                        searchTerm, searchTerm)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private Subject mapToEntity(SubjectRequestDTO dto) {
        return Subject.builder()
                .subjectCode(dto.getSubjectCode())
                .subjectName(dto.getSubjectName())
                .credits(dto.getCredits())
                .build();
    }

    private SubjectResponseDTO mapToDTO(Subject subject) {
        return SubjectResponseDTO.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .credits(subject.getCredits())
                .build();
    }
}
