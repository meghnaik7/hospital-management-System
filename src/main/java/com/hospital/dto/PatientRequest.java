package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Payload for creating / updating a Patient profile.
 */
@Data
public class PatientRequest {

    @NotBlank(message = "Phone is required")
    private String phone;

    @Positive(message = "Age must be positive")
    private int age;

    @NotBlank(message = "Address is required")
    private String address;

    private String medicalHistory;

    // The user account to link with this patient profile
    @NotNull(message = "userId is required")
    private Long userId;
}
