package org.net.perorin.crablaser.format

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement

@XmlAccessorType( XmlAccessType.NONE )
public class EvidenceInformation {

	@XmlElement(name="order")
	private int order

	@XmlElement(name="evidence_name")
	private String evidence_name

	@XmlElement(name="image_name")
	private String image_name

	@XmlElement(name="image_width")
	private int image_width

	@XmlElement(name="image_height")
	private int image_height

	@XmlElement(name="bdd")
	private String bdd

	@XmlElement(name="contents")
	private String contents

	@XmlElement(name="evi_no_address")
	private String evi_no_address

	public String getEvidence_name() {
		return evidence_name
	}

	public void setEvidence_name(String evidence_name) {
		this.evidence_name = evidence_name
	}

	public String getImage_name() {
		return image_name
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name
	}

	public int getImage_width() {
		return image_width
	}

	public void setImage_width(int image_width) {
		this.image_width = image_width
	}

	public int getImage_height() {
		return image_height
	}

	public void setImage_height(int image_height) {
		this.image_height = image_height
	}

	public String getBdd() {
		return bdd
	}

	public void setBdd(String bdd) {
		this.bdd = bdd
	}

	public String getContents() {
		return contents
	}

	public void setContents(String contents) {
		this.contents = contents
	}

	public int getOrder() {
		return order
	}

	public void setOrder(int order) {
		this.order = order
	}

	public String getEvi_no_address() {
		return evi_no_address
	}

	public void setEvi_no_address(String evi_no_address) {
		this.evi_no_address = evi_no_address
	}


}
