package com.pm.patient_service.kafka;

import com.pm.patient_service.model.Client;
import com.pm.patient_service.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import project.events.ProjectEvent;

@Service
public class kafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(kafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public kafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Client client) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(client.getId().toString())
                .setName(client.getName())
                .setEmail(client.getEmail() != null ? client.getEmail() : "")
                .setEventType("CLIENT_CREATED")
                .build();
        try {
            kafkaTemplate.send("patient", event.toByteArray());
            log.info("ClientCreated event sent successfully for clientId: {}", client.getId());
        } catch (Exception e) {
            log.error("Error sending ClientCreated event: {}", event, e);
        }
    }

    public void sendProjectEvent(Project project, String eventType) {
        ProjectEvent event = ProjectEvent.newBuilder()
                .setProjectId(project.getId().toString())
                .setClientId(project.getClientId() != null ? project.getClientId().toString() : "")
                .setDeveloperId(project.getDeveloperId() != null ? project.getDeveloperId().toString() : "")
                .setTitle(project.getTitle() != null ? project.getTitle() : "")
                .setBudget(project.getBudget() != null ? project.getBudget().doubleValue() : 0.0)
                .setReceiptId(project.getReceiptId() != null ? project.getReceiptId() : "")
                .setEventType(eventType)
                .build();
        try {
            kafkaTemplate.send("projects", event.toByteArray());
            log.info("Project event [{}] sent successfully for projectId: {}", eventType, project.getId());
        } catch (Exception e) {
            log.error("Error sending Project event: {}", event, e);
        }
    }
}
