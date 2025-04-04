package server.session;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SessionEncryptionManager {

	private SessionCipherProcessor sessionEncryptor;
	private SessionCipherProcessor sessionDecryptor;
	
	SessionEncryptionManager() {
		sessionEncryptor = new SessionEncryptionProcessor();
		sessionDecryptor = new SessionDecryptionProcessor();
	}
	
	public void init(SessionKey sessionKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
		sessionEncryptor.init(sessionKey);
		sessionDecryptor.init(sessionKey);
	}
	
	public byte[] encrypt(byte[] messageBytes) throws IllegalBlockSizeException, BadPaddingException {
		return sessionEncryptor.process(messageBytes);
	}
	
	public byte[] decrypt(byte[] encryptedBytes) throws IllegalBlockSizeException, BadPaddingException {
		return sessionDecryptor.process(encryptedBytes);
	}
}
