package server;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SessionDecryptor {
	
	private static final String SESSION_CIPHER_ALGORITHM = "AES";
	private Cipher cipher;
	
	public void init(SessionKey sessionKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		cipher = Cipher.getInstance(SESSION_CIPHER_ALGORITHM);
		SecretKeySpec sessionKeySpec = new SecretKeySpec(sessionKey.getSessionKey(), SESSION_CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, sessionKeySpec);
	}
	
	public byte[] decrypt(byte[] encryptedBytes) throws IllegalBlockSizeException, BadPaddingException { 
		return cipher.doFinal(encryptedBytes);
	}

}
