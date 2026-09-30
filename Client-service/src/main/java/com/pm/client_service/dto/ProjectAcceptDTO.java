package com.pm.patient_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ProjectAcceptDTO {

    @NotNull(message = "Developer ID is required")
    private UUID developerId;

    public UUID getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(UUID developerId) {
        this.developerId = developerId;
    }
}
