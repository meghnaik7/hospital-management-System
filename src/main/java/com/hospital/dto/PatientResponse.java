package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Safe representation of a Patient – never exposes password.
 */
@Data
@Builder
public class PatientResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private int age;
    private String address;
    private String medicalHistory;
}
