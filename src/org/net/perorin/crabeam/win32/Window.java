package org.net.perorin.crabeam.win32;

import java.util.List;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.net.perorin.crabeam.exception.HwndNotFoundException;

public class Window {

	private TitleWindow title = null;
	private List<ChildWindow> childList = null;

	public Window(String title) throws HwndNotFoundException {
		this.title = new TitleWindow(title);
		childList = Win32Util.getChilds(this.title.getHwnd());
	}

	/***
	 * nCmdShowの取りうる値<br>
	 * SW_FORCEMINIMIZE	ウィンドウを所有するスレッドがハングしても、ウィンドウを最小化する。<br>
	 * SW_HIDE	ウィンドウを非表示にし、他のウィンドウをアクティブにする<br>
	 * SW_MAXIMIZE	ウィンドウを最大化する<br>
	 * SW_MINIMIZE	ウィンドウを最小化し、Zオーダーが次のトップレベルウィンドウをアクティブにする。<br>
	 * SW_RESTORE	ウィンドウをアクティブにして表示する。最大化または最小化されていたウィンドウは元の位置とサイズに戻る。<br>
	 * SW_SHOW	ウィンドウをアクティブにして、現在の位置とサイズで表示する。<br>
	 * SW_SHOWMAXIMIZED	ウィンドウを最大化する<br>
	 * SW_SHOWMINIMIZED	ウィンドウを最小化する<br>
	 * SW_SHOWMINNOACTIVE	ウィンドウを現在のサイズと位置で表示する。<br>
	 * SW_SHOWNORMAL	ウィンドウをアクティブにして表示する。初めてウィンドウを表示するときには、このフラグを指定せよ。<br>
	 * @param nCmdShow
	 */
	public void showWindow(int nCmdShow) {
		OS.ShowWindow(title.getHwnd(), nCmdShow);
	}

	/***
	 * ウィンドウの位置を設定
	 * @param pos
	 */
	public void setWindowPos(Position pos) {
		setWindowPos(pos.getX(), pos.getY());
	}

	/***
	 * ウィンドウの位置を設定
	 * @param x
	 * @param y
	 */
	public void setWindowPos(int x, int y) {
		Size size = getWindowSize();
		OS.SetWindowPos(title.getHwnd(), 0, x, y, size.getWidth(), size.getHeight(), 0);
	}

	/***
	 * ウィンドウの高さ、幅を設定
	 * @param size
	 */
	public void setWindowSize(Size size) {
		setWindowSize(size.getWidth(), size.getHeight());
	}

	/***
	 * ウィンドウの高さ、幅を設定
	 * @param width
	 * @param height
	 */
	public void setWindowSize(int width, int height) {
		Position pos = getWindowPos();
		OS.SetWindowPos(title.getHwnd(), 0, pos.getX(), pos.getY(), width, height, 0);
	}

	/***
	 * ウィンドウの位置を取得
	 * @return
	 */
	public Position getWindowPos() {
		RECT rec = new RECT();
		OS.GetWindowRect(title.getHwnd(), rec);
		return new Position(rec.left, rec.top);
	}

	/***
	 * ウィンドウの幅、高さを取得
	 * @return
	 */
	public Size getWindowSize() {
		RECT rec = new RECT();
		OS.GetWindowRect(title.getHwnd(), rec);
		return new Size(rec.right - rec.left, rec.bottom - rec.top);
	}

	/***
	 * ウィンドウを最前面に出す
	 */
	public void foreGround() {
		OS.SetForegroundWindow(title.getHwnd());
	}

	public TitleWindow getTitle() {
		return title;
	}

	public List<ChildWindow> getChildList() {
		return childList;
	}

	public ChildWindow get(int index) {
		return childList.get(index);
	}

	public static void main(String[] args) throws HwndNotFoundException {
		Window w = new Window("VOICEROID＋ 琴葉茜*");
		System.out.println(w.getTitle().getHwndHex());

		w.get(0).get(1).get(0).get(0).get(0).get(0).get(0).get(0).sendText("hoge");
		System.out.println(w.get(0).get(1).get(0).get(0).get(0).get(0).get(0).get(0).getHwndHex());
		System.out.println(w.get(0).get(1).get(0).get(0).get(0).get(0).get(0).get(0).getText());
		System.out.println(w.get(0).get(1).get(0).get(1).get(0).getClassName());
//		w.get(0).get(1).get(0).get(1).get(0).click();
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_RIGHT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_RIGHT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_RIGHT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_RIGHT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_LEFT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_LEFT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_LEFT);
		w.get(0).get(1).get(0).get(1).get(0).sendKey(OS.VK_LEFT);
	}

}
