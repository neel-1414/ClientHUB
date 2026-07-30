package com.pm.billingservice.grpc;
import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase{
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);


    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {
        log.info("create billing account request received{}",billingRequest.toString());

        //business logic
        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("1234")
                .setStatus("Active")
                .build();
        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}
