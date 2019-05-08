package main.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.Main;
import main.networking.Server;
import utils.TempDialogueUtils;

public class ServerSetupMenuController extends Controller{

	private Main _main;
	private Server _server;
	
	@FXML TextField _idField;
	@FXML TextField _portField;
	
	@FXML ListView<String> _serverList;
	
	@Override
	public void injectData(Main main) {
		System.out.println("Injecting data model into ServerSetupMenuController.");
		_main = main;
	}
	
	@FXML
	public void host() {
		System.out.println("Attempting to host");
		
		int port = 0;
		try {
			port = Integer.parseInt(_portField.getText());
			
			if(!(port < 65566 & port > 0)) {
				TempDialogueUtils.generateErrorMessage("Port Error", "The port must be between 1 and 65565.");
			}
		} catch (NumberFormatException e) {
			System.out.println("User entered a port that wasn't a number.");
			TempDialogueUtils.generateErrorMessage("Port Error", "The port must be a number.");
			return;
		}
		
		_server = _main._serverMenuController.createServer(port);
		if(!_server.startServer()) {
			return;
		} else {
			_main.setSceneToServerMenu();
		}
		_main._serverMenuController.updateChatlog();
	}
	
	@FXML
	public void addServer() {
		
	}
}
