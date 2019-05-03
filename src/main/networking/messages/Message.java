package main.networking.messages;

public class Message {
	
	public String _sender;
	public String _receiver;
	public String _message;
	
	public Message(String sender, String receiver, String message) {
		_sender = sender;
		_receiver = receiver;
		_message = message;
	}
}
