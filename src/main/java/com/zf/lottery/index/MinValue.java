package com.zf.lottery.index;

public class MinValue implements IndexStrategy {

	@Override
	public int calcIndex(int number) {
		int[] array = toArray(number);
		int minValue = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] < minValue)
				minValue = array[i];
		}
		return minValue;
	}

}
