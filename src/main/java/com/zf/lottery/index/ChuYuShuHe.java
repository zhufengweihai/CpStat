package com.zf.lottery.index;

public class ChuYuShuHe implements IndexStrategy {
	private int divisor = 0;

	public ChuYuShuHe(int divisor) {
		super();
		this.divisor = divisor;
	}

	@Override
	public int calcIndex(int... numbers) {
		int sum = 0;
		for (int n : numbers) {
			sum += n % divisor;
		}
		return sum;
	}

}
