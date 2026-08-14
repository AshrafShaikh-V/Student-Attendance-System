package com.attendance.attendance_tracker.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentAttendanceReportDTO {
    private Long studentId;
    private String studentName;
    private long totalClasses;
    private long presentClasses;
    private long absentClasses;
    private double attendancePercentage;
    private List<AttendanceHistoryEntryDTO> attendanceHistory;
}
