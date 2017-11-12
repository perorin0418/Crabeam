package org.net.perorin.crabeam.font;

import java.awt.Font;
import java.io.File;

import org.net.perorin.crabeam.config.Constant;

public class NicoFont {

	private static Font font = null;

	private NicoFont() {
	}

	public static Font getFont() {
		if (font == null) {
			try {
				font = Font.createFont(Font.TRUETYPE_FONT, new File(Constant.NICO_FONT_PATH));
				font = font.deriveFont(16f);
			} catch (Exception e) {
				font = new Font("MS UI Gothic", Font.PLAIN, 16);
			}
			return font;
		} else {
			return font;
		}
	}

}
