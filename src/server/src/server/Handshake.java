package server;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Handshake {
	
	private Server server;
	private RandomGenerator randomGenerator;
	
	Handshake(Server server, RandomGenerator randomGenerator) {
		this.server = server;
		this.randomGenerator = randomGenerator;
	}
	
	public void initiate(int helloMessageSize, int keyPairSize, String cipherAlgorithm) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Encoder base64Encoder = Base64.getEncoder();
		Decoder base64Decoder = Base64.getDecoder();
		
		String clientHelloEncoded = server.receive();
		byte[] clientHello = base64Decoder.decode(clientHelloEncoded);
		System.out.println("Received client hello");
		
		byte[] serverHello = randomGenerator.generate(helloMessageSize);
		String serverHelloEncoded = base64Encoder.encodeToString(serverHello);
		server.send(serverHelloEncoded);
		System.out.println("Sent server hello");
		
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(cipherAlgorithm);
		keyPairGenerator.initialize(keyPairSize);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		PublicKey publicKey = keyPair.getPublic();
		String publicKeyEncoded = base64Encoder.encodeToString(publicKey.getEncoded());
		server.send(publicKeyEncoded);
		System.out.println("Sent public key to client");
		
		
		String premasterEncoded = server.receive();
		byte[] premaster = base64Decoder.decode(premasterEncoded);
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
		byte[] serverPremaster = cipher.doFinal(premaster);
		System.out.println("Received premaster from client");
		
	}
}
