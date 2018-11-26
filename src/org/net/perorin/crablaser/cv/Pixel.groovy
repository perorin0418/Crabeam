package org.net.perorin.crablaser.cv

import java.awt.Color

public class Pixel {

	public int r
	public int g
	public int b
	public int a

	public Pixel() {
	}

	public Pixel(int r, int g, int b, int a) {
		this.r = r
		this.g = g
		this.b = b
		this.a = a
	}

	public void setRGBA(int r, int g, int b, int a) {
		this.r = r
		this.g = g
		this.b = b
		this.a = a
	}

	public int[] getRGBA() {
		int[] rgba = new int[4]

		rgba[0] = r
		rgba[1] = g
		rgba[2] = b
		rgba[3] = a

		return rgba
	}

	public int getR() {
		return this.r
	}

	public int getG() {
		return this.g
	}

	public int getB() {
		return this.b
	}

	public int getA() {
		return this.a
	}

	public void setR(int r) {
		this.r = r
	}

	public void setG(int g) {
		this.g = g
	}

	public void setB(int b) {
		this.b = b
	}

	public void setA(int a) {
		this.a = a
	}

	public int getPixel() {
		int r = this.r * 256 * 256
		int g = this.g * 256
		int b = this.b
		return r + g + b
	}

	@Override
	public String toString() {
		return "Pixel [r=" + r + ", g=" + g + ", b=" + b + ", a=" + a + "]"
	}

	public static Color getAlphaBlendedColor(Color background, Color foreground, double alpha) {
		int br = background.getRed()
		int bg = background.getGreen()
		int bb = background.getBlue()
		int fr = foreground.getRed()
		int fg = foreground.getGreen()
		int fb = foreground.getBlue()
		// Value = Value0(1.0 - Alpha) + Value1(Alpha)
		int newr = (int) ((br * (1.0 - alpha)) + (fr * alpha))
		int newg = (int) ((bg * (1.0 - alpha)) + (fg * alpha))
		int newb = (int) ((bb * (1.0 - alpha)) + (fb * alpha))
		return new Color(newr, newg, newb)
	}

}
