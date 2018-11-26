package org.net.perorin.crablaser.win32

import org.eclipse.swt.internal.win32.OS

public class Win32Util {

	private Win32Util() {
	}

	public static List<ChildWindow> getChilds(int hwnd) {

		List<ChildWindow> childList = new ArrayList<>()
		List<Integer> longList = new ArrayList<>()
		boolean hasChild = false
		int childHwnd = OS.GetWindow(hwnd, OS.GW_CHILD)
		if (childHwnd != 0l) {
			hasChild = true
			longList.add(childHwnd)
		}

		while (hasChild) {
			childHwnd = OS.GetWindow(longList.get(longList.size() - 1), OS.GW_HWNDNEXT)
			if (childHwnd != 0l) {
				longList.add(childHwnd)
			} else {
				break
			}
		}

		if (hasChild) {
			for (Integer lhwnd : longList) {
				childList.add(new ChildWindow(lhwnd))
			}
		}

		return childList
	}
}
