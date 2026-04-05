package main

import (
	"context"
	"fmt"
	"log"
	"net"

	"example.com/proto/models/helloworld"
	"google.golang.org/grpc"
)

type ServerHandler struct {
	helloworld.UnimplementedHelloServiceServer
}

// SayHello implements helloworld.HelloServiceServer.
func (s *ServerHandler) SayHello(ctx context.Context, in *helloworld.Message) (*helloworld.Message, error) {
	log.Printf("Received message body from client: %s", in.Body)
	return &helloworld.Message{Body: "Hello from server!"}, nil
}

func main() {
	fmt.Println("Starting gRPC server!")

	listener, err := net.Listen("tcp", ":9000")

	if err != nil {
		log.Fatalf("Error with server connection: %v", err)
	}

	s := ServerHandler{}

	grpcServer := grpc.NewServer()

	helloworld.RegisterHelloServiceServer(grpcServer, &s)

	if err := grpcServer.Serve(listener) ; err != nil {
		log.Fatalf("Error serving response: %v", err)
	}
}
