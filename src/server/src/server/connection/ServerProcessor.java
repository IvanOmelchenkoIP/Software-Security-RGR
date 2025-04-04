package server.connection;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import server.session.SessionCipherMessageManager;
import server.util.FileReaderUtil;

public class ServerProcessor {

	private static final int HELLO_MESSAGE_SIZE = 32;
	private static final int KEY_PAIR_SIZE = 2048;
	private static final String KEY_PAIR_ALGORITHM = "RSA";
	private static final String FILE_TO_READ = "./resources/data.txt";
	
	private int port;
	private Server server;
	
	public ServerProcessor(int port, Server server) {
		this.port = port;
		this.server = server;
	}
	
	public void process() throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SessionCipherMessageManager sessionCipherMessageManager = new SessionCipherMessageManager();
		
		server.start(port);
		System.out.println("Started server at port " + port);
		server.acceptConnection();
		System.out.println("Accepted connection from client");
		
		Handshake handshake = new Handshake(server, sessionCipherMessageManager);
		handshake.initiate(HELLO_MESSAGE_SIZE, KEY_PAIR_SIZE, KEY_PAIR_ALGORITHM);
		
		String serverFileContents = FileReaderUtil.read(FILE_TO_READ);
		String serverFileContentsEncoded = sessionCipherMessageManager.prepareToSend(serverFileContents);
		server.send(serverFileContentsEncoded);
		System.out.println("Sending file contents to client:\n\"\"\"\n" + serverFileContents + "\n\"\"\"");
		
		String clientFileContentsEncoded = server.receive();
		String clientFileContents = sessionCipherMessageManager.extractReceived(clientFileContentsEncoded);
		System.out.println("Receiving file contents from client:\n\"\"\"\n" + clientFileContents + "\n\"\"\"");
		
		server.close();
		
		System.out.println("Closing server");
	}
	
}
