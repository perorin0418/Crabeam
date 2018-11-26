package org.net.perorin.crablaser.config

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class ConfigCrabLaser {

	@XmlElement(name="on_top")
	private boolean on_top

	@XmlElement(name="save_full")
	private boolean save_full

	@XmlElement(name="save_path")
	private String save_path

	@XmlElement(name="save_type")
	private String save_type

	@XmlElement(name="split_location")
	private int split_location

	@XmlElement(name="cursor_type")
	private String cursor_type

	@XmlElement(name="image_width_limit")
	private int image_width_limit

	@XmlElement(name="image_height_limit")
	private int image_height_limit

	@XmlElement(name="comp_image")
	private boolean comp_image

	public boolean isOn_top() {
		return on_top
	}

	public boolean isSave_full() {
		return save_full
	}

	public String getSave_path() {
		return save_path
	}

	public String getSave_type() {
		return save_type
	}

	public void setOn_top(boolean on_top) {
		this.on_top = on_top
	}

	public void setSave_full(boolean save_full) {
		this.save_full = save_full
	}

	public void setSave_path(String save_path) {
		this.save_path = save_path
	}

	public void setSave_type(String save_type) {
		this.save_type = save_type
	}

	public int getSplit_location() {
		return split_location
	}

	public void setSplit_location(int split_location) {
		this.split_location = split_location
	}

	public String getCursor_type() {
		return cursor_type
	}

	public void setCursor_type(String cursor_type) {
		this.cursor_type = cursor_type
	}

	public int getImage_width_limit() {
		return image_width_limit
	}

	public void setImage_width_limit(int image_width_limit) {
		this.image_width_limit = image_width_limit
	}

	public int getImage_height_limit() {
		return image_height_limit
	}

	public void setImage_height_limit(int image_height_limit) {
		this.image_height_limit = image_height_limit
	}

	public boolean isComp_image() {
		return comp_image
	}

	public void setComp_image(boolean comp_image) {
		this.comp_image = comp_image
	}

}
