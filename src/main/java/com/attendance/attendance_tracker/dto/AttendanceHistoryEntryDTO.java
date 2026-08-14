package com.attendance.attendance_tracker.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceHistoryEntryDTO {
    private LocalDate attendanceDate;
    private String subjectName;
    private String status;
}
