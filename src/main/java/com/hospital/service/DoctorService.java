package com.hospital.service;

import com.hospital.dto.DoctorRequest;
import com.hospital.dto.DoctorResponse;
import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for Doctor CRUD operations.
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    public DoctorResponse create(DoctorRequest request) {
        User user = findUser(request.getUserId());
        if (doctorRepository.existsByUser(user)) {
            throw new IllegalArgumentException("Doctor profile already exists for this user");
        }
        Doctor doctor = Doctor.builder()
                .specialization(request.getSpecialization())
                .phone(request.getPhone())
                .experienceYears(request.getExperienceYears())
                .user(user)
                .build();
        return toResponse(doctorRepository.save(doctor));
    }

    public List<DoctorResponse> getAll() {
        return doctorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DoctorResponse getById(Long id) {
        return toResponse(findDoctor(id));
    }

    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = findDoctor(id);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPhone(request.getPhone());
        doctor.setExperienceYears(request.getExperienceYears());
        return toResponse(doctorRepository.save(doctor));
    }

    public void delete(Long id) {
        doctorRepository.delete(findDoctor(id));
    }

    // ---- Helpers ----

    private Doctor findDoctor(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + id));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    public DoctorResponse toResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getUser().getName())
                .email(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .experienceYears(doctor.getExperienceYears())
                .build();
    }
}
