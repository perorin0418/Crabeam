package org.net.perorin.crablaser.win32

import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.TCHAR

public class ChildWindow {

	private List<ChildWindow> childList = null
	private int hwnd = -1
	private String className = ""

	public ChildWindow(int hwnd) {
		this.hwnd = hwnd

		TCHAR _className = new TCHAR(OS.CP_INSTALLED, 256)
		OS.GetClassName(hwnd, _className, 256)
		className = _className.toString(0, _className.strlen())

		childList = Win32Util.getChilds(hwnd)
	}

	public void sendText(String text) {
		for (int i = 0; i < text.length(); i++) {
			OS.SendMessage(hwnd, OS.WM_CHAR, text.charAt(i), 0)
		}
	}

	public String getText() {
		int length = OS.GetWindowTextLength(hwnd)
		TCHAR text = new TCHAR(OS.CP_INSTALLED, length)
		OS.GetWindowText(hwnd, text, length)
		return text.toString(0, text.strlen())
	}

	public void sendKey(int key) {
		OS.SendMessage(hwnd, OS.WM_KEYDOWN, key, 0)
	}

	public void click() {
		OS.SendMessage(hwnd, OS.BM_CLICK, 0, 0)
	}

	public List<ChildWindow> getChildList() {
		return childList
	}

	public ChildWindow get(int index) {
		return childList.get(index)
	}

	public long getHwnd() {
		return hwnd
	}

	public String getHwndHex() {
		return Long.toHexString(hwnd)
	}

	public String getClassName() {
		return className
	}

}
