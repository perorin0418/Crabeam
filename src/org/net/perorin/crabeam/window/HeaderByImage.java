package org.net.perorin.crabeam.window;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.cv.CV;
import org.net.perorin.crabeam.cv.CVImage;
import org.net.perorin.crabeam.logic.CacheManeger;

public class HeaderByImage extends JPanel {

	CVImage img;
	JLabel imgLbl;
	JLabel txtLbl;

	public HeaderByImage() {
		this.img = new CVImage(Constant.TESTSUITE_HEADER_PATH);
		this.setLayout(null);
		this.setOpaque(false);
		imgLbl = new JLabel();
		txtLbl = new JLabel();
		txtLbl.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(Constant.TANUKI_FONT_PATH));
			txtLbl.setFont(font.deriveFont(32f));
		} catch (FontFormatException | IOException e) {
			txtLbl.setFont(new Font("MS UI Gothic", Font.PLAIN, 16));
		}
		this.add(txtLbl);
		this.add(imgLbl);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		imgLbl.setBounds(0, 0, width, height);
		CVImage cache = CacheManeger.uncacheImage("header" + width + height);
		if (cache != null) {
			img = cache;
		} else {
			img = CV.resize(img, width, height);
			CacheManeger.cacheImage("header" + width + height, img);
		}
		imgLbl.setIcon(new ImageIcon(img.getImageBuffer()));
		txtLbl.setBounds(10, 10, width - img.getWidth() / 5, height - 20);
	}

	public void setText(String text) {
		txtLbl.setText(text);
	}

	public String getText(){
		return txtLbl.getText();
	}

}
