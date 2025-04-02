package client;

import java.security.SecureRandom;

public class RandomGenerator {
	
	private SecureRandom secureRandom;
	
	RandomGenerator() {
		secureRandom = new SecureRandom();
	}
	
	public byte[] generate(int bytesCount) {
		byte[] bytes = new byte[bytesCount];
		secureRandom.nextBytes(bytes);
		return bytes;
	}

}
