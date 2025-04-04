package server.session;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SessionCipherMessageManager {

	private SessionEncryptionManager sessionEncryptionManager;
	private Encoder base64Encoder;
	private Decoder base64Decoder;
	
	public void init(SessionKey sessionKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		sessionEncryptionManager = new SessionEncryptionManager();
		sessionEncryptionManager.init(sessionKey);
		base64Encoder = Base64.getEncoder();
		base64Decoder = Base64.getDecoder();
	}
	
	public String prepareToSend(String message) throws IllegalBlockSizeException, BadPaddingException {
		byte[] messageEncrypted = sessionEncryptionManager.encrypt(message.getBytes());
		String messageEncoded = base64Encoder.encodeToString(messageEncrypted);
		return messageEncoded;
	}
	
	public String extractReceived(String receivedMessage) throws IllegalBlockSizeException, BadPaddingException {
		byte[] receivedMessageEncrypted = base64Decoder.decode(receivedMessage);
		byte[] receivedMessageDecrypted = sessionEncryptionManager.decrypt(receivedMessageEncrypted);
		String extractedMessage = new String(receivedMessageDecrypted);
		return extractedMessage;
	}
}
