package org.net.perorin.crabeam.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.bind.JAXB;

import org.net.perorin.crabeam.config.Config;
import org.net.perorin.crabeam.config.ConfigCrabLaser;
import org.net.perorin.crabeam.cv.CV;
import org.net.perorin.crabeam.cv.CVImage;
import org.net.perorin.crabeam.logic.CacheManeger;

public class ScreenShotThumb extends JPanel {

	private JLabel lbl;
	private CVImage img;
	private boolean isSelect;

	public ScreenShotThumb(String imgFile) {
		super();
		this.setBackground(SystemColor.inactiveCaptionBorder);

		img = new CVImage(imgFile);

		ConfigCrabLaser config = JAXB.unmarshal(new File(Config.CONFIG_PATH), Config.class).getCrablaser();

		int height_limit = config.getSplit_location() - 145;
		int width_limit = (int) (height_limit * 2);

		double mag_pic = (double) height_limit / (double) img.getHeight();

		if (width_limit < img.getWidth() * mag_pic) {
			mag_pic = (double) width_limit / (double) img.getWidth();
		}

		CVImage cache_thumb = CacheManeger.uncacheImage("thumb" + imgFile + mag_pic);
		if (cache_thumb != null) {
			img = cache_thumb;
		} else {
			img = CV.resize(img, mag_pic);
			CacheManeger.cacheImage("thumb" + imgFile + mag_pic, img);
		}

		lbl = new JLabel(new ImageIcon(img.getImageBuffer()));
		this.add(lbl, BorderLayout.CENTER);
	}

	public void select() {
		isSelect = true;
		this.setBackground(Color.RED);
	}

	public void diselect() {
		isSelect = false;
		this.setBackground(SystemColor.inactiveCaptionBorder);
	}

}
