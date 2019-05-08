package main.networking;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import main.Main;
import main.networking.messages.Message;
import utils.FileUtils;
import utils.TempDialogueUtils;

public class Client {
	private Main _main;
	
	private Socket _socket;
	private String _ip;
	private int _port;

	private String _displayName;

	private Thread _listeningThread;

	private ObjectInputStream _inputStream;
	private ObjectOutputStream _outputStream;

	public Client(String ip, int port, String displayName, Main main) {
		_main = main;
		_ip = ip;
		_port = port;
		_displayName = displayName;
	}

	/**
	 * Start the connection.
	 * @return Whether the connection was successful or not.
	 */
	public boolean startConnection() {
		// Get the socket and IO
		try {
			_socket = new Socket(_ip, _port);
			System.out.println("Socket successfully created on "+_ip+":"+_port);

			_outputStream = new ObjectOutputStream(_socket.getOutputStream());
			_inputStream = new ObjectInputStream(_socket.getInputStream());

			System.out.println("Successfully established I/O connection.");
		} catch (ConnectException e){
			System.out.println("Couldn't connect, maybe server is offline?");
			TempDialogueUtils.generateErrorMessage("Error trying to connect", "Server unavailable or offline.");
			return false;
		} catch (UnknownHostException e) {
			System.out.println("Address could not be found.");
			TempDialogueUtils.generateErrorMessage("Error trying to connect", "IP could not be determined.");
			return false;
		} catch (SocketException e) {
			System.out.println("Network is unreachable.");
			TempDialogueUtils.generateErrorMessage("Error trying to connect", "Address is unreachable.");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		checkIfDisplayNameTaken(_displayName);
		
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
		return true;
	}


	/**
	 * Gets display name.
	 */
	public String getDisplayName() {
		return _displayName;
	}
	
	/**
	 * Checks if the display name is taken.
	 * @param displayName
	 */
	public void checkIfDisplayNameTaken(String displayName) {
		System.out.println("Checking the display name "+displayName+" with the server...");
		_displayName = displayName;
		System.out.println("Display name is free!");
	}
	
	/**
	 * Send a object to the server.
	 * @param message
	 */
	public void sendToServer(Message message){
		try {
			System.out.println(message._sender+" to "+message._receiver+": "+message._message);
			FileUtils.writeLineInFile(message._sender+" to "+message._receiver+": "+message._message, new File(FileUtils.getSettingsProperty("path")+"/chatlog.txt"));
			_outputStream.writeObject(message);
			_main._clientMenuController.updateChatlog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Process a message received in the socket.
	 * @param message
	 */
	public void processMessage(Message message) {
		System.out.println(message._sender+" to "+message._receiver+": "+message._message);
		FileUtils.writeLineInFile(message._sender+" to "+message._receiver+": "+message._message, new File(FileUtils.getSettingsProperty("path")+"/chatlog.txt"));
		_main._clientMenuController.updateChatlog();
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
