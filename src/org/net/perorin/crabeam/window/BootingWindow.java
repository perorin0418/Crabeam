package org.net.perorin.crabeam.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.internal.win32.TCHAR;
import org.net.perorin.crabeam.cv.CV;
import org.net.perorin.crabeam.cv.CVImage;

public class BootingWindow {

	Window window;

	public static void main(String[] args) {
		BootingWindow window = new BootingWindow();
		window.run();
		while (true) {
			long hwnd = OS.FindWindow(null, new TCHAR(OS.CP_INSTALLED, "蟹光線", true));
			if (hwnd != 0) {
				window.stop();
				break;
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public BootingWindow() {
	}

	public void run() {
		RECT rect = new RECT();
		OS.GetWindowRect(OS.GetDesktopWindow(), rect);
		window = new Window(new Frame());
		CVImage cvImg = new CVImage(ImageRandomizer.getBootingImagePath());
		window.setBounds(rect.right / 2 - cvImg.getWidth() / 2, rect.bottom / 2 - cvImg.getHeight() / 2, cvImg.getWidth(), cvImg.getHeight());
		String strs[] = { "起動中です。", "しばらくお待ちください。" };
		CV.writeString(cvImg.getImageBuffer().createGraphics(), strs, 10, 10, new Font("メイリオ", Font.BOLD, 20), new Color(0, 0, 0));
		ImageIcon img = new ImageIcon(cvImg.getImageBuffer());
		JLabel lbl = new JLabel(img);
		window.add(lbl);

		window.setVisible(true);
	}

	public void stop() {
		window.dispose();
	}
}
