package main.networking;

public class ServerTest {
	public static void main(String[] args) {
		Server server = new Server(80);
		server.startServer();
	}
}
