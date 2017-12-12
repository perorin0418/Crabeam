package org.net.perorin.crabeam.format;

public class EvidenceInformation {

	private int order;
	private String evidence_name;
	private String image_name;
	private int image_width;
	private int image_height;
	private String bdd;
	private String contents;
	private String evi_no_address;

	public String getEvidence_name() {
		return evidence_name;
	}

	public void setEvidence_name(String evidence_name) {
		this.evidence_name = evidence_name;
	}

	public String getImage_name() {
		return image_name;
	}

	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	public int getImage_width() {
		return image_width;
	}

	public void setImage_width(int image_width) {
		this.image_width = image_width;
	}

	public int getImage_height() {
		return image_height;
	}

	public void setImage_height(int image_height) {
		this.image_height = image_height;
	}

	public String getBdd() {
		return bdd;
	}

	public void setBdd(String bdd) {
		this.bdd = bdd;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getEvi_no_address() {
		return evi_no_address;
	}

	public void setEvi_no_address(String evi_no_address) {
		this.evi_no_address = evi_no_address;
	}


}
