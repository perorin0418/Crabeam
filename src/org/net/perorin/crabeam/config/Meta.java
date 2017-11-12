package org.net.perorin.crabeam.config;

public class Meta {

	public static final String META_PATH = "./contents/metadata";

	private int frameHeight = 0;
	private int frameWidth = 0;
	private int frameX = 0;
	private int frameY = 0;
	private int noWidth = 0;
	private int ssWidth = 0;

	public int getFrameHeight() {
		return frameHeight;
	}

	public int getFrameWidth() {
		return frameWidth;
	}

	public int getFrameX() {
		return frameX;
	}

	public int getFrameY() {
		return frameY;
	}

	public int getNoWidth() {
		return noWidth;
	}

	public int getSsWidth() {
		return ssWidth;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public void setFrameX(int frameX) {
		this.frameX = frameX;
	}

	public void setFrameY(int frameY) {
		this.frameY = frameY;
	}

	public void setNoWidth(int noWidth) {
		this.noWidth = noWidth;
	}

	public void setSsWidth(int ssWidth) {
		this.ssWidth = ssWidth;
	}

}
