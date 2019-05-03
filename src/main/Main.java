package main;

import java.io.File;
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

public class Main extends Application {

	private Stage _window;
	private Scene _setupScene, _mainMenuScene;
	private SetupController _setupSceneController;
	private MainMenuController _mainMenuController;
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
		FXMLLoader setupSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/Setup.fxml"));
		Parent setupPane = setupSceneLoader.load();
		_setupScene = new Scene(setupPane, 800, 450);

		FXMLLoader mainMenuSceneLoader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource("fxmlFiles/MainMenu.fxml"));
		Parent mainMenuPane = mainMenuSceneLoader.load();
		_mainMenuScene = new Scene(mainMenuPane, 800, 450);

		// Set the closing operation of the stage
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				quit();
			}
		});

		// Inject the data model into all controllers
		_setupSceneController = setupSceneLoader.getController();
		_controllers.add(_setupSceneController);

		_mainMenuController = mainMenuSceneLoader.getController();
		_controllers.add(_mainMenuController);
		
		for(Controller c: _controllers) {
			c.injectData(this);
		}
		
		// Check for first time setup
		if(FileUtils.getSettingsProperty("path").equals("")) {
			System.out.println("No path found in settings.properties.");
			System.out.println("Starting first time setup.");
			setSceneToSetup();
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
	 * Checks if all necessary files and folders are present.
	 * -- UNIMPLEMENTED --
	 */
	public void repairFiles() {
		
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
	public void setSceneToSetup() { 
		_window.setWidth(442);
		_window.setHeight(130);
		_window.setScene(_setupScene); 
	}
	
	/**
	 * Change the current scene to the MainMenu scene.
	 */
	public void setSceneToMainMenu() { 
		_window.setWidth(800);
		_window.setHeight(400);
		_window.setScene(_mainMenuScene); 
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
