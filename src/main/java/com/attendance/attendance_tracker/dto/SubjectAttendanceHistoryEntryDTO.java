package com.attendance.attendance_tracker.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubjectAttendanceHistoryEntryDTO {
    private LocalDate attendanceDate;
    private Long studentId;
    private String studentName;
    private String status;
}
