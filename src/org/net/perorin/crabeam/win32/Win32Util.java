package org.net.perorin.crabeam.win32;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.internal.win32.OS;

public class Win32Util {

	private Win32Util() {
	}

	public static List<ChildWindow> getChilds(long hwnd) {

		List<ChildWindow> childList = new ArrayList<>();
		List<Long> longList = new ArrayList<>();
		boolean hasChild = false;
		long childHwnd = OS.GetWindow(hwnd, OS.GW_CHILD);
		if (childHwnd != 0l) {
			hasChild = true;
			longList.add(childHwnd);
		}

		while (hasChild) {
			childHwnd = OS.GetWindow(longList.get(longList.size() - 1), OS.GW_HWNDNEXT);
			if (childHwnd != 0l) {
				longList.add(childHwnd);
			} else {
				break;
			}
		}

		if (hasChild) {
			for (Long lhwnd : longList) {
				childList.add(new ChildWindow(lhwnd));
			}
		}

		return childList;
	}
}
