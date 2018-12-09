package org.net.perorin.crablaser.config

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

/**
 * コンフィグの根幹クラス
 *
 * @author perorin
 */
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
	 * @return crablaserのコンフィグ
	 */
	public ConfigCrabLaser getCrablaser() {
		return crablaser
	}

	/**
	 * crablaserのコンフィグのsetter
	 *
	 * @param crablaser コンフィグ
	 */
	public void setCrablaser(ConfigCrabLaser crablaser) {
		this.crablaser = crablaser
	}
}
