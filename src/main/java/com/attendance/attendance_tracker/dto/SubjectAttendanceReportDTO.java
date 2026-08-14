package com.attendance.attendance_tracker.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubjectAttendanceReportDTO {
    private Long subjectId;
    private String subjectName;
    private long totalStudents;
    private long totalAttendanceRecords;
    private long presentCount;
    private long absentCount;
    private double attendancePercentage;
    private List<SubjectAttendanceHistoryEntryDTO> attendanceHistory;
}
