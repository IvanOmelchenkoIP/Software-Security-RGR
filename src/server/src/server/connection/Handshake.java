package server.connection;

import java.io.IOException;
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

import server.session.SessionCipherMessageManager;
import server.session.SessionKey;
import server.session.SessionKeyGenerator;
import server.util.SecureRandomGeneratorUtil;

public class Handshake {
	
	private Server server;
	private SessionCipherMessageManager sessionCipherMessageManager;
	
	Handshake(Server server, SessionCipherMessageManager sessionCipherMessageManager) {
		this.server = server;
		this.sessionCipherMessageManager = sessionCipherMessageManager;
	}
	
	public void initiate(int helloMessageSize, int keyPairSize, String cipherAlgorithm) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecureRandomGeneratorUtil randomGenerator = new SecureRandomGeneratorUtil();
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
		
		SessionKey sessionKey = new SessionKey();
		sessionKey.setSessionKey(SessionKeyGenerator.generate(clientHello, serverHello, serverPremaster));
		sessionCipherMessageManager.init(sessionKey);
		
		String serverReady = "Server ready";
		String readyMessageEncoded = sessionCipherMessageManager.prepareToSend(serverReady);
		server.send(readyMessageEncoded);
		System.out.println("Sent server ready");
		
		String clientReadyEncoded = server.receive();
		String clientReady = sessionCipherMessageManager.extractReceived(clientReadyEncoded);
		System.out.println("Received ready message from client: " + clientReady);
		
		System.out.println("Exchanged encrypted messages. Handshake ended");
	}
}
