package org.net.perorin.crablaser.format

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class HeadItem {

	@XmlElement(name="name")
	private String name

	@XmlElement(name="require")
	private boolean require

	@XmlElement(name="address")
	private int address

	@XmlElement(name="order")
	private int order

	@XmlElement(name="type")
	private String type

	@XmlElement(name="show_name")
	private boolean show_name

	public String getName() {
		return name
	}

	public void setName(String name) {
		this.name = name
	}

	public boolean isRequire() {
		return require
	}

	public void setRequire(boolean require) {
		this.require = require
	}

	public int getAddress() {
		return address
	}

	public void setAddress(int address) {
		this.address = address
	}

	public int getOrder() {
		return order
	}

	public void setOrder(int order) {
		this.order = order
	}

	public String getType() {
		return type
	}

	public void setType(String type) {
		this.type = type
	}

	public boolean isShow_name() {
		return show_name
	}

	public void setShow_name(boolean show_name) {
		this.show_name = show_name
	}

}
