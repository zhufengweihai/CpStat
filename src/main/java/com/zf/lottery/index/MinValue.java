package com.zf.lottery.index;

public class MinValue implements IndexStrategy {

	@Override
	public int calcIndex(int... numbers) {
		int minValue = numbers[0];
		for (int i = 0; i < numbers.length; i++) {
			if (numbers[i] < minValue)
				minValue = numbers[i];
		}
		return minValue;
	}

}
