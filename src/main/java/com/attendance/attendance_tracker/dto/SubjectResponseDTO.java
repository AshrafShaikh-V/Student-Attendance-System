package com.attendance.attendance_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponseDTO {

    private Long id;
    private String subjectCode;
    private String subjectName;
    private Integer credits;
}
