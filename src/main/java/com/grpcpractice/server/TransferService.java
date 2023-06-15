package com.grpcpractice.server;

import com.grpcpractice.models.TransferRequest;
import com.grpcpractice.models.TransferResponse;
import com.grpcpractice.models.TransferServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {
    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
