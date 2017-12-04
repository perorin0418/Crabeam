package org.net.perorin.crabeam.logic;

import java.io.File;

import org.net.perorin.crabeam.cv.CVImage;

public class CacheManeger {

	private static String CACHE_FOLDER = "./contents/cache/";

	public static void cacheImage(String key, CVImage img) {
		System.out.println("cache : " + key + "," + key.hashCode());
		img.save(CACHE_FOLDER + key.hashCode());
	}

	public static CVImage uncacheImage(String key) {
		File caches[] = new File(CACHE_FOLDER).listFiles();
		for (File cache : caches) {
			String hashCode = String.valueOf(key.hashCode());
			if (hashCode.equals(cache.getName())) {
				return new CVImage(cache);
			}
		}
		return null;
	}

}
