package com.pm.patient_service.kafka;

import com.pm.patient_service.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

//this is the kafka producer
//responsible for sending events to a kafka topic
@Service
public class kafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(kafkaProducer.class);
    private final KafkaTemplate<String,byte[]> kafkaTemplate; // this how we define our message types
    // we use Kafka template to send the messages
    //this is telling we are sending kafka event , that has key: string and value : byte array
    public kafkaProducer(KafkaTemplate<String,byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendEvent(Patient patient)
    {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail() != null ? patient.getEmail() : "")
                .setEventType("PATIENT_CREATED")
                .build();
        try {
            kafkaTemplate.send("patient", event.toByteArray());
            log.info("PatientCreated event sent successfully for patientId: {}", patient.getId());
        }
        catch (Exception e) {
            log.error("Error sending PatientCreated event: {}", event, e);
        }
    }


}
