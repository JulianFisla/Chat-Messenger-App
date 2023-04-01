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
	
	
	public ServerNetwork() {
		
		try {
			socket = new DatagramSocket(1331);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() {
        while (true) {
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
		
		String uniqueIdentifier = message.trim().substring(message.trim().indexOf("-")+1);
		
		boolean alreadyUser = false;
		
		if (users.size() == 0) {
			
			users.add(new ConnectedUser(address, port, uniqueIdentifier));
			
		}
		else {
			
			for (int i = 0; i < users.size(); i++) {
				
				if (uniqueIdentifier.equals(users.get(i).getUniqueIdentifier())){
					
					alreadyUser = true;
					
				}
				
			}
			
			if (!alreadyUser) {
				
				users.add(new ConnectedUser(address, port, uniqueIdentifier));
				
			}
			
		}
		
		sendDataToClient(data, address, port);
		
	}
	
	public void sendDataToClient(byte[] data, InetAddress ip, int port) {
		
		String message = new String(data);
		
		String uniqueIdentifier = message.trim().substring(message.trim().indexOf("-")+1);
		
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
