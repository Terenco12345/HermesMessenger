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
		
	}
	
	@FXML
	public void join() {
		
	}
	
	@FXML
	public void quit() {
		_main.quit();
	}
}
