package  com.example.grpc;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class HelloServer {
    private static final int PORT = 9000;
    private static final Logger logger = Logger.getLogger(HelloServer.class.getName());

    public static void main(String[] args) {
        Server server = Grpc.newServerBuilderForPort(PORT, InsecureServerCredentials.create()).addService(new HelloServerImp()).build();

        try {
            server.start();
            logger.info("Starting server in :"+PORT);
            server.awaitTermination();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error starting server: "+e.toString());
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Server interrupted: "+e.toString());
        }
    }

    static class HelloServerImp extends com.example.grpc.HelloServiceGrpc.HelloServiceImplBase {
        @Override
        public void sayHello(Message request, StreamObserver<Message> responseObserver) {
            Message response = Message.newBuilder()
                    .setBody("Hello from server!")
                    .build();
            logger.info("Client says: " + request.getBody());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
