package helper;

public class Message {
	
	private String message;
	private boolean clientUser;
	
	public Message(String message, boolean clientUser) {
		
		this.setMessage(message);
		this.setClientUser(clientUser);
		
	}

	// GETTERS AND SETTERS
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isClientUser() {
		return clientUser;
	}

	public void setClientUser(boolean clientUser) {
		this.clientUser = clientUser;
	}
	
}
