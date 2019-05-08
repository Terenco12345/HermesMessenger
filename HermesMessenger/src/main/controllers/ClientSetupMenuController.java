package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.Main;
import main.networking.Client;
import utils.TempDialogueUtils;

public class ClientSetupMenuController extends Controller{

	private Main _main;
	private Client _client;
	
	@FXML TextField _nameField;
	@FXML TextField _ipField;
	@FXML TextField _portField;
	
	@FXML ListView<String> _serverList;
	
	@Override
	public void injectData(Main main) {
		System.out.println("Injecting data model into ClientSetupMenuController.");
		_main = main;
	}
	
	@FXML
	public void connect() {
		System.out.println("Attempting to connect");
		String displayName = "";
		String ip = "";
		int port = 0;
		try {
			displayName = _nameField.getText();
			ip = _ipField.getText();
			port = Integer.parseInt(_portField.getText());
			
			if(ip == "" || port == 0 || displayName == "") {
				TempDialogueUtils.generateErrorMessage("One or more empty fields", "Please fill in all fields.");
			}
			
			if(!(port < 65566 & port > 0)) {
				TempDialogueUtils.generateErrorMessage("Port Error", "The port must be between 1 and 65565.");
			}
		} catch (NumberFormatException e) {
			System.out.println("User entered a port that wasn't a number.");
			TempDialogueUtils.generateErrorMessage("Port Error", "The port must be a number.");
			return;
		}
		
		_client = _main._clientMenuController.createClient(ip, port, displayName);
		if(!_client.startConnection()) {
			return;
		} else {
			_main.setSceneToClientMenu();
		}
		
		_main._clientMenuController.updateChatlog();
	}
	
	@FXML
	public void addServer() {
		
	}
}
