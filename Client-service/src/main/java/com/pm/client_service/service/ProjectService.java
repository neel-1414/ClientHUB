package com.pm.patient_service.service;

import billing.ReceiptResponse;
import com.pm.patient_service.Exception.ProjectNotFoundException;
import com.pm.patient_service.Mapper.ProjectMapper;
import com.pm.patient_service.dto.ProjectRequestDTO;
import com.pm.patient_service.dto.ProjectResponseDTO;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.kafkaProducer;
import com.pm.patient_service.model.Project;
import com.pm.patient_service.model.ProjectStatus;
import com.pm.patient_service.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final kafkaProducer kafkaProducer;

    public ProjectService(ProjectRepository projectRepository,
                          BillingServiceGrpcClient billingServiceGrpcClient,
                          kafkaProducer kafkaProducer) {
        this.projectRepository = projectRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        Project project = ProjectMapper.toModel(request);
        Project savedProject = projectRepository.save(project);

        kafkaProducer.sendProjectEvent(savedProject, "PROJECT_SUBMITTED");

        return ProjectMapper.toDTO(savedProject);
    }

    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public List<ProjectResponseDTO> getOpenProjects() {
        return projectRepository.findByStatus(ProjectStatus.OPEN)
                .stream()
                .map(ProjectMapper::toDTO)
                .toList();
    }

    public ProjectResponseDTO getProjectById(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + id));
        return ProjectMapper.toDTO(project);
    }

    @Transactional
    public ProjectResponseDTO acceptProject(UUID projectId, UUID developerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        if (project.getStatus() != ProjectStatus.OPEN) {
            throw new IllegalStateException("Project is not open for acceptance. Current status: " + project.getStatus());
        }

        project.setDeveloperId(developerId);
        project.setStatus(ProjectStatus.ACCEPTED);
        project.setAcceptedAt(LocalDateTime.now());

        try {
            ReceiptResponse receiptResponse = billingServiceGrpcClient.generateReceipt(
                    project.getId().toString(),
                    project.getClientId() != null ? project.getClientId().toString() : "",
                    developerId.toString(),
                    project.getBudget() != null ? project.getBudget().doubleValue() : 0.0
            );
            project.setReceiptId(receiptResponse.getReceiptId());
        } catch (Exception e) {
            project.setReceiptId("REC-PENDING");
        }

        Project updatedProject = projectRepository.save(project);

        kafkaProducer.sendProjectEvent(updatedProject, "PROJECT_ACCEPTED");

        return ProjectMapper.toDTO(updatedProject);
    }
}
