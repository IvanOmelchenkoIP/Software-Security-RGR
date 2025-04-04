package server;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import server.connection.Server;
import server.connection.ServerProcessor;

public class Main {

	private static int PORT = 10000;
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
		
		Server server = new Server();
		ServerProcessor serverProcessor = new ServerProcessor(PORT, server);
		serverProcessor.process();
	}

}
