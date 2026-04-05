package com.example.grpc;

import com.example.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.logging.Logger;

public class HelloClient {
    private static final int PORT = 9000;
    private static final Logger logger = Logger.getLogger(HelloClient.class.getName());

    public static void main(String[] args) {
        logger.info("Starting client");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", PORT)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);

        Message msg = stub.sayHello(Message.newBuilder().setBody("Hello from client!").build());

        logger.info("Server says: " + msg.getBody());
    }
}
