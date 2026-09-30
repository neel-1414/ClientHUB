package com.pm.patient_service.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import billing.ReceiptRequest;
import billing.ReceiptResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort) {
        log.info("Connect to billing service GRPC service at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();
        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("receive response from billing service via GRPC: {}", response);
        return response;
    }

    public ReceiptResponse generateReceipt(String projectId, String clientId, String developerId, double amount) {
        ReceiptRequest request = ReceiptRequest.newBuilder()
                .setProjectId(projectId)
                .setClientId(clientId != null ? clientId : "")
                .setDeveloperId(developerId != null ? developerId : "")
                .setAmount(amount)
                .build();
        ReceiptResponse response = blockingStub.generateReceipt(request);
        log.info("receive receipt response from billing service via GRPC: {}", response);
        return response;
    }
}
