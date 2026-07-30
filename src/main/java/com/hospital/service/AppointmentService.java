package com.hospital.service;

import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentResponse;
import com.hospital.entity.Appointment;
import com.hospital.entity.Appointment.AppointmentStatus;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for Appointment CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentResponse create(AppointmentRequest request) {
        Patient patient = findPatient(request.getPatientId());
        Doctor doctor  = findDoctor(request.getDoctorId());

        Appointment appointment = Appointment.builder()
                .appointmentDate(request.getAppointmentDate())
                .reason(request.getReason())
                .status(AppointmentStatus.SCHEDULED)
                .patient(patient)
                .doctor(doctor)
                .build();

        return toResponse(appointmentRepository.save(appointment));
    }

    public List<AppointmentResponse> getAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AppointmentResponse getById(Long id) {
        return toResponse(findAppointment(id));
    }

    /** Get all appointments for a specific patient. */
    public List<AppointmentResponse> getByPatient(Long patientId) {
        Patient patient = findPatient(patientId);
        return appointmentRepository.findByPatient(patient)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /** Get all appointments for a specific doctor. */
    public List<AppointmentResponse> getByDoctor(Long doctorId) {
        Doctor doctor = findDoctor(doctorId);
        return appointmentRepository.findByDoctor(doctor)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update appointment status (SCHEDULED → COMPLETED or CANCELLED).
     */
    public AppointmentResponse updateStatus(Long id, String status) {
        Appointment appointment = findAppointment(id);
        try {
            appointment.setStatus(AppointmentStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Use SCHEDULED, COMPLETED, or CANCELLED");
        }
        return toResponse(appointmentRepository.save(appointment));
    }

    public void delete(Long id) {
        appointmentRepository.delete(findAppointment(id));
    }

    // ---- Helpers ----

    private Appointment findAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + id));
    }

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
    }

    private Doctor findDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + id));
    }

    private AppointmentResponse toResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .appointmentDate(a.getAppointmentDate())
                .reason(a.getReason())
                .status(a.getStatus())
                .patientId(a.getPatient().getId())
                .patientName(a.getPatient().getUser().getName())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getUser().getName())
                .specialization(a.getDoctor().getSpecialization())
                .build();
    }
}
