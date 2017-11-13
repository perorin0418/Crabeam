package org.net.perorin.crabeam.cv;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.net.perorin.crabeam.exception.CVException;

public class CV {

	public static final double POINT_SIZE = 4 / 3;
	public static final int MODE_SIMILAR_SIZE = 1;
	public static final int MODE_CENTER = 2;

	/***
	 * list内のCVImageをすべて統合して一つのCVImageにする<br>
	 * そして、出来上がったCVImageを返却する<br>
	 * list内のCVImageはすべて同じ大きさでなければならない
	 *
	 * @param list
	 * @return
	 * @throws CVException
	 */
	public static CVImage merge(ArrayList<CVImage> list, int mode) throws CVException {
		CVImage img = list.get(0);

		for (CVImage buf : list)
			merge(img, buf, mode);

		return img;
	}

	/***
	 * img1とimg2を統合して返却する<br>
	 * img1とimg2は同じ大きさでなければならない<br>
	 *
	 * @param img1
	 * @param img2
	 * @return
	 * @throws CVException
	 */
	public static CVImage merge(CVImage img1, CVImage img2, int mode) throws CVException {

		//画像が同じサイズの時
		if (mode == MODE_SIMILAR_SIZE) {
			if (img1.getWidth() != img2.getWidth())
				throw new CVException("入力された二つの画像サイズが違います");
			if (img1.getHeight() != img2.getHeight())
				throw new CVException("入力された二つの画像サイズが違います");

			Graphics2D g2d = img1.getImageBuffer().createGraphics();
			g2d.drawImage(img2.getImageBuffer(), 0, 0, img2.getWidth(), img2.getHeight(), null);

		}
		//画像が違うサイズの時、中央揃えにする
		else if (mode == MODE_CENTER) {
			if (img1.getWidth() < img2.getWidth())
				throw new CVException("img2の幅はimg1より小さくなければなりません");
			if (img1.getHeight() < img2.getHeight())
				throw new CVException("img2の高さはimg1より小さくなければなりません");

			int widthDif = (img1.getWidth() - img2.getWidth()) / 2;
			int heightDif = (img1.getHeight() - img2.getHeight()) / 2;

			Graphics2D g2d = img1.getImageBuffer().createGraphics();
			g2d.drawImage(img2.getImageBuffer(), widthDif, heightDif, img2.getWidth(), img2.getHeight(), null);

		}
		return img1;
	}

	/***
	 * 文字列から画像を作成する
	 * @param str
	 * @param font
	 * @param fontColor
	 * @param drawEdge
	 * @param edgeSize
	 * @param edgeColor
	 * @return
	 */
	public static CVImage stringImage(String str, Font font, Color fontColor, int edgeSize, Color edgeColor) {

		//大きい画像を作って、小さくリサイズすることで滑らかな文字ができる
		int magnification = 2;
		font = new Font(font.getName(), font.getStyle(), font.getSize() * magnification);

		//文字列を改行で分ける
		String[] strs = str.split("\\r\\n|\\r|\\n");

		//横幅が一番長くなる行を検索
		int maxWidth = 0;
		for (int i = 0; i < strs.length; i++) {
			if (maxWidth < strs[i].length()) {
				maxWidth = strs[i].length();
			}
		}

		//縦幅が一番長くなるところ
		int maxHeight = strs.length;

		int width = (int) (font.getSize() * POINT_SIZE * maxWidth + font.getSize() * 1.5) * magnification;
		int height = (int) (font.getSize() * POINT_SIZE * maxHeight * 1.5 + 1) * magnification;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setBackground(new Color(0, 0, 0, 0));
		g2d.clearRect(0, 0, img.getWidth(), img.getHeight());

		//縁の描画
		if (edgeSize != 0 && edgeColor != null) {
			for (int x = -edgeSize; x <= edgeSize; x++) {
				for (int y = -edgeSize; y <= edgeSize; y++) {
					writeString(g2d, strs, x, y, font, edgeColor);
				}
			}
		}

		//文字の描画
		writeString(g2d, strs, 0, 0, font, fontColor);

		return imageNormalize(resize(new CVImage(img), 1.0 / magnification));
	}

	/***
	 * 文字列から画像を作成する
	 * @param str
	 * @param font
	 * @param fontColor
	 * @return
	 */
	public static CVImage stringImage(String str, Font font, Color fontColor) {
		return stringImage(str, font, fontColor, 0, null);
	}

	/**
	 * 画像に文字列を描画する
	 * @param img
	 * @param strs
	 * @param x 開始位置
	 * @param y 開始位置
	 * @param font
	 * @param fontColor
	 */
	public static void writeString(Graphics2D g2d, String[] strs, int x, int y, Font font, Color fontColor) {
		g2d.setFont(font);
		g2d.setColor(fontColor);
		for (int i = 0; i < strs.length; i++) {
			g2d.drawString(strs[i], x + 5, y + font.getSize() * (i + 1));
		}
	}

	/***
	 * 画像の余分な余白(透明部分)を削除する
	 * @param img
	 * @return
	 */
	public static CVImage imageNormalize(CVImage img) {
		WritableRaster wr = img.getImageRaster();
		int buf[] = new int[4];
		List<Integer> xList = new ArrayList<Integer>();
		List<Integer> yList = new ArrayList<Integer>();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				wr.getPixel(x, y, buf);
				if (buf[3] > 250) {
					xList.add(x);
					yList.add(y);
				}
			}
		}
		int xMin = 0;
		int yMin = 0;
		int xMax = 0;
		int yMax = 0;
		if (xList.size() > 0 && yList.size() > 0) {
			xMin = Collections.min(xList) - 1;
			yMin = Collections.min(yList) - 1;
			xMax = Collections.max(xList) + 2;
			yMax = Collections.max(yList) + 2;
		} else {
			return img;
		}
		CVImage dst = new CVImage(xMax - xMin, yMax - yMin, img.getType());
		WritableRaster dst_wr = dst.getImageRaster();
		for (int y = yMin; y < yMax; y++) {
			for (int x = xMin; x < xMax; x++) {
				wr.getPixel(x, y, buf);
				dst_wr.setPixel(x - xMin, y - yMin, buf);
			}
		}
		dst.setData(dst_wr);

		return dst;
	}

	/***
	 * リサイズ
	 * @param img
	 * @param width
	 * @param height
	 * @return
	 */
	public static CVImage resize(CVImage img, int width, int height) {
		CVImage thumb = new CVImage(width, height, img.getType());
		thumb.getImageBuffer().getGraphics().drawImage(img.getImageBuffer().getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
		return thumb;
	}

	/***
	 * ぼかし
	 * @param img
	 * @param magnification
	 * @return
	 */
	public static CVImage resize(CVImage img, double magnification) {
		return resize(img, (int) (img.getWidth() * magnification), (int) (img.getHeight() * magnification));
	}

	/***
	 * ぼかし
	 * @param img
	 * @param filterSize
	 * @return
	 */
	public static CVImage blur(CVImage img, int filterSize) {
		BufferedImage img1 = img.getImageBuffer();
		BufferedImage img2 = new BufferedImage(
				img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D gr = img2.createGraphics();
		gr.drawImage(img1, 0, 0, null);
		gr.dispose();

		float kernel[] = new float[filterSize * filterSize];
		for (int li = 0; li < filterSize * filterSize; li++) {
			kernel[li] = 1F / (filterSize * filterSize);
		}

		BufferedImageOp bio = new ConvolveOp(new Kernel(3, 3, kernel),
				ConvolveOp.EDGE_NO_OP, new RenderingHints(new HashMap()));
		BufferedImage img3 = bio.filter(img2, null);

		return new CVImage(img3);
	}

	/***
	 * CVImageをSWTのImageに変換する
	 * @param image
	 * @return
	 */
	public static org.eclipse.swt.graphics.Image convertCVImage2SWTImage(CVImage image) {
		ImageData imd = new ImageData(image.getWidth(), image.getHeight(), 32, new PaletteData(0, 0, 0));
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Pixel p = image.getPixel(x, y);
				imd.setPixel(x, y, p.getPixel());
				int alpha = image.getPixel(x, y).getA();
				imd.setAlpha(x, y, alpha);
			}
		}
		return new org.eclipse.swt.graphics.Image(null, imd);
	}

	public static CVImage setFrame(CVImage img, Color edgeColor) {
		WritableRaster wr = img.getImageRaster();
		int color[] = { edgeColor.getRed(), edgeColor.getGreen(), edgeColor.getBlue(), edgeColor.getAlpha() };

		//top
		for (int i = 0; i < wr.getWidth(); i++) {
			wr.setPixel(i, 0, color);
		}

		//bottom
		for (int i = 0; i < wr.getWidth(); i++) {
			wr.setPixel(i, wr.getHeight() - 1, color);
		}

		//right
		for (int i = 0; i < wr.getHeight(); i++) {
			wr.setPixel(wr.getWidth() - 1, i, color);
		}

		//left
		for (int i = 0; i < wr.getHeight(); i++) {
			wr.setPixel(0, i, color);
		}

		img.setData(wr);

		return img;
	}
}
