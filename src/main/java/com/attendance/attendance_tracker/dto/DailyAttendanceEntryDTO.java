package com.attendance.attendance_tracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DailyAttendanceEntryDTO {
    private Long studentId;
    private String studentName;
    private Long subjectId;
    private String subjectName;
    private String status;
}
