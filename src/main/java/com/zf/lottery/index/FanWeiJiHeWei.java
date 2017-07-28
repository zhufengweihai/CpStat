package com.zf.lottery.index;

public class FanWeiJiHeWei implements IndexStrategy {

	@Override
	public int calcIndex(int number) {
		int[] array = toArray(number);
		return (array[0] * (4 - 1) + array[1] * (4 - 2) + array[2] * (4 - 3)) % 10;
	}

}
