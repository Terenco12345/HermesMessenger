package main.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import main.networking.messages.Message;

public class Client {

	private Socket _socket;
	private String _ip;
	private int _port;

	private Thread _listeningThread;

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
		// Get the socket and IO
		try {
			_socket = new Socket(_ip, _port);
			System.out.println("Socket successfully created on "+_ip+":"+_port);

			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());
			
			System.out.println("Successfully established I/O connection.");
		} catch (ConnectException e){
			System.out.println("Couldn't connect, maybe server is offline?");
			e.printStackTrace();
			return;
		} catch (UnknownHostException e) {
			System.out.println("Address could not be found.");
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		// The listening thread
		_listeningThread = new Thread() {
			public void run() {
				try {
					Object o;
					while((o = _inputStream.readObject()) != null) {
						processMessage((Message) o);
					}
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
	 * Send a object to the server.
	 * @param message
	 */
	public void sendToServer(Message message){
		try {
			System.out.println("Sending message to server: "+message._message);
			_outputStream.writeObject(message);
		} catch (IOException e) {
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
			_inputStream.close();
			_outputStream.close();
			_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
