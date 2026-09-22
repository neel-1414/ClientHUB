package com.pm.patient_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NonNull
    private String name;

    @NonNull
    @Email
    @Column(unique = true)
    private String email;

    @NonNull
    private String address;

    @NonNull
    private LocalDate dateOfBirth;

    @NonNull
    private LocalDate registeredDate;

    public @NonNull LocalDate getRegisterDate() {
        return registeredDate;
    }

    public void setRegisterDate(@NonNull LocalDate registerDate) {
        this.registeredDate = registerDate;
    }

    public @NonNull LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NonNull LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @NonNull String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public @NonNull String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }



}
