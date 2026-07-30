package com.hospital.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Payload for booking an appointment.
 */
@Data
public class AppointmentRequest {

    @NotNull(message = "Appointment date is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    @NotBlank(message = "Reason is required")
    private String reason;

    @NotNull(message = "patientId is required")
    private Long patientId;

    @NotNull(message = "doctorId is required")
    private Long doctorId;
}
