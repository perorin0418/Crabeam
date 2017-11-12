package org.net.perorin.crabeam.window;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.net.perorin.crabeam.config.Constant;

public class CastRandomizer {

	private CastRandomizer() {
	}

	public static String getCastImagePath() {
		File castDir = new File(Constant.CAST_IMG_PATH);
		ArrayList<String> castList = new ArrayList<>();
		for (File cast : castDir.listFiles()) {
			if (cast.isFile()) {
				castList.add(cast.getPath());
			}
		}
		return castList.get((new Random()).nextInt(castList.size() - 1));
	}
}
