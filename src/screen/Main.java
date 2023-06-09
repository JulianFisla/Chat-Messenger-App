package screen;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {

	public static App app = null;
	public static JFrame frame = null;
	
	public static void main(String[] args) {
		
		// INITIALIZE FRAME
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Chat-Messenger");
		
		// INITIALIZE APP
		
		app = new App();
		
		frame.add(app);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// START PROGRAM
		
		app.start();
	}

}
