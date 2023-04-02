package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import helper.Message;
import network.ClientNetwork;
import network.ConnectionCode;
import network.ServerNetwork;

public class App extends JPanel implements Runnable{

	public static String clientIp;
	
	public static Thread thread;
	
	public static ClientNetwork clientNetwork;
	public static ServerNetwork serverNetwork;
	
	public static InputHandler inputHandler = new InputHandler();
	
	public static String username;
	public static String secondUsername = "";
	
	public static BufferedImage defaultIcon;
	public static BufferedImage submitButton;
	
	public static boolean startUpFinished = false;
	
	public static boolean connectedToServer = false;
	
	public static String currentTextBox = "";
	
	public static ArrayList<Message> messagesSent = new ArrayList<> ();
	
	public static String uniqueIdentifier;
	
	public static String justTyped = "";
	
	public static boolean characterLimitReached = false;
	
	public static int loginTick = 0;
	
	public App() {
		
		this.setPreferredSize(new Dimension(600, 600));
		this.setBackground(Color.white);
		this.addKeyListener(inputHandler);
		this.addMouseListener(inputHandler);
		this.setFocusable(true);
		
	}
	
	public void start() {
		
		uniqueIdentifier = randomUUID();
		
		System.out.println("App has started");

		try {
			DatagramSocket socket = new DatagramSocket();
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			clientIp = socket.getLocalAddress().getHostAddress();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(clientIp);
		
		clientNetwork = new ClientNetwork(clientIp);
		clientNetwork.start();
		
		if (JOptionPane.showConfirmDialog(this, "Do you want to run the server") == 0) {
			System.out.println("Server Started");
			serverNetwork = new ServerNetwork();
			serverNetwork.start();
        }
		
		username = JOptionPane.showInputDialog(this, "Please enter a username");
		
		while (username == null || username.trim().equals("")) {
			username = JOptionPane.showInputDialog(this, "Please enter a username");
		}
		
		System.out.println("username entered");
		
		loadImages();
		
		startUpFinished = true;
		
		thread = new Thread(this);
		thread.start();
		
	}
	
	private void loadImages() {

		try {
			defaultIcon = ImageIO.read(getClass().getResourceAsStream("/icons/defaulticon.png"));
			submitButton = ImageIO.read(getClass().getResourceAsStream("/icons/entericon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (startUpFinished) {
			
			if (loginTick == 1000) {
				loginTick = 0;
			}
			
			// DRAWING CLIENT USER
			g.drawImage(defaultIcon, 504, 504, 96, 96, null);
			
			if (username.trim().length() == 0) {
				
			}
			else if (username.length() >= 14) {
				username = username.substring(0, 12);
			}
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.PLAIN, 12));
	        g.drawString("Username: " + username, 510-(username.length()*8), 504);
	        
	        // DRAWING SECOND USER
			g.drawImage(defaultIcon, 0, 504, 96, 96, null);
			
			String temp = secondUsername;
	        
	        if (secondUsername.equals("")) {
	        	temp = "not connected";
	        }
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.PLAIN, 12));
	        g.drawString("Username: " + temp, 16, 504);
	        
	        temp = secondUsername;
	        
	        if (secondUsername.equals("")) {
	        	temp = "nobody";
	        }
	        
	        g.setColor(Color.GRAY);
			g.setFont(new Font("Verdana", Font.PLAIN, 18));
	        g.drawString("You are currently chatting with: " + temp, 100, 50);
	        
	        g.setColor(Color.GRAY);
			g.setFont(new Font("Verdana", Font.PLAIN, 18));
	        g.drawString("Connection Code: " + Long.toString(ConnectionCode.encodeIpAddress(clientIp)), 100, 30);
	        
	        // DRAWING TEXT BOX
	        
	        g.drawRect(96, 525, 344, 45);
	        
	        if (characterLimitReached) {
	        	g.setColor(Color.BLACK);
				g.setFont(new Font("Verdana", Font.PLAIN, 13));
	        	g.drawString("Character limit reached", 96, 590);
	        }
	        
	        if (!inputHandler.typing && currentTextBox.equals("")) {
	        	
	        	g.setColor(Color.GRAY);
		        Font font = new Font("Verdana", Font.PLAIN, 15);
		        
		        g.setFont(font);
		        g.drawString("Click here", 105, 550);
	        	
	        }
	        
	        // DRAWING SUBMIT BUTTON
	        
	        g.drawImage(submitButton, 450, 523, 48, 46, null);
	        
	        // ENTER KEY MESSAGE SUBMISSION
	        
	        if (inputHandler.enterPressed) {
	        
	        	characterLimitReached = false;
	        	
	        	if (currentTextBox.equals("")) {
	        		
	        		currentTextBox = "";
		        	inputHandler.enterPressed = false;
	        	}
	        	else {
	        		
	        		justTyped = currentTextBox;
	        		
	        		messagesSent.add(new Message(currentTextBox, true));
	        		
	        		clientNetwork.sendMessage(currentTextBox + "-" + uniqueIdentifier + "_" + username);
	        		
		        	currentTextBox = "";
		        	inputHandler.enterPressed = false;
	        	}
	        	
	        }
	        
	        // DRAWING TEXT WHILE TYPING
	        
	        g.setColor(Color.BLACK);
	        Font font = new Font("Verdana", Font.PLAIN, 15);
	        
	        int length = getStringLength(currentTextBox, font);
	        
			g.setFont(font);
	        g.drawString(getLast325Pixels(currentTextBox), 105, 550);
	        
	        // DRAWING MESSAGES TO SCREEN
	        
	        drawTextOnScreen(messagesSent, g);
		}
		
	}
	
	public void drawTextOnScreen(ArrayList<Message> messagesSent, Graphics g) {
	   
		Font font = new Font("Verdana", Font.PLAIN, 15);
		
		int posX = 250;
		int posY = 425;
		
		
		for (int i = messagesSent.size()-1; i >= 0; i--) {
			
			if (messagesSent.get(i).isClientUser()) {
				posX = 250;
				int adjustmentFactor = drawIndividualMessage(messagesSent.get(i).getMessage(), posX, posY, font, g);
				
				posY -= (adjustmentFactor-1)*20+30;
			}
			else {
				posX = 40;
				int adjustmentFactor = drawIndividualMessageOther(messagesSent.get(i).getMessage(), posX, posY, font, g);
				
				posY -= (adjustmentFactor-1)*20+30;
			}
			
		}
		
	}

	private int drawIndividualMessageOther(String message, int startX, int endY, Font font, Graphics g) {
		
		// Set the font and size of the text
	    g.setFont(font);

	    // Define the text to display
	    String text = message;

	    // Get the font metrics object to measure the width of the text
	    FontMetrics metrics = g.getFontMetrics();
	    
	    // Initialize the line width and height
	    int lineWidth = 0;
	    int lineHeight = metrics.getHeight();
	    
	    // Split the text into lines
	    List<String> lines = new ArrayList<>();
	    
	    StringBuilder currentLine = new StringBuilder();
	    for (String word : text.split("\\s+")) {
	        int wordWidth = metrics.stringWidth(word);
	        if (wordWidth > 300) {
	            // Split the word into smaller chunks
	            int startIndex = 0;
	            while (startIndex < word.length()) {
	                int endIndex = startIndex;
	                int chunkWidth = 0;
	                while (endIndex < word.length() && chunkWidth + metrics.charWidth(word.charAt(endIndex)) <= 300) {
	                    chunkWidth += metrics.charWidth(word.charAt(endIndex));
	                    endIndex++;
	                }
	                lines.add(word.substring(startIndex, endIndex));
	                startIndex = endIndex;
	            }
	        } else if (lineWidth + wordWidth > 300) {
	            lines.add(currentLine.toString());
	            currentLine = new StringBuilder(word + " ");
	            lineHeight += metrics.getHeight();
	            lineWidth = wordWidth + metrics.stringWidth(" ");
	        } else {
	            currentLine.append(word).append(" ");
	            lineWidth += wordWidth + metrics.stringWidth(" ");
	        }
	    }
	    if (currentLine.length() > 0) {
	        lines.add(currentLine.toString());
	    }

	    // Calculate the starting Y position
	    int startY = endY - (lines.size() - 1) * metrics.getHeight();

	    String type = "";
	    
	    if (lines.size() == 1) {
	    	type = "single";
	    }
	    else {
	    	type = "other";
	    }
	    
	    int maxPixelLength = 0;
	    
	    for (int i = 0; i < lines.size(); i++) {
	    	
	    	String line = lines.get(i);
	    	
	    	maxPixelLength = Math.max(maxPixelLength, getStringLength(lines.get(i).trim(), font));
	    	
	    }
	    
	    // Draw the lines
	    for (int i = 0; i < lines.size(); i++) {
	        String line = lines.get(i);
	        
	        int xPos = 0;
	        
	        if (i == 0 && (lines.size()-1 == i)) {
	        	xPos = startX;
	        }
	        else if (lines.size()-1 == i) {
	        	xPos = startX;
	        }
	        else {
	        	xPos = startX;
	        }
	        if (startY - 15 > 75) {
	        	g.drawString(line, xPos +10, startY + i * metrics.getHeight());
	        }
	    }
	    if (startY - 15 > 75) {
		    if (type.equals("single")) {
		    	g.drawRect(startX +5, startY-15, maxPixelLength+5+5, 20);
		    }
		    else if (type.equals("other")) {
		    	g.drawRect(startX +5, startY-15, maxPixelLength+5+5, lines.size() * metrics.getHeight());
		    }
	    }
	    
	    int numberOfLines = lines.size();
	    
	    return numberOfLines;
	}

	private int drawIndividualMessage(String message, int startX, int endY, Font font, Graphics g) {
	    // Set the font and size of the text
	    g.setFont(font);

	    // Define the text to display
	    String text = message;

	    // Get the font metrics object to measure the width of the text
	    FontMetrics metrics = g.getFontMetrics();
	    
	    // Initialize the line width and height
	    int lineWidth = 0;
	    int lineHeight = metrics.getHeight();
	    
	    // Split the text into lines
	    List<String> lines = new ArrayList<>();
	    
	    StringBuilder currentLine = new StringBuilder();
	    for (String word : text.split("\\s+")) {
	        int wordWidth = metrics.stringWidth(word);
	        if (wordWidth > 300) {
	            // Split the word into smaller chunks
	            int startIndex = 0;
	            while (startIndex < word.length()) {
	                int endIndex = startIndex;
	                int chunkWidth = 0;
	                while (endIndex < word.length() && chunkWidth + metrics.charWidth(word.charAt(endIndex)) <= 300) {
	                    chunkWidth += metrics.charWidth(word.charAt(endIndex));
	                    endIndex++;
	                }
	                lines.add(word.substring(startIndex, endIndex));
	                startIndex = endIndex;
	            }
	        } else if (lineWidth + wordWidth > 300) {
	            lines.add(currentLine.toString());
	            currentLine = new StringBuilder(word + " ");
	            lineHeight += metrics.getHeight();
	            lineWidth = wordWidth + metrics.stringWidth(" ");
	        } else {
	            currentLine.append(word).append(" ");
	            lineWidth += wordWidth + metrics.stringWidth(" ");
	        }
	    }
	    if (currentLine.length() > 0) {
	        lines.add(currentLine.toString());
	    }

	    // Calculate the starting Y position
	    int startY = endY - (lines.size() - 1) * metrics.getHeight();

	    String type = "";
	    
	    if (lines.size() == 1) {
	    	type = "single";
	    }
	    else {
	    	type = "other";
	    }
	    
	    int maxPixelLength = 0;
	    
	    for (int i = 0; i < lines.size(); i++) {
	    	
	    	String line = lines.get(i);
	    	
	    	maxPixelLength = Math.max(maxPixelLength, getStringLength(lines.get(i).trim(), font));
	    	
	    }
	    
	    // Draw the lines
	    for (int i = 0; i < lines.size(); i++) {
	        String line = lines.get(i);
	        
	        int xPos = 0;
	        
	        if (i == 0 && (lines.size()-1 == i)) {
	        	xPos = startX + 300 - maxPixelLength;
	        	
	        }
	        else if (lines.size()-1 == i) {
	        	xPos = startX + 300 - maxPixelLength;
	        	
	        }
	        else {
	        	xPos = startX + 300 - maxPixelLength;
	        }
	        
	        if (startY - 15 > 75) {
	        	g.drawString(line, xPos, startY + i * metrics.getHeight());
	        }
	    }
	    
	    if (startY - 15 > 75) {
		    if (type.equals("single")) {
		    	g.drawRect(startX + 300 - maxPixelLength-5, startY-15, maxPixelLength+5+5, 20);
		    }
		    else if (type.equals("other")) {
		    	g.drawRect(startX + 300 - maxPixelLength-5, startY-15, maxPixelLength+5+5, lines.size() * metrics.getHeight());
		    }
	    }
	    int numberOfLines = lines.size();
	    
	    return numberOfLines;
	}
	
	public static int getStringLength(String str, Font font) {
        Graphics graphics = null;
        try {
            // Create a temporary graphics object to get the font metrics
            graphics = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB).createGraphics();
            graphics.setFont(font);
            
            // Get the font metrics for the given string and font
            FontMetrics metrics = graphics.getFontMetrics();
            
            // Return the width of the string in pixels
            return metrics.stringWidth(str);
        } finally {
            if (graphics != null) {
                graphics.dispose();
            }
        }
    }
	
	public static String getLast325Pixels(String str) {
	    Objects.requireNonNull(str, "Input string must not be null");
	    Font font = new Font("Verdana", Font.PLAIN, 15);
	    Graphics graphics = null;
	    try {
	        // Create a temporary graphics object to get the font metrics
	        graphics = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB).createGraphics();
	        graphics.setFont(font);

	        // Get the font metrics for the given string and font
	        FontMetrics metrics = graphics.getFontMetrics();

	        // Determine the maximum number of characters that can be displayed in 325 pixels
	        int maxLength = 0;
	        int pixelWidth = 0;
	        for (int i = str.length() - 1; i >= 0; i--) {
	            pixelWidth += metrics.charWidth(str.charAt(i));
	            if (pixelWidth > 325) {
	                break;
	            }
	            maxLength++;
	        }

	        // Return the last 325 pixels of the string
	        return str.substring(Math.max(0, str.length() - maxLength));
	    } finally {
	        if (graphics != null) {
	            graphics.dispose();
	        }
	    }
	}

	public static String randomUUID() {

	    final int MAX = 65535; //FFFF
	    final int MIN = 0;   //00
		
        Long currentTime = System.currentTimeMillis();

        Random r = new Random();
        int randomNum = r.nextInt((MAX - MIN) + 1) + MIN;

        StringBuilder sb = new StringBuilder();
        sb.append(Long.toHexString(currentTime));
        sb.append(Integer.toHexString(randomNum));

        return sb.toString();
    }
	
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
	    final double ns = 1000000000.0 / 45.0;
	    double delta = 0;
	    while(true){
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;
	        while(delta >= 1){
	        	
	        	tick();
	    		repaint();
	        	
	            delta--;
	        }
	   } 
	}

	private void tick() {
		
		if (loginTick % 10 == 0) {
			if (!connectedToServer) {
				
				clientNetwork.sendMessage("829473583763" + "-" + uniqueIdentifier + "_" + username);
			}
		}
		
		loginTick++;
		
	}
	
}
