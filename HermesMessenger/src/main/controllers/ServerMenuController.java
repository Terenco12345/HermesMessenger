package main.controllers;

import java.io.File;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;
import main.networking.Server;
import main.networking.messages.Message;
import utils.FileUtils;

public class ServerMenuController extends Controller{

	private Main _main;
	private Server _server;

	@FXML TextArea _outputArea;
	@FXML TextField _inputField;
	@FXML ListView<String> _ServerList;

	@Override
	public void injectData(Main main) {
		System.out.println("Injecting data model into ServerMenuController.");
		_main = main;
	}

	/**
	 * Creates an instance of a Server object.
	 * @param port The port to connect to.
	 * @return The instance of the Server object.
	 */
	public Server createServer(int port) {
		_server = new Server(port, _main);
		
		return _server;
	}

	/**
	 * Updates the chat log text area.
	 */
	public void updateChatlog() {
		List<String> chatlogList = FileUtils.getTextFileAsList(new File(FileUtils.getSettingsProperty("path")+"/server-chatlog.txt"));
		String chatlog = "";
		for(String s: chatlogList) {
			chatlog += s+"\n";
		}
		_outputArea.setText(chatlog);
	}

	/**
	 * Sends the message in the input field to all Clients.
	 */
	@FXML
	public void sendMessage() {
		String message = _inputField.getText();
		_server.sendToAll(new Message("Server", "all", message));
	}
}
