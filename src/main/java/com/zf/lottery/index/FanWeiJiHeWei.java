package com.zf.lottery.index;

public class FanWeiJiHeWei implements IndexStrategy {

	@Override
	public int calcIndex(int... numbers) {
		return (numbers[0] * (4 - 1) + numbers[1] * (4 - 2) + numbers[2] * (4 - 3)) % 10;
	}

}
