package org.net.perorin.crabeam;

import java.awt.EventQueue;

import org.net.perorin.crabeam.window.CrabLaserWindow;

public class Main {
	public static void main(String[] args) {
//		if (args.length > 0) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						CrabLaserWindow window = new CrabLaserWindow("");
						window.getFrame().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
//		} else {
//			EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						CrabeamWindow window = new CrabeamWindow();
//						window.getFrame().setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
	}}
