package org.net.perorin.crablaser.config

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

import org.net.perorin.crablaser.config.ConfigCrabLaser

@XmlAccessorType( XmlAccessType.NONE )
public class Config {

	/** コンフィグファイルの格納パス */
	public final static String CONFIG_PATH = "./contents/config/config.xml"

	/** crablaserのコンフィグ */
	@XmlElement(name="crablaser")
	private ConfigCrabLaser crablaser

	/**
	 * crablaserのコンフィグのgetter
	 *
	 * @return
	 */
	public ConfigCrabLaser getCrablaser() {
		return crablaser
	}

	/**
	 * crablaserのコンフィグのsetter
	 *
	 * @return
	 */
	public void setCrablaser(ConfigCrabLaser crablaser) {
		this.crablaser = crablaser
	}
}
