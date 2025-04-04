package server.session;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public abstract class SessionCipherProcessor {
	
	protected static final String SESSION_CIPHER_ALGORITHM = "AES";

	protected Cipher cipher;
	
	public abstract void init(SessionKey sessionKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException;
	
	public byte[] process(byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
		return cipher.doFinal(bytes);
	}
}
