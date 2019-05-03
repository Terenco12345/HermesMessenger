package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import main.Main;

public class FileUtils {
	public static Properties _settings = new Properties();
	
	/**
	 * Gets the property for a specified key in the settings.properties file.
	 * @param key The desired key.
	 * @return The value associated with the key.
	 */
	public static String getSettingsProperty(String key) {
		try {
			File settingsFile = new File(".\\settings.properties");
			FileInputStream settingsStream = new FileInputStream(settingsFile);
			_settings.load(settingsStream);
			settingsStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(!_settings.containsKey(key)) {
			writeNewProperty(key, "");
		}
		return (String) _settings.get(key);
	}
	
	/**
	 * Sets a property for a specified key in the settings.properties file.
	 * @param key The desired key.
	 * @param value The value to be set.
	 */
	public static void setSettingsProperty(String key, String value) {
		try {
			File settingsFile = new File(".\\settings.properties");
			FileOutputStream out = new FileOutputStream(settingsFile);
			_settings.setProperty(key, value);
			_settings.store(out, null);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write a new property in settings.properties
	 */
	public static void writeNewProperty(String key, String value) {
		try {
			File settingsFile = new File(".\\settings.properties");
			BufferedWriter out = new BufferedWriter(new FileWriter(settingsFile, true));
			out.write(key+"="+value);
			out.newLine();
			out.flush();			
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
