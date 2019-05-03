package main.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import main.networking.messages.Message;

public class Client {
	
	private Socket _socket;
	private String _ip;
	private int _port;
	
	private Thread _listeningThread;
	private boolean _listening;
	
	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;
	
	public Client(String ip, int port) {
		_ip = ip;
		_port = port;
	}
	
	/**
	 * Start the connection.
	 */
	public void startConnection() {
		// Get the socket
		try {
			_socket = new Socket(_ip, _port);
			System.out.println("Socket successfully created.");
		} catch (UnknownHostException e) {
			System.out.println("Address could not be found.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		// Obtain all the streams of the socket
		try {
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// The listening thread
		_listening = false;
		_listeningThread = new Thread() {
			public void run() {
				try {
					// Keep listening until _listening == false
					Object o;
					while(_listening & (o = _inputStream.readObject()) != null) {
						processMessage((Message) o);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		};
		startListening();
	}
	
	/**
	 * Send a object to the server.
	 * @param message
	 */
	public void sendToServer(Message message){
		try {
			_outputStream.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Start the listening thread.
	 */
	public void startListening() {
		_listening = true;
		_listeningThread.start();
	}
	
	/**
	 * Stop the listening thread.
	 */
	public void stopListening() {
		_listening = false;
		try {
			_listeningThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Process a message received in the socket.
	 * @param message
	 */
	public void processMessage(Message message) {
		System.out.println(message._message);
	}
	
	/**
	 * Closes all streams and socket.
	 */
	public void close() {
		try {
			stopListening();
			_inputStream.close();
			_outputStream.close();
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
