package client;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Handshake {
	
	private Client client;
	private Encoder base64Encoder;
	private Decoder base64Decoder;
	private SessionKey sessionKey;
	private SessionEncryptor sessionEncryptor;
	private SessionDecryptor sessionDecryptor;
	
	Handshake(Client client, Encoder base64Encoder, Decoder base64Decoder, SessionKey sessionKey, SessionEncryptor sessionEncryptor, SessionDecryptor sessionDecryptor) {
		this.client = client;
		this.base64Encoder = base64Encoder;
		this.base64Decoder = base64Decoder;
		this.sessionKey = sessionKey;
		this.sessionEncryptor = sessionEncryptor;
		this.sessionDecryptor = sessionDecryptor;
	}
	
	public void initiate(int helloMessageSize, int premasterSize, String cipherAlgorithm) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		RandomGenerator randomGenerator = new RandomGenerator();
		
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
		
		sessionKey.setSessionKey(SessionKeyGenerator.generate(clientHello, serverHello, premaster));
		sessionEncryptor.init(sessionKey);
		sessionDecryptor.init(sessionKey);
		
		String serverReadyEncoded = client.receive();
		byte[] serverReadyEncrypted = base64Decoder.decode(serverReadyEncoded);
		byte[] serverReadyBytes = sessionDecryptor.decrypt(serverReadyEncrypted);
		String serverReady = new String(serverReadyBytes);
		System.out.println("Received ready message from server: " + serverReady);
		
		String readyMessage = "Client ready";
		byte[] readyMessageEncrypted = sessionEncryptor.encrypt(readyMessage.getBytes());
		String readyMessageEncoded = base64Encoder.encodeToString(readyMessageEncrypted);
		client.send(readyMessageEncoded);
		System.out.println("Sent client ready");
		
		System.out.println("Exchanged encrypted messages. Handshake ended");
	}
}
