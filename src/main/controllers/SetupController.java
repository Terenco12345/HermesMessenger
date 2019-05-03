package main.controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import main.Main;
import utils.FileUtils;

public class SetupController extends Controller{

	Main _main;
	@FXML TextField _pathField;

	@Override
	public void injectData(Main main) {
		_main = main;
		System.out.println("Injecting data model into SetupController.");

		_pathField.setText(System.getProperty("user.home")+"\\Documents");
	}

	@FXML
	public void chooseFile() {
		DirectoryChooser browser = new DirectoryChooser();
		browser.setTitle("JavaFX Projects");
		browser.setInitialDirectory(new File(_pathField.getText()));
		File selectedFile = browser.showDialog(_main.getStage());
		if (selectedFile != null) {
			_pathField.setText(selectedFile.getAbsolutePath());
	    }
	}
	
	@FXML
	public void next() {
		FileUtils.setSettingsProperty("path", _pathField.getText()+"\\HermesMessenger");
		System.out.println("Main directory set as "+FileUtils.getSettingsProperty("path")+". Moving onto main menu.");
		_main.setSceneToMainMenu();
	}
}
