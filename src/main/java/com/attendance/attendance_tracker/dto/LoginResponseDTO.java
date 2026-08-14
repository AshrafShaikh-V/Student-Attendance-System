package com.attendance.attendance_tracker.dto;

import com.attendance.attendance_tracker.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class LoginResponseDTO {

    @Schema(description = "Bearer token to send in the Authorization header")
    private String accessToken;

    @Schema(example = "Bearer")
    private String tokenType;

    @Schema(description = "Lifetime of the token in seconds")
    private long expiresInSeconds;

    private String email;
    private Role role;
}
