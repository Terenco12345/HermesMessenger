package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TempDialogueUtils {
	public static void generateErrorMessage(String title, String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(title);
		alert.setContentText(message);

		alert.showAndWait();
	}
}
