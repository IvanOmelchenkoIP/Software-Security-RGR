package client.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

	private Socket clientSocket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	public void connect(String ip, int port) throws IOException {
		clientSocket = new Socket(ip, port);
		outputStream = new DataOutputStream(clientSocket.getOutputStream());
		inputStream = new DataInputStream(clientSocket.getInputStream());
	}
	
	public void send(String message) throws IOException {
		outputStream.writeUTF(message);
	}
	
	public String receive() throws IOException {
		return inputStream.readUTF();
	}
	
	public void close() throws IOException {
		outputStream.close();
		inputStream.close();
		clientSocket.close();
	}
}
