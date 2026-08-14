package com.attendance.attendance_tracker.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceReportResponseDTO {
    // Generic report wrapper
    private Long studentId;
    private String studentName;

    private Long subjectId;
    private String subjectName;

    private LocalDate date;

    private long totalRecords;
    private long presentCount;
    private long absentCount;
    private double presentPercentage;

    private List<AttendanceResponseDTO> records;
}
