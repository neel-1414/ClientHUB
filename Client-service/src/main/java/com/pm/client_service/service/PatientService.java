package com.pm.patient_service.service;

import com.pm.patient_service.Exception.EmailAlreadyExistsException;
import com.pm.patient_service.Exception.PatientNotFoundException;
import com.pm.patient_service.Mapper.ClientMapper;
import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.grpc.BillingServiceGrpcClient;
import com.pm.patient_service.kafka.kafkaProducer;
import com.pm.patient_service.model.Client;
import com.pm.patient_service.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private final ClientRepository clientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final kafkaProducer kafkaProducer;

    public PatientService(ClientRepository clientRepository, BillingServiceGrpcClient billingServiceGrpcClient,
                          kafkaProducer kafkaProducer) {
        this.clientRepository = clientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatient() {
        List<Client> patients = clientRepository.findAll();
        return patients.stream().map(ClientMapper::toDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (clientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with email already exist:" + patientRequestDTO.getEmail());
        }
        Client patient = clientRepository.save(ClientMapper.toModel(patientRequestDTO));
        billingServiceGrpcClient.createBillingAccount(patient.getId().toString(), patient.getName(), patient.getEmail());
        kafkaProducer.sendEvent(patient);
        return ClientMapper.toDTO(patient);
    }

    public PatientResponseDTO updateInfo(UUID id, PatientRequestDTO patientRequestDTO) {
        Client patient = clientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with this UUID: " + id));
        if (clientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with email already exist:" + patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisterDate(LocalDate.parse(patientRequestDTO.getRegisterDate()));
        Client updatedPatient = clientRepository.save(patient);
        return ClientMapper.toDTO(updatedPatient);
    }

    public void DeletePatient(UUID id) {
        clientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient Not found"));
        clientRepository.deleteById(id);
    }
}
