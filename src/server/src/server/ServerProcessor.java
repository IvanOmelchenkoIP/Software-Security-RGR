package server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ServerProcessor {

	private static final int HELLO_MESSAGE_SIZE = 32;
	private static final int KEY_PAIR_SIZE = 2048;
	private static final String KEY_PAIR_ALGORITHM = "RSA";
	private static final String FILE_TO_READ = "../resources/data.txt";
	
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
		
		String fileContents = FileReader.read(FILE_TO_READ);
		byte[] fileContentsEncrypted = sessionEncryptor.encrypt(fileContents.getBytes());
		String fileContentsEncoded = base64Encoder.encodeToString(fileContentsEncrypted);
		server.send(fileContentsEncoded);
		System.out.println("Sending file contents to client:\n" + fileContents);
		
		String clientFileContentsEncoded = server.receive();
		byte[] clientFileContentsEncrypted = base64Decoder.decode(clientFileContentsEncoded);
		byte[] clientFileContentsBytes = sessionDecryptor.decrypt(clientFileContentsEncrypted);
		String clientFileContents = new String(clientFileContentsBytes);
		System.out.println("Receiving file contents from client:\n" + clientFileContents);
		
		server.close();
		System.out.println("Closing server");
	}
	
}
