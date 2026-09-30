package com.pm.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;
import project.events.ProjectEvent;

@Service
public class kafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(kafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumerEvent(ConsumerRecord<String, byte[]> record) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(record.value());
            log.info("Received Patient/Client Event: [Id={}, Name={}, Email={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail()
            );
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing patient event {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "projects", groupId = "analytics-service")
    public void consumeProjectEvent(ConsumerRecord<String, byte[]> record) {
        try {
            ProjectEvent projectEvent = ProjectEvent.parseFrom(record.value());
            log.info("Received Project Event: [EventType={}, ProjectId={}, DeveloperId={}, Budget={}, ReceiptId={}]",
                    projectEvent.getEventType(),
                    projectEvent.getProjectId(),
                    projectEvent.getDeveloperId(),
                    projectEvent.getBudget(),
                    projectEvent.getReceiptId()
            );
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing project event {}", e.getMessage());
        }
    }
}
