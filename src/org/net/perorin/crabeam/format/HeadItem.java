package org.net.perorin.crabeam.format;

public class HeadItem {

	private String name;
	private boolean require;
	private int address;
	private int order;
	private String type;
	private boolean show_name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRequire() {
		return require;
	}

	public void setRequire(boolean require) {
		this.require = require;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isShow_name() {
		return show_name;
	}

	public void setShow_name(boolean show_name) {
		this.show_name = show_name;
	}

}
