package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class App extends JPanel implements Runnable{

	public static Thread thread;
	
	public static InputHandler inputHandler = new InputHandler();
	
	public static String username;
	
	public static BufferedImage defaultIcon;
	
	public static boolean startUpFinished = false;
	
	public static String currentTextBox = "";
	
	public App() {
		
		this.setPreferredSize(new Dimension(600, 600));
		this.setBackground(Color.white);
		this.addKeyListener(inputHandler);
		this.setFocusable(true);
		
	}
	
	public void start() {
		
		System.out.println("App has started");
		
		username = JOptionPane.showInputDialog(this, "Please enter a username");
		
		System.out.println("username entered");
		
		loadImages();
		
		startUpFinished = true;
		
		thread = new Thread(this);
		thread.start();
		
	}
	
	private void loadImages() {

		try {
			defaultIcon = ImageIO.read(getClass().getResourceAsStream("/icons/defaulticon.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (startUpFinished) {
			
			// DRAWING CURRENT USER
			g.drawImage(defaultIcon, 504, 504, 96, 96, null);

			if (username.trim().length() == 0) {
				
				username = randomUUID();
				
			}
			else if (username.length() >= 14) {
				username = username.substring(0, 12);
			}
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.PLAIN, 12));
	        g.drawString("Username: " + username, 510-(username.length()*8), 504);
	        
	        // DRAWING SECOND USER
			g.drawImage(defaultIcon, 0, 504, 96, 96, null);
			
			g.setColor(Color.BLACK);
			g.setFont(new Font("Verdana", Font.PLAIN, 12));
	        g.drawString("Username: not connected", 16, 504);
	        
	        g.setColor(Color.GRAY);
			g.setFont(new Font("Verdana", Font.PLAIN, 18));
	        g.drawString("You are currently chatting with: not connected",50, 50);
	        
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
	    final double ns = 1000000000.0 / 60.0;
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

		
		
	}
	
}
