package main.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;

public class Server {
	
	private int _port;
	private ServerSocket _serverSocket;
	private ObjectInputStream _inputStream;
	
	private boolean _acceptingNewClients;
	
	public Server(int port) {
		_acceptingNewClients = false;
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while(_acceptingNewClients) {
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
