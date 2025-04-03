package client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Main {

	private static String IP = "127.0.0.1";
	private static int PORT = 10002;
	
	public static void main(String[] args) throws UnknownHostException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Client client = new Client();
		ClientProcessor clientProcessor = new ClientProcessor(IP, PORT, client);
		clientProcessor.process();
		
		// TODO Auto-generated method stub

	}

}
