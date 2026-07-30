package com.hospital.repository;

import com.hospital.entity.Doctor;
import com.hospital.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    boolean existsByUser(User user);
}
