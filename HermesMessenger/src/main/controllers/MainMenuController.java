package main.controllers;

import javafx.fxml.FXML;
import main.Main;

public class MainMenuController extends Controller{

	private Main _main;
	
	@Override
	public void injectData(Main main) {
		System.out.println("Injecting data model into MainMenuController.");
		_main = main;
	}
	
	@FXML
	public void host() {
		_main.setSceneToServerSetupMenu();
	}
	
	@FXML
	public void join() {
		_main.setSceneToClientSetupMenu();
	}
	
	@FXML
	public void quit() {
		_main.quit();
	}
}
