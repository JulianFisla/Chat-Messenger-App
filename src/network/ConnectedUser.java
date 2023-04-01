package network;

import java.net.InetAddress;

public class ConnectedUser {

	private InetAddress ip;
	private int port;
	private String uniqueIdentifier;
	
	public ConnectedUser(InetAddress ip, int port, String uniqueIdentifier) {
		
		this.setIp(ip);
		this.setPort(port);
		this.setUniqueIdentifier(uniqueIdentifier);
		
	}

	// GETTERS AND SETTERS
	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}
	
}
