package main.controllers;

import java.io.File;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;
import main.networking.Client;
import main.networking.messages.Message;
import utils.FileUtils;

public class ClientMenuController extends Controller{

	private Main _main;
	private Client _client;

	@FXML TextArea _outputArea;
	@FXML TextField _inputField;
	@FXML ListView<String> _clientList;

	@Override
	public void injectData(Main main) {
		System.out.println("Injecting data model into ClientMenuController.");
		_main = main;
	}

	/**
	 * Creates an instance of a Client object.
	 * @param ip The IP to connect to.
	 * @param port The port to connect to.
	 * @return The instance of the Client object.
	 */
	public Client createClient(String ip, int port, String displayName) {
		_client = new Client(ip, port, displayName, _main);
		return _client;
	}

	/**
	 * Updates the chat log text area.
	 */
	public void updateChatlog() {
		List<String> chatlogList = FileUtils.getTextFileAsList(new File(FileUtils.getSettingsProperty("path")+"/chatlog.txt"));
		String chatlog = "";
		for(String s: chatlogList) {
			chatlog += s+"\n";
		}
		_outputArea.selectPositionCaret(_outputArea.getLength()); 
		_outputArea.deselect(); 
		_outputArea.setText(chatlog);
	}

	/**
	 * Sends the message in the input field to the server.
	 */
	@FXML
	public void sendMessage() {
		String message = _inputField.getText();
		_client.sendToServer(new Message(_client.getDisplayName(), "Server", message));
	}
}
