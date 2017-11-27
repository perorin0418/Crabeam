package org.net.perorin.crabeam.window;

import javax.swing.JFrame;

public class CrabLaserWindow {

	private JFrame frame;

	public CrabLaserWindow(String excelFile ) {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public JFrame getFrame() {
		return frame;
	}

}
