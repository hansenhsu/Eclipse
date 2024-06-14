package basicThreeDimensions;

import java.awt.Color;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		JFrame window = new JFrame("3D renderer");
		window.setVisible(true);
		window.setSize(600, 600);
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.black);
		
		Body body = new Body();
		window.add(body);
		window.pack();
		body.requestFocus();
	}

}



