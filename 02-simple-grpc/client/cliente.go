package main

import (
	"context"
	"log"

	"example.com/proto/models/helloworld"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func main() {
	var conn *grpc.ClientConn

	conn, err := grpc.NewClient(":9000", grpc.WithTransportCredentials(insecure.NewCredentials()))

	if err != nil {
		log.Fatalf("Client could not connect: %v", err)
	}

	defer conn.Close()

	c := helloworld.NewHelloServiceClient(conn)

	res, err := c.SayHello(context.Background(), &helloworld.Message{Body: "Hello from client!"})

	if err != nil {
		log.Fatalf("Error while calling SayHello from client: %v", err)
	}

	log.Printf("Server response: %s", res.Body)
}
