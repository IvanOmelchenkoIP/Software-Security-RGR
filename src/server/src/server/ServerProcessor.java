package server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ServerProcessor {

	private static int HELLO_MESSAGE_SIZE = 32;
	private static int KEY_PAIR_SIZE = 2048;
	private static String KEY_PAIR_ALGORITHM = "RSA";
	
	private int port;
	private Server server;
	
	ServerProcessor(int port, Server server) {
		this.port = port;
		this.server = server;
	}
	
	public void process() throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		RandomGenerator randomGenerator = new RandomGenerator();
		
		server.start(port);
		System.out.println("Started server");
		server.acceptConnection();
		System.out.println("Accepted connection from client");
		
		Handshake handshake = new Handshake(server, randomGenerator);
		handshake.initiate(HELLO_MESSAGE_SIZE, KEY_PAIR_SIZE, KEY_PAIR_ALGORITHM);
		
		server.close();
		System.out.println("Closing server");
	}
	
}
