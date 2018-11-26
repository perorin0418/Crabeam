package org.net.perorin.crablaser.panel

import org.net.perorin.crablaser.config.Constant

public class ImageRandomizer {

	private ImageRandomizer() {
	}

	public static String getCastImagePath() {
		File castDir = new File(Constant.CAST_IMG_PATH)
		ArrayList<String> castList = new ArrayList<>()
		for (File cast : castDir.listFiles()) {
			if (cast.isFile()) {
				castList.add(cast.getPath())
			}
		}
		return castList.get((new Random()).nextInt(castList.size()))
	}

	public static String getBootingImagePath() {
		File bootDir = new File(Constant.BOOTING_IMG_PATH)
		ArrayList<String> bootList = new ArrayList<>()
		for (File cast : bootDir.listFiles()) {
			if (cast.isFile()) {
				bootList.add(cast.getPath())
			}
		}
		return bootList.get((new Random()).nextInt(bootList.size()))
	}
}
