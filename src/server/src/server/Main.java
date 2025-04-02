package server;

import java.io.IOException;

public class Main {

	private static int PORT = 10002;
	
	public static void main(String[] args) throws IOException {
		Server server = new Server();
		ServerProcessor serverProcessor = new ServerProcessor(PORT, server);
		serverProcessor.process();
		/*Server server = new Server();
		server.start(PORT);
		server.acceptConnection();
		
		String messageFromClient = server.receive();
		System.out.println(messageFromClient);
		
		String message = "Message from server";
		server.send(message);
		
		server.close();*/
		
		// TODO Auto-generated method stub

	}

}
