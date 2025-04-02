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
		client.connect(ip, port);
		
		Handshake handshake = new Handshake(client);
		handshake.initiate();
		
		client.close();		
	}
	
}
