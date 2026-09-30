package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.ReceiptRequest;
import billing.ReceiptResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.UUID;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {
        log.info("create billing account request received: {}", billingRequest.toString());

        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("1234")
                .setStatus("Active")
                .build();
        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void generateReceipt(ReceiptRequest request, StreamObserver<ReceiptResponse> responseObserver) {
        log.info("generate receipt request received for projectId: {}, clientId: {}, developerId: {}, amount: {}",
                request.getProjectId(), request.getClientId(), request.getDeveloperId(), request.getAmount());

        String receiptId = "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ReceiptResponse response = ReceiptResponse.newBuilder()
                .setReceiptId(receiptId)
                .setStatus("GENERATED")
                .setGeneratedAt(Instant.now().toString())
                .setAmount(request.getAmount())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
