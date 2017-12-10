package org.net.perorin.crabeam.window;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import org.net.perorin.crabeam.config.Constant;
import org.net.perorin.crabeam.cv.CV;
import org.net.perorin.crabeam.cv.CVImage;
import org.net.perorin.crabeam.logic.CacheManeger;

public class PictureCanvas extends JLayeredPane {

	CVImage img;
	CVImage picture;
	String imgPath;
	JLabel imgLbl;
	JLabel pictureLbl;

	public PictureCanvas() {
		this.img = new CVImage(Constant.PICTURE_FRAME_PATH);
		this.setLayout(null);
		this.setOpaque(false);

		imgLbl = new JLabel();
		pictureLbl = new JLabel();

		this.add(imgLbl);
		this.setLayer(imgLbl, 0);
		this.add(pictureLbl);
		this.setLayer(pictureLbl, 1);

		imgLbl.setBackground(Color.BLUE);
	}

	/**
	 * バウンズ設定
	 * <p>
	 * 高さは自動設定
	 *
	 * @param x
	 * @param y
	 * @param width
	 */
	public void setBounds(int x, int y, int width) {
		double mag = (double) width / (double) img.getWidth();
		img = CV.resize(img, width, (int) (img.getHeight() * mag));
		imgLbl.setIcon(new ImageIcon(img.getImageBuffer()));
		imgLbl.setBounds(0, 0, width, img.getHeight());
		super.setBounds(x, y, width, img.getHeight());

		if (picture != null) {
			int width_limit = (int) (img.getWidth() * 0.8);
			int height_limit = (int) (img.getHeight() * 0.7);

			double mag_pic = (double) height_limit / (double) picture.getHeight();

			if (width_limit < picture.getWidth() * mag_pic) {
				mag_pic = (double) width_limit / (double) picture.getWidth();
			}

			CVImage cache_picture = CacheManeger.uncacheImage("picture" + imgPath + mag_pic);
			if (cache_picture != null) {
				picture = cache_picture;
			} else {
				picture = CV.resize(picture, mag_pic);
				CacheManeger.cacheImage("picture" + imgPath + mag_pic, picture);
			}

			pictureLbl.setIcon(new ImageIcon(picture.getImageBuffer()));
			pictureLbl.setBounds(
					img.getWidth() / 2 - picture.getWidth() / 2 - img.getWidth() / 150,
					img.getHeight() / 2 - picture.getHeight() / 2 - img.getHeight() / 150,
					picture.getWidth(),
					picture.getHeight());
		}
	}

	public void setPicture(String imgPath) {
		this.imgPath = imgPath;
		this.picture = new CVImage(imgPath);
	}

	public void refresh() {
		setBounds(this.getBounds().x, this.getBounds().y, this.getBounds().width);
	}

	public int getPreferredHeight() {
		return img.getHeight();
	}
}
