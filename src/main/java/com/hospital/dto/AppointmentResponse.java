package com.hospital.dto;

import com.hospital.entity.Appointment.AppointmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO for Appointment – flattens nested entities for clarity.
 */
@Data
@Builder
public class AppointmentResponse {
    private Long id;
    private LocalDateTime appointmentDate;
    private String reason;
    private AppointmentStatus status;

    // Patient info
    private Long patientId;
    private String patientName;

    // Doctor info
    private Long doctorId;
    private String doctorName;
    private String specialization;
}
