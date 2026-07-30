package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Safe representation of a Doctor – never exposes password.
 */
@Data
@Builder
public class DoctorResponse {
    private Long id;
    private String name;
    private String email;
    private String specialization;
    private String phone;
    private int experienceYears;
}
