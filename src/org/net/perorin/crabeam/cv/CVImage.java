package org.net.perorin.crabeam.cv;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CVImage {

	private BufferedImage img = null;
	private WritableRaster wr = null;

	private int width;
	private int height;
	private int type;

	public CVImage() {
		super();
	}

	public CVImage(int width, int height, int type) {
		this(new BufferedImage(width, height, type));
	}

	public CVImage(BufferedImage img) {
		this.img = img;
		this.wr = this.img.getRaster();
		this.width = this.img.getWidth();
		this.height = this.img.getHeight();
		this.type = this.img.getType();
	}

	public CVImage(String filename) {
		try {
			this.img = ImageIO.read(new File(filename));
			this.wr = this.img.getRaster();
			this.width = this.img.getWidth();
			this.height = this.img.getHeight();
			this.type = this.img.getType();
		} catch (IOException e) {
			System.out.println(filename + "の入力に失敗しました");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CVImage(File file) {
		this(file.getPath());
	}

	public boolean save(String filename) {
		DataOutputStream dos;

		try {
			dos = new DataOutputStream(new FileOutputStream(filename));
			ImageIO.write(this.img, "png", dos);
			dos.close();
			dos = null;
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean setPixel(Point p, Pixel pxl) {
		int w = 0;
		int h = 0;
		int buff[] = new int[4];

		buff[0] = pxl.getR();
		buff[1] = pxl.getG();
		buff[2] = pxl.getB();
		buff[3] = pxl.getA();

		w = this.width;
		h = this.height;

		if (p.x < w && p.y < h) {
			wr = img.getRaster();
			wr.setPixel(p.x, p.y, buff);
			img.setData(wr);
			return true;
		}
		return false;
	}

	public boolean setPixel(int x, int y, Pixel pxl) {
		return setPixel(new Point(x, y), pxl);
	}

	public void setData(WritableRaster wr) {
		this.img.setData(wr);
	}

	public void setData(BufferedImage img) {
		this.img = img;
	}

	public Pixel getPixel(Point p) {
		Pixel pxl = new Pixel();
		int buf[] = new int[4];

		if (wr == null)
			wr = img.getRaster();

		wr.getPixel(p.x, p.y, buf);

		pxl.setRGBA(buf[0], buf[1], buf[2], buf[3]);

		return pxl;
	}

	public Pixel getPixel(int x, int y) {
		return getPixel(new Point(x, y));
	}

	public BufferedImage getImageBuffer() {
		return this.img;
	}

	public WritableRaster getImageRaster() {
		return this.img.getRaster();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getType() {
		return type;
	}
}
