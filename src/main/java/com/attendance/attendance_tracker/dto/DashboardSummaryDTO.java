package com.attendance.attendance_tracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DashboardSummaryDTO {
    private long totalStudents;
    private long totalTeachers;
    private long totalSubjects;
    private long totalAttendanceRecords;
    private long todaysAttendanceCount;
    private long presentToday;
    private long absentToday;
}
