package yılan;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		int en=600, boy=600;
		
		JFrame frame=new JFrame("Yılan Oyunu");
		frame.setSize(en, boy);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Screen screen=new Screen(en,boy);
		frame.add(screen);
		frame.pack();
		screen.requestFocus();
		frame.setVisible(true);
	}

}
