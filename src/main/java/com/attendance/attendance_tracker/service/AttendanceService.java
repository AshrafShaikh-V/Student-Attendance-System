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
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    @Transactional
    public AttendanceResponseDTO createAttendance(AttendanceRequestDTO dto) {
        // Prevent duplicate attendance for the same student, subject and date.
        boolean exists = attendanceRepository.existsByStudentIdAndSubjectIdAndAttendanceDate(
            dto.getStudentId(), dto.getSubjectId(), dto.getAttendanceDate());
        if (exists) {
            throw new com.attendance.attendance_tracker.exception.DuplicateAttendanceException(
                "Attendance already recorded for this student, subject and date");
        }
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

    public List<AttendanceResponseDTO> getAllAttendance() {
        // Uses JOIN FETCH to load student and subject in a single query (avoids N+1).
        return attendanceRepository.findAllWithDetails().stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public List<AttendanceResponseDTO> searchAttendance(Long studentId, Long subjectId,
            java.time.LocalDate attendanceDate, com.attendance.attendance_tracker.entity.AttendanceStatus status) {
        return attendanceRepository.searchAttendance(studentId, subjectId, attendanceDate, status).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    public AttendanceResponseDTO getAttendanceById(Long id) {
        Attendance attendance = findAttendanceById(id);
        return mapToResponseDTO(attendance);
    }

    /**
     * Milestone 1: Calculate attendance percentage for a student.
     */
    public com.attendance.attendance_tracker.dto.AttendancePercentageResponseDTO getStudentAttendancePercentage(Long studentId) {
        // Verify student exists (throws StudentNotFoundException if absent)
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        // Use grouped query to reduce DB round-trips
        java.util.List<Object[]> grouped = attendanceRepository.countByStudentGroupedByStatus(studentId);
        long present = 0L, absent = 0L, total = 0L;
        for (Object[] row : grouped) {
            com.attendance.attendance_tracker.entity.AttendanceStatus status = (com.attendance.attendance_tracker.entity.AttendanceStatus) row[0];
            long cnt = ((Number) row[1]).longValue();
            total += cnt;
            if (status == com.attendance.attendance_tracker.entity.AttendanceStatus.PRESENT) present = cnt;
            else if (status == com.attendance.attendance_tracker.entity.AttendanceStatus.ABSENT) absent = cnt;
        }

        double percentage = 0.0;
        if (total > 0) {
            // Present / Total * 100, rounded to 2 decimal places
            percentage = Math.round((present * 10000.0) / total) / 100.0;
        }

        String studentName = student.getFirstName() + (student.getLastName() == null || student.getLastName().isBlank() ? "" : " " + student.getLastName());

        return com.attendance.attendance_tracker.dto.AttendancePercentageResponseDTO.builder()
                .studentId(student.getId())
                .studentName(studentName)
                .totalClasses(total)
                .presentClasses(present)
                .absentClasses(absent)
                .attendancePercentage(percentage)
                .build();
    }

    /**
     * Milestone 2: Calculate subject-wise attendance percentage for a student.
     */
    public com.attendance.attendance_tracker.dto.AttendanceSubjectPercentageResponseDTO getStudentSubjectAttendancePercentage(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new com.attendance.attendance_tracker.exception.SubjectNotFoundException(subjectId));

        // Use grouped query to reduce DB round-trips for student+subject
        java.util.List<Object[]> grouped = attendanceRepository.countByStudentAndSubjectGroupedByStatus(studentId, subjectId);
        long present = 0L, absent = 0L, total = 0L;
        for (Object[] row : grouped) {
            com.attendance.attendance_tracker.entity.AttendanceStatus status = (com.attendance.attendance_tracker.entity.AttendanceStatus) row[0];
            long cnt = ((Number) row[1]).longValue();
            total += cnt;
            if (status == com.attendance.attendance_tracker.entity.AttendanceStatus.PRESENT) present = cnt;
            else if (status == com.attendance.attendance_tracker.entity.AttendanceStatus.ABSENT) absent = cnt;
        }

        double percentage = 0.0;
        if (total > 0) {
            percentage = Math.round((present * 10000.0) / total) / 100.0;
        }

        String studentName = student.getFirstName() + (student.getLastName() == null || student.getLastName().isBlank() ? "" : " " + student.getLastName());

        return com.attendance.attendance_tracker.dto.AttendanceSubjectPercentageResponseDTO.builder()
                .studentId(student.getId())
                .studentName(studentName)
                .subjectId(subject.getId())
                .subjectName(subject.getSubjectName())
                .totalClasses(total)
                .presentClasses(present)
                .absentClasses(absent)
                .attendancePercentage(percentage)
                .build();
    }

    /**
     * Milestone 3: Attendance reports
     */
    public com.attendance.attendance_tracker.dto.AttendanceReportResponseDTO getAttendanceReportByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        // Fetch records with joins using existing search to benefit from JOIN FETCH
        java.util.List<Attendance> records = attendanceRepository.searchAttendance(studentId, null, null, null);

        long total = attendanceRepository.countByStudentId(studentId);
        long present = attendanceRepository.countByStudentIdAndStatus(studentId, com.attendance.attendance_tracker.entity.AttendanceStatus.PRESENT);
        long absent = attendanceRepository.countByStudentIdAndStatus(studentId, com.attendance.attendance_tracker.entity.AttendanceStatus.ABSENT);
        double percent = total > 0 ? Math.round((present * 10000.0) / total) / 100.0 : 0.0;

        java.util.List<com.attendance.attendance_tracker.dto.AttendanceResponseDTO> dtoRecords = records.stream()
                .map(this::mapToResponseDTO)
                .toList();

        String studentName = student.getFirstName() + (student.getLastName() == null || student.getLastName().isBlank() ? "" : " " + student.getLastName());

        return com.attendance.attendance_tracker.dto.AttendanceReportResponseDTO.builder()
                .studentId(student.getId())
                .studentName(studentName)
                .totalRecords(total)
                .presentCount(present)
                .absentCount(absent)
                .presentPercentage(percent)
                .records(dtoRecords)
                .build();
    }

    public com.attendance.attendance_tracker.dto.AttendanceReportResponseDTO getAttendanceReportBySubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new com.attendance.attendance_tracker.exception.SubjectNotFoundException(subjectId));

        java.util.List<Attendance> records = attendanceRepository.searchAttendance(null, subjectId, null, null);

        long total = attendanceRepository.countBySubjectId(subjectId);
        long present = attendanceRepository.countBySubjectIdAndStatus(subjectId, com.attendance.attendance_tracker.entity.AttendanceStatus.PRESENT);
        long absent = attendanceRepository.countBySubjectIdAndStatus(subjectId, com.attendance.attendance_tracker.entity.AttendanceStatus.ABSENT);
        double percent = total > 0 ? Math.round((present * 10000.0) / total) / 100.0 : 0.0;

        java.util.List<com.attendance.attendance_tracker.dto.AttendanceResponseDTO> dtoRecords = records.stream()
                .map(this::mapToResponseDTO)
                .toList();

        return com.attendance.attendance_tracker.dto.AttendanceReportResponseDTO.builder()
                .subjectId(subject.getId())
                .subjectName(subject.getSubjectName())
                .totalRecords(total)
                .presentCount(present)
                .absentCount(absent)
                .presentPercentage(percent)
                .records(dtoRecords)
                .build();
    }

    public com.attendance.attendance_tracker.dto.AttendanceReportResponseDTO getAttendanceReportByDate(java.time.LocalDate date) {
        java.util.List<Attendance> records = attendanceRepository.searchAttendance(null, null, date, null);

        long total = attendanceRepository.countByAttendanceDate(date);
        long present = attendanceRepository.countByAttendanceDateAndStatus(date, com.attendance.attendance_tracker.entity.AttendanceStatus.PRESENT);
        long absent = attendanceRepository.countByAttendanceDateAndStatus(date, com.attendance.attendance_tracker.entity.AttendanceStatus.ABSENT);
        double percent = total > 0 ? Math.round((present * 10000.0) / total) / 100.0 : 0.0;

        java.util.List<com.attendance.attendance_tracker.dto.AttendanceResponseDTO> dtoRecords = records.stream()
                .map(this::mapToResponseDTO)
                .toList();

        return com.attendance.attendance_tracker.dto.AttendanceReportResponseDTO.builder()
                .date(date)
                .totalRecords(total)
                .presentCount(present)
                .absentCount(absent)
                .presentPercentage(percent)
                .records(dtoRecords)
                .build();
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

        if (attendanceRepository.existsByStudentIdAndSubjectIdAndAttendanceDateAndIdNot(
                dto.getStudentId(), dto.getSubjectId(), dto.getAttendanceDate(), id)) {
            throw new com.attendance.attendance_tracker.exception.DuplicateAttendanceException(
                    "Attendance already recorded for this student, subject and date");
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
