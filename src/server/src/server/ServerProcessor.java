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
		server.start(port);
		server.acceptConnection();
		
		Handshake handshake = new Handshake(server);
		handshake.initiate();
		
		server.close();
	}
	
}
