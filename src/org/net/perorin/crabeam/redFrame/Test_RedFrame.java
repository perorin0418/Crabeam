package org.net.perorin.crabeam.redFrame;

import java.awt.Cursor;

public class Test_RedFrame {

	public static void main(String[] args) throws InterruptedException {

		Thread.sleep(10000);

		Cursor c = Cursor.getDefaultCursor();
		System.out.println(c.getType());
	}

}
