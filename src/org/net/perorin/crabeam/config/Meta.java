package org.net.perorin.crabeam.config;

public class Meta {

	public static final String META_PATH = "./contents/metadata";

	private MetaCrabeam crabeam;
	private MetaCrabLaser crablaser;

	public MetaCrabeam getCrabeam() {
		return crabeam;
	}

	public MetaCrabLaser getCrablaser() {
		return crablaser;
	}

	public void setCrabeam(MetaCrabeam crabeam) {
		this.crabeam = crabeam;
	}

	public void setCrablaser(MetaCrabLaser crablaser) {
		this.crablaser = crablaser;
	}

}
