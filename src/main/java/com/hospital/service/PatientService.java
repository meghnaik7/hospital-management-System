package com.hospital.service;

import com.hospital.dto.PatientRequest;
import com.hospital.dto.PatientResponse;
import com.hospital.entity.Patient;
import com.hospital.entity.User;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for Patient CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientResponse create(PatientRequest request) {
        User user = findUser(request.getUserId());
        if (patientRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Patient profile already exists for this user");
        }
        Patient patient = Patient.builder()
                .phone(request.getPhone())
                .age(request.getAge())
                .address(request.getAddress())
                .medicalHistory(request.getMedicalHistory())
                .user(user)
                .build();
        return toResponse(patientRepository.save(patient));
    }

    public List<PatientResponse> getAll() {
        return patientRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PatientResponse getById(Long id) {
        return toResponse(findPatient(id));
    }

    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = findPatient(id);
        patient.setPhone(request.getPhone());
        patient.setAge(request.getAge());
        patient.setAddress(request.getAddress());
        patient.setMedicalHistory(request.getMedicalHistory());
        return toResponse(patientRepository.save(patient));
    }

    public void delete(Long id) {
        patientRepository.delete(findPatient(id));
    }

    // ---- Helpers ----

    private Patient findPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with id: " + id));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    public PatientResponse toResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getUser().getName())
                .email(patient.getUser().getEmail())
                .phone(patient.getPhone())
                .age(patient.getAge())
                .address(patient.getAddress())
                .medicalHistory(patient.getMedicalHistory())
                .build();
    }
}
