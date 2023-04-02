package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ServerNetwork extends Thread{

	private DatagramSocket socket;
	private List<ConnectedUser> users = new ArrayList<ConnectedUser>();
	
	private ArrayList<String> loginPackets = new ArrayList<String> ();
	
	private boolean alreadyLocalServer = false;
	
	public ServerNetwork() {
		
		try {
			socket = new DatagramSocket(1331);
		} catch (SocketException e) {

			System.out.println("A server is already running locally!");
			//alreadyLocalServer = true;
			
		}
		
	}
	
	public void run() {
        while (!alreadyLocalServer) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

	private void parsePacket(byte[] data, InetAddress address, int port) {
		
		String message = new String(data);
		
		String uniqueIdentifier = message.trim().substring(message.trim().indexOf("-")+1, message.trim().indexOf("_"));
		
		boolean alreadyUser = false;
		
		if (users.size() == 0) {
			
			users.add(new ConnectedUser(address, port, uniqueIdentifier));
			loginPackets.add(new String(data));
			
		}
		else {
			
			for (int i = 0; i < users.size(); i++) {
				
				if (uniqueIdentifier.equals(users.get(i).getUniqueIdentifier())){
					
					alreadyUser = true;
					
				}
				
			}
			
			if (!alreadyUser) {
				
				users.add(new ConnectedUser(address, port, uniqueIdentifier));
				sendDataToClient(loginPackets.get(0).getBytes(), users.get(0).getIp(), users.get(0).getPort());
				
			}
			
		}
		
		sendDataToClient(data, address, port);
		
	}
	
	public void sendDataToClient(byte[] data, InetAddress ip, int port) {
		
		String message = new String(data);
		
		String uniqueIdentifier = message.trim().substring(message.trim().indexOf("-")+1, message.trim().indexOf("_"));
		
        for (int i = 0; i < users.size(); i++) {
        	
        	if (!(users.get(i).getUniqueIdentifier().equals(uniqueIdentifier))) {
        		sendData(data, users.get(i).getIp(), users.get(i).getPort());
        	}
        	else {
        		
        	}
            
        }
        
    }

	private void sendData(byte[] data, InetAddress ip, int port) {

		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            this.socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}
	
}
