package client;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

	private static String IP = "127.0.0.1";
	private static int PORT = 10002;
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client();
		client.connect(IP, PORT);
		String message = "Message from client";
		client.send(message);
		
		String messageFromServer = client.receive();
		System.out.println(messageFromServer);
		
		client.close();
		
		// TODO Auto-generated method stub

	}

}
