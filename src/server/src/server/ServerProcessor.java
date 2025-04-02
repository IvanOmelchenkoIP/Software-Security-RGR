package server;

import java.io.IOException;

public class ServerProcessor {

	private int port;
	private Server server;
	
	ServerProcessor(int port, Server server) {
		this.port = port;
		this.server = server;
	}
	
	public void process() throws IOException {
		RandomGenerator randomGenerator = new RandomGenerator();
		
		server.start(port);
		System.out.println("Started server");
		server.acceptConnection();
		System.out.println("Accepted connection from client");
		
		Handshake handshake = new Handshake(server, randomGenerator);
		handshake.initiate();
		
		server.close();
		System.out.println("Closing server");
	}
	
}
