package com.attendance.attendance_tracker.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.attendance_tracker.dto.DashboardSummaryDTO;
import com.attendance.attendance_tracker.entity.AttendanceStatus;
import com.attendance.attendance_tracker.repository.AttendanceRepository;
import com.attendance.attendance_tracker.repository.StudentRepository;
import com.attendance.attendance_tracker.repository.SubjectRepository;
import com.attendance.attendance_tracker.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final AttendanceRepository attendanceRepository;

    public DashboardSummaryDTO getDashboardSummary() {
        LocalDate today = LocalDate.now();

        long totalStudents = studentRepository.count();
        long totalTeachers = teacherRepository.count();
        long totalSubjects = subjectRepository.count();
        long totalAttendanceRecords = attendanceRepository.count();

        long todaysAttendanceCount = attendanceRepository.countByAttendanceDate(today);
        long presentToday = attendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.PRESENT);
        long absentToday = attendanceRepository.countByAttendanceDateAndStatus(today, AttendanceStatus.ABSENT);

        return DashboardSummaryDTO.builder()
                .totalStudents(totalStudents)
                .totalTeachers(totalTeachers)
                .totalSubjects(totalSubjects)
                .totalAttendanceRecords(totalAttendanceRecords)
                .todaysAttendanceCount(todaysAttendanceCount)
                .presentToday(presentToday)
                .absentToday(absentToday)
                .build();
    }
}
