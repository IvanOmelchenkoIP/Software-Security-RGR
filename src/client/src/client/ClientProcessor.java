package client;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ClientProcessor {

	private static final int HELLO_MESSAGE_SIZE = 32;
	private static final int PREMASTER_SIZE = 48;
	private static final String HANDSHAKE_ALGORITHM = "RSA";
	private static final String FILE_TO_READ = "../resources/data.txt";
	
	private String ip;
	private int port;
	private Client client;
	
	ClientProcessor(String ip, int port, Client client) {
		this.ip = ip;
		this.port = port;
		this.client = client;
	}
	
	public void process() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Encoder base64Encoder = Base64.getEncoder();
		Decoder base64Decoder = Base64.getDecoder();
		SessionKey sessionKey = new SessionKey();
		SessionEncryptor sessionEncryptor = new SessionEncryptor();
		SessionDecryptor sessionDecryptor = new SessionDecryptor();
		
		client.connect(ip, port);
		
		System.out.println("Connected to server");
		
		Handshake handshake = new Handshake(client, base64Encoder, base64Decoder, sessionKey, sessionEncryptor, sessionDecryptor);
		handshake.initiate(HELLO_MESSAGE_SIZE, PREMASTER_SIZE, HANDSHAKE_ALGORITHM);
		
		String fileContents = FileReader.read(FILE_TO_READ);
		byte[] fileContentsEncrypted = sessionEncryptor.encrypt(fileContents.getBytes());
		String fileContentsEncoded = base64Encoder.encodeToString(fileContentsEncrypted);
		client.send(fileContentsEncoded);
		System.out.println("Sensing file contents to the server:\n" + fileContents);
		
		String serverFileContentsEncrypted = client.receive();
		byte[] serverfileContentsEcoded = sessionDecryptor.decrypt(serverFileContentsEncrypted.getBytes());
		byte[] serverFileContentsBytes = base64Decoder.decode(serverfileContentsEcoded);
		String serverFileContents = new String(serverFileContentsBytes);
		System.out.println("Received file contents from server:\n" + serverFileContents);
		
		client.close();		
		
		System.out.println("Connection closed");
	}
	
}
