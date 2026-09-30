package com.pm.patient_service.repository;

import com.pm.patient_service.model.Project;
import com.pm.patient_service.model.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByClientId(UUID clientId);
    List<Project> findByDeveloperId(UUID developerId);
}
