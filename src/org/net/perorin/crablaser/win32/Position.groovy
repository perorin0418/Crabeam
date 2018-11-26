package org.net.perorin.crablaser.win32

import java.awt.Point

public class Position {

	private int x
	private int y

	public Position() {
	}

	public Position(int x, int y) {
		this.x = x
		this.y = y
	}

	public int getX() {
		return x
	}

	public void setX(int x) {
		this.x = x
	}

	public int getY() {
		return y
	}

	public void setY(int y) {
		this.y = y
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]"
	}

	public Point getPoint() {
		Point p = new Point(x, y)
		return p
	}

}
