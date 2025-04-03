package server;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Handshake {
	
	private Server server;
	private Encoder base64Encoder;
	private Decoder base64Decoder;
	private SessionKey sessionKey;
	private SessionEncryptor sessionEncryptor;
	private SessionDecryptor sessionDecryptor;
	
	Handshake(Server server, Encoder base64Encoder, Decoder base64Decoder, SessionKey sessionKey, SessionEncryptor sessionEncryptor, SessionDecryptor sessionDecryptor) {
		this.server = server;
		this.base64Encoder = base64Encoder;
		this.base64Decoder = base64Decoder;
		this.sessionKey = sessionKey;
		this.sessionEncryptor = sessionEncryptor;
		this.sessionDecryptor = sessionDecryptor;
	}
	
	public void initiate(int helloMessageSize, int keyPairSize, String cipherAlgorithm) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		RandomGenerator randomGenerator = new RandomGenerator();

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
		
		sessionKey.setSessionKey(SessionKeyGenerator.generate(clientHello, serverHello, serverPremaster));
		sessionEncryptor.init(sessionKey);
		sessionDecryptor.init(sessionKey);
		
		String readyMessage = "Server ready";
		byte[] readyMessageEncrypted = sessionEncryptor.encrypt(readyMessage.getBytes());
		String readyMessageEncoded = base64Encoder.encodeToString(readyMessageEncrypted);
		server.send(readyMessageEncoded);
		System.out.println("Sent server ready");
		
		String clientReadyEncoded = server.receive();
		byte[] clientReadyEncrypted = base64Decoder.decode(clientReadyEncoded);
		byte[] clientReadyBytes = sessionDecryptor.decrypt(clientReadyEncrypted);
		String clientReady = new String(clientReadyBytes);
		System.out.println("Received ready message from client: " + clientReady);
		
		System.out.println("Exchanged encrypted messages. Handshake ended");
	}
}
