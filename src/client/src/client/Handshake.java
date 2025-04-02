package client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class Handshake {

	private static int HELLO_MESSAGE_SIZE = 32;
	
	private Client client;
	private RandomGenerator randomGenerator;
	
	Handshake(Client client, RandomGenerator randomGenerator) {
		this.client = client;
		this.randomGenerator = randomGenerator;
	}
	
	public void initiate() throws IOException {
		Encoder base64Encoder = Base64.getEncoder();
		Decoder base64Decoder = Base64.getDecoder();
		
		byte[] clientHello = randomGenerator.generate(HELLO_MESSAGE_SIZE);
		String clientHelloEncoded = base64Encoder.encodeToString(clientHello);
		client.send(clientHelloEncoded);
		System.out.println("Sent client hello to server");
		
		String serverHelloEncoded = client.receive();
		byte[] serverHello = base64Decoder.decode(serverHelloEncoded);
		System.out.println("Received server hello");
	}
}
