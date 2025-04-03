package client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Handshake {
	
	private Client client;
	private RandomGenerator randomGenerator;
	
	Handshake(Client client, RandomGenerator randomGenerator) {
		this.client = client;
		this.randomGenerator = randomGenerator;
	}
	
	public void initiate(int helloMessageSize, int premasterSize, String cipherAlgorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Encoder base64Encoder = Base64.getEncoder();
		Decoder base64Decoder = Base64.getDecoder();
		
		byte[] clientHello = randomGenerator.generate(helloMessageSize);
		String clientHelloEncoded = base64Encoder.encodeToString(clientHello);
		client.send(clientHelloEncoded);
		System.out.println("Sent client hello to server");
		
		String serverHelloEncoded = client.receive();
		byte[] serverHello = base64Decoder.decode(serverHelloEncoded);
		System.out.println("Received server hello");
		
		String publicKeyEncoded = client.receive();
		byte[] publicKey = base64Decoder.decode(publicKeyEncoded);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(cipherAlgorithm);
		PublicKey clientPublicKey = keyFactory.generatePublic(publicKeySpec);
		System.out.println("Received public key from server");
		
		byte[] premaster = randomGenerator.generate(premasterSize);
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
		byte[] premasterCipher = cipher.doFinal(premaster);
		String premasterEncoded = base64Encoder.encodeToString(premasterCipher);
		client.send(premasterEncoded);
		System.out.println("Sent premaster key to server");
		
	}
}
