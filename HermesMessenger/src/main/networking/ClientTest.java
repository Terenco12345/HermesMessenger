package main.networking;

import java.io.IOException;
import java.util.Scanner;

import main.networking.messages.Message;

public class ClientTest {
	public static void main(String[] args) throws IOException {
		Client client = new Client("localhost",80);
		client.startConnection();
		
		Scanner s = new Scanner(System.in);
		String line = null;
		line = s.nextLine();
		while(line != "stop") {
			client.sendToServer(new Message("John", "Greg", line));
			line = s.nextLine();
		}


		System.out.println("Connection established");
	}
}
