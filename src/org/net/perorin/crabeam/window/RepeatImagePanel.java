package org.net.perorin.crabeam.window;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLayeredPane;

public class RepeatImagePanel extends JLayeredPane {

	private BufferedImage backgroundImage;

	public RepeatImagePanel(String image) {
		super();
		try {
			backgroundImage = ImageIO.read(new File(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		int tileWidth = backgroundImage.getWidth();
		int tileHeight = backgroundImage.getHeight();
		for (int y = 0; y < getHeight(); y += tileHeight) {
			for (int x = 0; x < getWidth(); x += tileWidth) {
				g2d.drawImage(backgroundImage, x, y, this);
			}
		}
		g2d.dispose();
	}

}
