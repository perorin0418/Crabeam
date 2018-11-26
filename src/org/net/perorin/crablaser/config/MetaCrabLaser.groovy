package org.net.perorin.crablaser.config

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class MetaCrabLaser {

	@XmlElement(name="frame_height")
	private int frame_height = 0

	@XmlElement(name="frame_width")
	private int frame_width = 0

	@XmlElement(name="frame_x")
	private int frame_x = 0

	@XmlElement(name="frame_y")
	private int frame_y = 0

	public int getFrame_height() {
		return frame_height
	}

	public int getFrame_width() {
		return frame_width
	}

	public int getFrame_x() {
		return frame_x
	}

	public int getFrame_y() {
		return frame_y
	}

	public void setFrame_height(int frame_height) {
		this.frame_height = frame_height
	}

	public void setFrame_width(int frame_width) {
		this.frame_width = frame_width
	}

	public void setFrame_x(int frame_x) {
		this.frame_x = frame_x
	}

	public void setFrame_y(int frame_y) {
		this.frame_y = frame_y
	}
}
