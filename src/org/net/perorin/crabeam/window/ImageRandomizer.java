package org.net.perorin.crabeam.window;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.net.perorin.crabeam.config.Constant;

public class ImageRandomizer {

	private ImageRandomizer() {
	}

	public static String getCastImagePath() {
		File castDir = new File(Constant.CAST_IMG_PATH);
		ArrayList<String> castList = new ArrayList<>();
		for (File cast : castDir.listFiles()) {
			if (cast.isFile()) {
				castList.add(cast.getPath());
			}
		}
		return castList.get((new Random()).nextInt(castList.size()));
	}

	public static String getBootingImagePath() {
		File bootDir = new File(Constant.BOOTING_IMG_PATH);
		ArrayList<String> bootList = new ArrayList<>();
		for (File cast : bootDir.listFiles()) {
			if (cast.isFile()) {
				bootList.add(cast.getPath());
			}
		}
		return bootList.get((new Random()).nextInt(bootList.size()));
	}
}
