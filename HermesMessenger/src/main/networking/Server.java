package main.networking;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import main.Main;
import main.networking.messages.Message;
import utils.FileUtils;

public class Server {

	private Main _main;
	private Server _server;
	private boolean _serverSuccess;
	
	private int _port;
	private ServerSocket _serverSocket;

	private Thread _clientAcceptorThread;

	protected List<ClientHandler> _clientList = new ArrayList<ClientHandler>();

	public Server(int port, Main main) {
		_server = this;
		_port = port;
		_main = main;
	}

	/**
	 * Starts the server.
	 */
	public boolean startServer() {
		
		_serverSuccess = true;
		try {
			_serverSocket = new ServerSocket(_port);
			System.out.println("Server socket established.");
		} catch (IOException e) {
			e.printStackTrace();
			_serverSuccess = false;
		}

		_clientAcceptorThread = new Thread() {
			public void run() {
				while(true) {
					try {
						Socket clientSocket = _serverSocket.accept();
						System.out.println("New client connected, "+clientSocket.getInetAddress());
						_clientList.add(new ClientHandler(clientSocket, _server, _main));
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						_serverSuccess = false;
					} catch (IOException e) {
						e.printStackTrace();
						_serverSuccess = false;
					}
				}
			}
		};

		_clientAcceptorThread.start();
		return _serverSuccess;
	}

	/**
	 * Handle a message.
	 */
	public void handleMessage(Message message) {
		System.out.println(message._sender+" to "+message._receiver+": "+message._message);
		FileUtils.writeLineInFile(message._sender+" to "+message._receiver+": "+message._message, new File(FileUtils.getSettingsProperty("path")+"/server-chatlog.txt"));
		
		_main._serverMenuController.updateChatlog();
	}

	/**
	 * Sends a message to all clients.
	 */
	public void sendToAll(Message message) {
		System.out.println(message._sender+" to all: "+message._message);
		FileUtils.writeLineInFile(message._sender+" to all: "+message._message, new File(FileUtils.getSettingsProperty("path")+"/server-chatlog.txt"));
		for(ClientHandler c: _clientList) {
			c.sendToClient(message);
		}
		_main._serverMenuController.updateChatlog();
	}
	
	/**
	 * Closes all streams.
	 */
	public void close() {
		for(ClientHandler c: _clientList) {
			try {
				c._socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

class ClientHandler {
	private Main _main;
	private ClientHandler _clientHandler;
	
	public Socket _socket;
	public ObjectInputStream _inputStream;
	public ObjectOutputStream _outputStream;

	public String _ip;
	public int _port;
	
	public boolean _listening;
	public Thread _listeningThread;

	public ClientHandler(Socket socket, Server server, Main main) {
		_main = main;
		_clientHandler = this;
		
		try {
			_socket = socket;

			_ip = socket.getInetAddress().getHostAddress();
			_port = socket.getPort();
			
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());

			System.out.println("Client socket "+socket.getInetAddress()+" IO established.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		_listeningThread = new Thread() {
			public void run() {
				try {
					while(true) {
						Object o;
						while((o = _inputStream.readObject()) != null) {
							System.out.println("Message received.");
							server.handleMessage(((Message) o));
						}
					}
				} catch (SocketException e){
					System.out.println("Connection from "+_ip+":"+_port+" was forcibly closed.");
					server._clientList.remove(_clientHandler);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		};
		_listeningThread.start();
	}

	/**
	 * Send a object to the client.
	 * @param message
	 */
	public void sendToClient(Message message){
		try {
			System.out.println("Sending message to server: "+message._message);
			_outputStream.writeObject(message);
			_main._serverMenuController.updateChatlog();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Close all streams.
	 */
	public void close() {
		try {
			_listening = false;
			_inputStream.close();
			_listeningThread.join();
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}