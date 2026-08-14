package com.attendance.attendance_tracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendancePercentageResponseDTO {

    private Long studentId;
    private String studentName;
    private long totalClasses;
    private long presentClasses;
    private long absentClasses;
    private double attendancePercentage;
}
