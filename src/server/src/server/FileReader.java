package server;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileReader {

	public static String read(String file) throws IOException {
		DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
		String contents = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		inputStream.close();
		return contents;
	}
}
