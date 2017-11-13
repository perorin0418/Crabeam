package org.net.perorin.crabeam;

import java.awt.EventQueue;

import org.net.perorin.crabeam.window.BootingWindow;
import org.net.perorin.crabeam.window.Window;

public class Main {
	public static void main(String[] args) {
		BootingWindow bw = new BootingWindow();
		bw.run();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.getFrame().setVisible(true);
					bw.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
