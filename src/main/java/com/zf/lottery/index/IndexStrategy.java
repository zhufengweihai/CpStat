package com.zf.lottery.index;

public interface IndexStrategy {
	public int calcIndex(int number);

	public default int[] toArray(int number) {
		return new int[] { number / 10, number % 10 };
	}
}
