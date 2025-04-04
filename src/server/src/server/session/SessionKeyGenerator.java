package server.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SessionKeyGenerator {

	private static String algorithm = "SHA-256";
	
	public static byte[] generate(byte[] clientHello, byte[] serverHello, byte[] premaster) throws NoSuchAlgorithmException {
		byte[] secretBuffer = new byte[clientHello.length + serverHello.length + premaster.length];
		System.arraycopy(clientHello, 0, secretBuffer, 0, clientHello.length);
		System.arraycopy(serverHello, 0, secretBuffer, clientHello.length, serverHello.length);
		System.arraycopy(premaster, 0, secretBuffer, serverHello.length, premaster.length);		
		MessageDigest sha256Hash = MessageDigest.getInstance(algorithm);
		byte[] sessionKey = sha256Hash.digest(secretBuffer);
		return sessionKey;
	}
}
