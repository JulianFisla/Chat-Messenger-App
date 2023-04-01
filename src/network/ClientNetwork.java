package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import helper.Message;
import screen.App;

public class ClientNetwork extends Thread {

	private InetAddress clientIp;
	private DatagramSocket socket;
	
	public ClientNetwork(String ip) {
		
		try {
			this.socket = new DatagramSocket();
			this.clientIp = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		if (!message.trim().substring(0, message.trim().indexOf("-")).equals("829473583763")) {
			
			System.out.println("MESSAGE ADDED: " + message.trim().substring(0, message.trim().indexOf("-")));
			App.messagesSent.add(new Message(message.trim().substring(0, message.trim().indexOf("-")), false));
			
		}
		
	}

	public void sendMessage(String currentTextBox) {
		
		byte[] data = currentTextBox.getBytes();
		
		DatagramPacket packet = new DatagramPacket(data, data.length, clientIp, 1331);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	
	
}
