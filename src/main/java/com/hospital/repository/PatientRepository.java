package com.hospital.repository;

import com.hospital.entity.Patient;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    boolean existsByUser(User user);
}
