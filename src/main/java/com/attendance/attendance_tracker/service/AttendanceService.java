package com.attendance.attendance_tracker.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.attendance_tracker.dto.AttendanceRequestDTO;
import com.attendance.attendance_tracker.dto.AttendanceResponseDTO;
import com.attendance.attendance_tracker.entity.Attendance;
import com.attendance.attendance_tracker.entity.Student;
import com.attendance.attendance_tracker.entity.Subject;
import com.attendance.attendance_tracker.exception.AttendanceNotFoundException;
import com.attendance.attendance_tracker.exception.StudentNotFoundException;
import com.attendance.attendance_tracker.exception.SubjectNotFoundException;
import com.attendance.attendance_tracker.repository.AttendanceRepository;
import com.attendance.attendance_tracker.repository.StudentRepository;
import com.attendance.attendance_tracker.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public AttendanceResponseDTO createAttendance(AttendanceRequestDTO dto) {
        Student student = findStudentById(dto.getStudentId());
        Subject subject = findSubjectById(dto.getSubjectId());

        Attendance attendance = Attendance.builder()
                .student(student)
                .subject(subject)
                .attendanceDate(dto.getAttendanceDate())
                .status(dto.getStatus())
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        return mapToResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponseDTO> getAllAttendance() {
        return attendanceRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public AttendanceResponseDTO getAttendanceById(Long id) {
        Attendance attendance = findAttendanceById(id);
        return mapToResponseDTO(attendance);
    }

    @Transactional
    public AttendanceResponseDTO updateAttendance(Long id, AttendanceRequestDTO dto) {
        Attendance existing = findAttendanceById(id);

        if (!existing.getStudent().getId().equals(dto.getStudentId())) {
            existing.setStudent(findStudentById(dto.getStudentId()));
        }
        if (!existing.getSubject().getId().equals(dto.getSubjectId())) {
            existing.setSubject(findSubjectById(dto.getSubjectId()));
        }

        existing.setAttendanceDate(dto.getAttendanceDate());
        existing.setStatus(dto.getStatus());

        Attendance updated = attendanceRepository.save(existing);
        return mapToResponseDTO(updated);
    }

    @Transactional
    public void deleteAttendance(Long id) {
        if (!attendanceRepository.existsById(id)) {
            throw new AttendanceNotFoundException(id);
        }
        attendanceRepository.deleteById(id);
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    private Subject findSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(id));
    }

    private Attendance findAttendanceById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new AttendanceNotFoundException(id));
    }

    private AttendanceResponseDTO mapToResponseDTO(Attendance attendance) {
        return AttendanceResponseDTO.builder()
                .id(attendance.getId())
                .studentId(attendance.getStudent().getId())
                .studentName(attendance.getStudent().getFirstName() + " " + attendance.getStudent().getLastName())
                .subjectId(attendance.getSubject().getId())
                .subjectName(attendance.getSubject().getSubjectName())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .build();
    }
}
