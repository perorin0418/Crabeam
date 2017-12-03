package org.net.perorin.crabeam.config;

public class Config {

	public final static String CONFIG_PATH = "./contents/config/config.xml";

	private ConfigCrabeam crabeam;
	private ConfigCrabLaser crablaser;

	public ConfigCrabeam getCrabeam() {
		return crabeam;
	}

	public ConfigCrabLaser getCrablaser() {
		return crablaser;
	}

	public void setCrabeam(ConfigCrabeam crabeam) {
		this.crabeam = crabeam;
	}

	public void setCrablaser(ConfigCrabLaser crablaser) {
		this.crablaser = crablaser;
	}
}
