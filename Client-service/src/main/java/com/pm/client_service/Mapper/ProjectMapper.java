package com.pm.patient_service.Mapper;

import com.pm.patient_service.dto.ProjectRequestDTO;
import com.pm.patient_service.dto.ProjectResponseDTO;
import com.pm.patient_service.model.Project;
import com.pm.patient_service.model.ProjectStatus;

public class ProjectMapper {

    public static Project toModel(ProjectRequestDTO dto) {
        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setBudget(dto.getBudget());
        project.setClientId(dto.getClientId());
        project.setStatus(ProjectStatus.OPEN);
        return project;
    }

    public static ProjectResponseDTO toDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setBudget(project.getBudget());
        dto.setStatus(project.getStatus());
        dto.setClientId(project.getClientId());
        dto.setDeveloperId(project.getDeveloperId());
        dto.setReceiptId(project.getReceiptId());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setAcceptedAt(project.getAcceptedAt());
        return dto;
    }
}
