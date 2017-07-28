package com.zf.lottery.index;

public class ChuYuShuHe implements IndexStrategy {
	private int divisor = 0;

	public ChuYuShuHe(int divisor) {
		super();
		this.divisor = divisor;
	}

	@Override
	public int calcIndex(int number) {
		int sum = 0;
		int[] array = toArray(number);
		for (int n : array) {
			sum += n % divisor;
		}
		return sum;
	}

}
