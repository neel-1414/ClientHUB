package com.pm.patient_service.dto;


import com.pm.patient_service.dto.Validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatientRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be more than 100 character")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "DateOfBirth is required")
    private String dateOfBirth;

    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered Date is required")
    private String registerDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }  public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
    public String getRegisterDate() {
        return registerDate;
    }
}
