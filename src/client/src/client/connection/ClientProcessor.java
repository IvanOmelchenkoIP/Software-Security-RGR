package client.connection;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import client.session.SessionCipherMessageManager;
import client.util.FileReaderUtil;

public class ClientProcessor {

	private static final int HELLO_MESSAGE_SIZE = 32;
	private static final int PREMASTER_SIZE = 48;
	private static final String HANDSHAKE_ALGORITHM = "RSA";
	private static final String FILE_TO_READ = "../resources/data.txt";
	
	private String ip;
	private int port;
	private Client client;
	
	public ClientProcessor(String ip, int port, Client client) {
		this.ip = ip;
		this.port = port;
		this.client = client;
	}
	
	public void process() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {		
		SessionCipherMessageManager sessionCipherMessageManager = new SessionCipherMessageManager();
		
		client.connect(ip, port);
		
		System.out.println("Connected to server");
		
		Handshake handshake = new Handshake(client, sessionCipherMessageManager);
		handshake.initiate(HELLO_MESSAGE_SIZE, PREMASTER_SIZE, HANDSHAKE_ALGORITHM);
		
		String clientfileContents = FileReaderUtil.read(FILE_TO_READ);
		String clientFileContentsEncoded = sessionCipherMessageManager.prepareToSend(clientfileContents);
		client.send(clientFileContentsEncoded);
		System.out.println("Sensing file contents to the server:\n" + clientfileContents);
		
		String serverFileContentsEncoded = client.receive();
		String serverFileContents = sessionCipherMessageManager.extractReceived(serverFileContentsEncoded);
		System.out.println("Received file contents from server:\n" + serverFileContents);
		
		client.close();		
		
		System.out.println("Connection closed");
	}
}
