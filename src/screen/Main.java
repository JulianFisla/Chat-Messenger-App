package screen;

import javax.swing.JFrame;

public class Main {

	public static App app = null;
	public static JFrame frame = null;
	
	public static void main(String[] args) {
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Chat-Messenger");

		app = new App();
		
		frame.add(app);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		app.start();
	}

}
