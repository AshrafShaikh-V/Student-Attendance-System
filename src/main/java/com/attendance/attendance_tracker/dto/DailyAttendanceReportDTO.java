package com.attendance.attendance_tracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class DailyAttendanceReportDTO {
    private LocalDate attendanceDate;
    private long totalStudentsMarked;
    private long presentCount;
    private long absentCount;
    private double attendancePercentage;
    private List<DailyAttendanceEntryDTO> attendanceList;
}
