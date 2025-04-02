package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	
	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	public void acceptConnection() throws IOException {
		clientSocket = serverSocket.accept();
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
		serverSocket.close();
	}
}
