package com.grpcpractice.client;

import com.grpcpractice.models.BankServiceGrpc;
import com.grpcpractice.models.TransferRequest;
import com.grpcpractice.models.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest
{

    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setup() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",6565)
                .usePlaintext()
                .build();
        this.transferServiceStub = TransferServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void transfer() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TransferStreamingResponse response = new TransferStreamingResponse(latch);
        StreamObserver<TransferRequest> requestStreamObserver = this.transferServiceStub.transfer(response);

        for (int i=0;i<100; i++) {
            TransferRequest request = TransferRequest.newBuilder()
                    .setFromAccount(ThreadLocalRandom.current().nextInt(1,11))
                    .setToAccount(ThreadLocalRandom.current().nextInt(1,11))
                    .setAmount(ThreadLocalRandom.current().nextInt(1,21))
                    .build();
            requestStreamObserver.onNext(request);
        }

        requestStreamObserver.onCompleted();
        latch.await();


    }

}
