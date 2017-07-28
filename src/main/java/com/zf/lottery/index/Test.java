package com.zf.lottery.index;

public class Test {

	public static void main(String[] args) {
		int x = 0;
		IndexStrategy strategy = new ChuYuShuHe(7);
		for (int i = 0; i < 100; i++) {
			int index = strategy.calcIndex(i);
			if (index == 3 || index == 8) {
				System.out.println(i);
				x++;
			}
		}
		System.out.println("count:" + x);
	}
}
