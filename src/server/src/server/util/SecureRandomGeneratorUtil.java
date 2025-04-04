package server.util;

import java.security.SecureRandom;

public class SecureRandomGeneratorUtil {
	
	private SecureRandom secureRandom;
	
	public SecureRandomGeneratorUtil() {
		secureRandom = new SecureRandom();
	}
	
	public byte[] generate(int bytesCount) {
		byte[] bytes = new byte[bytesCount];
		secureRandom.nextBytes(bytes);
		return bytes;
	}

}