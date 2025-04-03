package server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

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
		Encoder base64Encoder = Base64.getEncoder();
		Decoder base64Decoder = Base64.getDecoder();
		SessionKey sessionKey = new SessionKey();
		SessionEncryptor sessionEncryptor = new SessionEncryptor();
		SessionDecryptor sessionDecryptor = new SessionDecryptor();
		
		server.start(port);
		System.out.println("Started server");
		server.acceptConnection();
		System.out.println("Accepted connection from client");
		
		Handshake handshake = new Handshake(server, base64Encoder, base64Decoder, sessionKey, sessionEncryptor, sessionDecryptor);
		handshake.initiate(HELLO_MESSAGE_SIZE, KEY_PAIR_SIZE, KEY_PAIR_ALGORITHM);
		
		server.close();
		System.out.println("Closing server");
	}
	
}
