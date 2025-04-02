package server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Handshake {
	
	private static int HELLO_MESSAGE_SIZE = 32;

	private Server server;
	private RandomGenerator randomGenerator;
	
	Handshake(Server server, RandomGenerator randomGenerator) {
		this.server = server;
		this.randomGenerator = randomGenerator;
	}
	
	public void initiate() throws IOException {
		Encoder base64Encoder = Base64.getEncoder();
		Decoder base64Decoder = Base64.getDecoder();
		
		String clientHelloEncoded = server.receive();
		byte[] clientHello = base64Decoder.decode(clientHelloEncoded);
		System.out.println("Received client hello");
		
		byte[] serverHello = randomGenerator.generate(HELLO_MESSAGE_SIZE);
		String serverHelloEncoded = base64Encoder.encodeToString(serverHello);
		server.send(serverHelloEncoded);
		System.out.println("Sent server hello");
		
	}
}
