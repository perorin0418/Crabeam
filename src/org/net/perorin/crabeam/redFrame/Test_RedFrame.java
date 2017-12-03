package org.net.perorin.crabeam.redFrame;

import java.util.ArrayDeque;
import java.util.Queue;

public class Test_RedFrame {

	public static void main(String[] args)  {

		Queue<String> que = new ArrayDeque<String>(0);
		que.offer("1");
		que.offer("2");
		que.offer("3");
		que.offer("4");
		que.offer("5");
		System.out.println(que);

		que.poll();
		que.offer("6");
		System.out.println(que);


	}

}
