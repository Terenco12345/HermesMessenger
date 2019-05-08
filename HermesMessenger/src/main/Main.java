package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.controllers.*;
import utils.FileUtils;

public class Main extends Application{

	private Stage _window;
	private Scene _setupMenuScene, _mainMenuScene,_clientSetupMenuScene, _clientMenuScene, _serverSetupMenuScene, _serverMenuScene;
	
	public SetupMenuController _setupMenuController;
	public MainMenuController _mainMenuController;
	public ClientMenuController _clientMenuController;
	public ClientSetupMenuController _clientSetupMenuController;
	public ServerMenuController _serverMenuController;
	public ServerSetupMenuController _serverSetupMenuController;
	private List<Controller> _controllers;

	@Override
	public void start(Stage window) throws Exception{
		
		// Check if settings.properties exists.
		File settings = new File(".\\settings.properties");
		if(!settings.exists()) {
			settings.createNewFile();
		}
		
		// Initialization
		_window = window;
		_controllers = new ArrayList<Controller>();
		
		// Load all the scenes
		FXMLLoader setupMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/SetupMenu.fxml"));
		Parent setupMenuPane = setupMenuSceneLoader.load();
		_setupMenuScene = new Scene(setupMenuPane, 480, 130);

		FXMLLoader mainMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/MainMenu.fxml"));
		Parent mainMenuPane = mainMenuSceneLoader.load();
		_mainMenuScene = new Scene(mainMenuPane, 800, 400);
		
		FXMLLoader clientSetupMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/ClientSetupMenu.fxml"));
		Parent clientSetupMenuPane = clientSetupMenuSceneLoader.load();
		_clientSetupMenuScene = new Scene(clientSetupMenuPane, 800, 400);
		
		FXMLLoader clientMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/ClientMenu.fxml"));
		Parent clientMenuPane = clientMenuSceneLoader.load();
		_clientMenuScene = new Scene(clientMenuPane, 800, 450);
		
		FXMLLoader serverSetupMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/ServerSetupMenu.fxml"));
		Parent serverSetupMenuPane = serverSetupMenuSceneLoader.load();
		_serverSetupMenuScene = new Scene(serverSetupMenuPane, 800, 400);
		
		FXMLLoader serverMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/ServerMenu.fxml"));
		Parent serverMenuPane = serverMenuSceneLoader.load();
		_serverMenuScene = new Scene(serverMenuPane, 800, 450);


		// Set the closing operation of the stage
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				quit();
			}
		});

		// Inject the data model into all controllers
		_setupMenuController = setupMenuSceneLoader.getController();
		_controllers.add(_setupMenuController);

		_mainMenuController = mainMenuSceneLoader.getController();
		_controllers.add(_mainMenuController);
		
		_clientSetupMenuController = clientSetupMenuSceneLoader.getController();
		_controllers.add(_clientSetupMenuController);
		
		_clientMenuController = clientMenuSceneLoader.getController();
		_controllers.add(_clientMenuController);
		
		_serverSetupMenuController = serverSetupMenuSceneLoader.getController();
		_controllers.add(_serverSetupMenuController);
		
		_serverMenuController = serverMenuSceneLoader.getController();
		_controllers.add(_serverMenuController);
		
		for(Controller c: _controllers) {
			c.injectData(this);
		}
		
		// Check for first time setup
		if(FileUtils.getSettingsProperty("path").equals("")) {
			System.out.println("No path found in settings.properties.");
			System.out.println("Starting first time setup.");
			setSceneToSetupMenu();
		} else {
			System.out.println("Path found in settings.properties.");
			System.out.println("Path is "+FileUtils.getSettingsProperty("path")+".");
			repairFiles();
			setSceneToMainMenu();
		}
		
		window.setTitle("Hermes Messenger");
		window.setResizable(false);
		window.show();
	}

	/**
	 * Checks if all necessary files and folders are present, and replaces all missing files.
	 */
	public void repairFiles() {
		File chatlogFile = new File(FileUtils.getSettingsProperty("path")+"/chatlog.txt");
		System.out.println("Trying to find chatlog...");
		if(!chatlogFile.exists()) {
			try {
				System.out.println("Could not find chatlog, creating new one.");
				chatlogFile.getParentFile().mkdirs();
				chatlogFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Found!");
		}
		
		File serverChatlogFile = new File(FileUtils.getSettingsProperty("path")+"/server-chatlog.txt");
		System.out.println("Trying to find server chatlog...");
		if(!serverChatlogFile.exists()) {
			try {
				System.out.println("Could not find server chatlog, creating new one.");
				serverChatlogFile.getParentFile().mkdirs();
				serverChatlogFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Found!");
		}
	}
	
	/**
	 * Returns the application's stage object.
	 * @return The application's stage.
	 */
	public Stage getStage(){
		return _window;
	}

	// Scene changing methods
	/**
	 * Change the current scene to the Setup scene.
	 */
	public void setSceneToSetupMenu() { 
		_window.setWidth(480);
		_window.setHeight(130);
		_window.setScene(_setupMenuScene); 
	}
	
	/**
	 * Change the current scene to the MainMenu scene.
	 */
	public void setSceneToMainMenu() { 
		_window.setWidth(800);
		_window.setHeight(600);
		_window.setScene(_mainMenuScene); 
	}

	/**
	 * Change the current scene to the ClientSetupMenu scene.
	 */
	public void setSceneToClientSetupMenu() {
		_window.setWidth(800);
		_window.setHeight(600);
		_window.setScene(_clientSetupMenuScene);
	}
	
	/**
	 * Change the current scene to the ClientSetupMenu scene.
	 */
	public void setSceneToClientMenu() {
		_window.setWidth(800);
		_window.setHeight(600);
		_window.setScene(_clientMenuScene);
	}
	
	/**
	 * Change the current scene to the ServerSetupMenu scene.
	 */
	public void setSceneToServerSetupMenu() {
		_window.setWidth(800);
		_window.setHeight(600);
		_window.setScene(_serverSetupMenuScene);
	}
	
	/**
	 * Change the current scene to the ServerSetupMenu scene.
	 */
	public void setSceneToServerMenu() {
		_window.setWidth(800);
		_window.setHeight(600);
		_window.setScene(_serverMenuScene);
	}
	
	/**
	 * Quits the application, and closes all active streams.
	 */
	public void quit() {
		System.out.println("Quitting application.");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
