package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class App extends JPanel implements Runnable{

	public static Thread thread;
	
	public static InputHandler inputHandler = new InputHandler();
	
	public App() {
		
		this.setPreferredSize(new Dimension(600, 600));
		this.setBackground(Color.white);
		this.addKeyListener(inputHandler);
		this.setFocusable(true);
		
	}
	
	public void start() {
		
		System.out.println("App has started");
		
		thread = new Thread(this);
		thread.start();
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
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
