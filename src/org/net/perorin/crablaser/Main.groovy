package org.net.perorin.crablaser

import java.awt.EventQueue

import javax.swing.JOptionPane

import org.net.perorin.crablaser.window.FormatSelector

public class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater({
			try {
				String xlsx = args.size() > 0 ? args[0] : ""
				FormatSelector fs = new FormatSelector(xlsx)
				fs.frame.setVisible(true)
			} catch (Exception e) {
				e.printStackTrace()
				JOptionPane.showMessageDialog(
						null,
						"ログを参照してください。\n『./contents/log/crabeam.log』",
						"例外発生",
						JOptionPane.WARNING_MESSAGE)
			}
		})
	}
}
