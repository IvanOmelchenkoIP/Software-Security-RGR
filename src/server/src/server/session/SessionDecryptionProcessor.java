package server.session;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SessionDecryptionProcessor extends SessionCipherProcessor {
		
	@Override
	public void init(SessionKey sessionKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		cipher = Cipher.getInstance(SESSION_CIPHER_ALGORITHM);
		SecretKeySpec sessionKeySpec = new SecretKeySpec(sessionKey.getSessionKey(), SESSION_CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, sessionKeySpec);
	}
}
