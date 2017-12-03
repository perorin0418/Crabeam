package org.net.perorin.crabeam.config;

public class ConfigCrabLaser {

	private boolean on_top;
	private boolean save_full;
	private String save_path;
	private String save_type;
	private String given_ext;
	private String when_ext;
	private String then_ext;

	public boolean isOn_top() {
		return on_top;
	}

	public boolean isSave_full() {
		return save_full;
	}

	public String getSave_path() {
		return save_path;
	}

	public String getSave_type() {
		return save_type;
	}

	public String getGiven_ext() {
		return given_ext;
	}

	public String getWhen_ext() {
		return when_ext;
	}

	public String getThen_ext() {
		return then_ext;
	}

	public void setOn_top(boolean on_top) {
		this.on_top = on_top;
	}

	public void setSave_full(boolean save_full) {
		this.save_full = save_full;
	}

	public void setSave_path(String save_path) {
		this.save_path = save_path;
	}

	public void setSave_type(String save_type) {
		this.save_type = save_type;
	}

	public void setGiven_ext(String given_ext) {
		this.given_ext = given_ext;
	}

	public void setWhen_ext(String when_ext) {
		this.when_ext = when_ext;
	}

	public void setThen_ext(String then_ext) {
		this.then_ext = then_ext;
	}

}
