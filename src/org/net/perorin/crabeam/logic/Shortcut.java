package org.net.perorin.crabeam.logic;

public class Shortcut {

	private String shortcut;
	private int keyCode;
	private int modifiers;

	public Shortcut(String shortcut, int keyCode, int modifiers) {
		this.shortcut = shortcut;
		this.keyCode = keyCode;
		this.modifiers = modifiers;
	}

	public String getShortcut() {
		return shortcut;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
}
