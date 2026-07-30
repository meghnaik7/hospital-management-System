package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Payload for creating / updating a Doctor profile.
 */
@Data
public class DoctorRequest {

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Phone is required")
    private String phone;

    @Positive(message = "Experience years must be positive")
    private int experienceYears;

    // The user account to link with this doctor profile
    @NotNull(message = "userId is required")
    private Long userId;
}
