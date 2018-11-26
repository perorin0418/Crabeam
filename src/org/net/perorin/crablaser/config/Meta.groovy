package org.net.perorin.crablaser.config

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

import org.net.perorin.crablaser.config.MetaCrabLaser

@XmlAccessorType( XmlAccessType.NONE )
public class Meta {

	public static final String META_PATH = "./contents/metadata"

	@XmlElement(name="crablaser")
	private MetaCrabLaser crablaser

	public MetaCrabLaser getCrablaser() {
		return crablaser
	}

	public void setCrablaser(MetaCrabLaser crablaser) {
		this.crablaser = crablaser
	}

}
