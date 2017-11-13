package org.net.perorin.crabeam.config;

public class Config {

	public final static String CONFIG_PATH = "./contents/config/config.xml";

	private String format;
	private boolean onTop;
	private boolean saveFull;
	private String savePath;
	private String saveType;
	private String givenExt;
	private String whenExt;
	private String thenExt;
	private int autoCountUpNo;
	private boolean autoCountUp;

	public String getFormat() {
		return format;
	}

	public boolean isOnTop() {
		return onTop;
	}

	public boolean isSaveFull() {
		return saveFull;
	}

	public String getSavePath() {
		return savePath;
	}

	public String getSaveType() {
		return saveType;
	}

	public String getGivenExt() {
		return givenExt;
	}

	public String getWhenExt() {
		return whenExt;
	}

	public String getThenExt() {
		return thenExt;
	}

	public boolean isAutoCountUp() {
		return autoCountUp;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setOnTop(boolean onTop) {
		this.onTop = onTop;
	}

	public void setSaveFull(boolean saveFull) {
		this.saveFull = saveFull;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public void setGivenExt(String givenExt) {
		this.givenExt = givenExt;
	}

	public void setWhenExt(String whenExt) {
		this.whenExt = whenExt;
	}

	public void setThenExt(String thenExt) {
		this.thenExt = thenExt;
	}

	public void setAutoCountUp(boolean autoCountUp) {
		this.autoCountUp = autoCountUp;
	}

	public int getAutoCountUpNo() {
		return autoCountUpNo;
	}

	public void setAutoCountUpNo(int autoCountUpNo) {
		this.autoCountUpNo = autoCountUpNo;
	}

}
