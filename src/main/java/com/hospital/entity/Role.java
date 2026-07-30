package com.hospital.entity;

/**
 * Roles available in the system.
 * ADMIN  - full access
 * DOCTOR - manage own profile and appointments
 * PATIENT - view/manage own appointments
 */
public enum Role {
    ADMIN,
    DOCTOR,
    PATIENT
}
