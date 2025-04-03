package client;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ClientProcessor {

	private static int HELLO_MESSAGE_SIZE = 32;
	private static int PREMASTER_SIZE = 48;
	private static String SECURITY_ALGORITHM = "RSA";
	
	private String ip;
	private int port;
	private Client client;
	
	ClientProcessor(String ip, int port, Client client) {
		this.ip = ip;
		this.port = port;
		this.client = client;
	}
	
	public void process() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		RandomGenerator randomGenerator = new RandomGenerator();
		
		client.connect(ip, port);
		
		System.out.println("Connected to server");
		
		Handshake handshake = new Handshake(client, randomGenerator);
		handshake.initiate(HELLO_MESSAGE_SIZE, PREMASTER_SIZE, SECURITY_ALGORITHM);
		
		client.close();		
		
		System.out.println("Connection closed");
	}
	
}
