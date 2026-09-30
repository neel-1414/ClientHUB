package com.pm.patient_service.controller;

import com.pm.patient_service.dto.ProjectAcceptDTO;
import com.pm.patient_service.dto.ProjectRequestDTO;
import com.pm.patient_service.dto.ProjectResponseDTO;
import com.pm.patient_service.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/createProject")
    public ResponseEntity<ProjectResponseDTO> createProject(@Valid @RequestBody ProjectRequestDTO projectRequestDTO) {
        ProjectResponseDTO responseDTO = projectService.createProject(projectRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/getAllProjects")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjects() {
        List<ProjectResponseDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/getOpenProjects")
    public ResponseEntity<List<ProjectResponseDTO>> getOpenProjects() {
        List<ProjectResponseDTO> openProjects = projectService.getOpenProjects();
        return ResponseEntity.ok(openProjects);
    }

    @GetMapping("/getProject/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable UUID id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/acceptProject/{id}")
    public ResponseEntity<ProjectResponseDTO> acceptProject(
            @PathVariable UUID id,
            @Valid @RequestBody ProjectAcceptDTO acceptDTO) {
        ProjectResponseDTO updatedProject = projectService.acceptProject(id, acceptDTO.getDeveloperId());
        return ResponseEntity.ok(updatedProject);
    }
}
