package main.networking.messages;

import java.io.Serializable;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 9056356710077035072L;
	
	public String _sender;
	public String _receiver;
	public String _message;
	
	public Message(String sender, String receiver, String message) {
		_sender = sender;
		_receiver = receiver;
		_message = message;
	}
}
