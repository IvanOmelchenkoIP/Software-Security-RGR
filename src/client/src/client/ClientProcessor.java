package client;

import java.io.IOException;

public class ClientProcessor {

	private String ip;
	private int port;
	private Client client;
	
	ClientProcessor(String ip, int port, Client client) {
		this.ip = ip;
		this.port = port;
		this.client = client;
	}
	
	public void process() throws IOException {
		RandomGenerator randomGenerator = new RandomGenerator();
		
		client.connect(ip, port);
		
		System.out.println("Connected to server");
		
		Handshake handshake = new Handshake(client, randomGenerator);
		handshake.initiate();
		
		client.close();		
		
		System.out.println("Connection closed");
	}
	
}
