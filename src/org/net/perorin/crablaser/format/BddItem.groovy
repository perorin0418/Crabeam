package org.net.perorin.crablaser.format

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class BddItem {

	@XmlElement(name="name")
	private String name

	@XmlElement(name="address")
	private int address

	public String getName() {
		return name
	}

	public void setName(String name) {
		this.name = name
	}

	public int getAddress() {
		return address
	}

	public void setAddress(int address) {
		this.address = address
	}

}
