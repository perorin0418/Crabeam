package org.net.perorin.crabeam.redFrame;

import java.awt.Color;

public class RedFrame {

	public int x;
	public int y;
	public int width;
	public int height;
	public int lineWidth;
	public Color color;

	public RedFrame(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.lineWidth = 1;
		this.color = new Color(255, 0, 0);
	}
}
