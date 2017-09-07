package com.zf.lottery.data;

public class Perm {
	private int[] nums = null;

	public Perm(int[] nums) {
		this.nums = nums;
	}

	@Override
	public int hashCode() {
		int sum = 0;
		for (int i : nums) {
			sum += i;
		}
		return sum;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		int[] ints = new int[10];
		Perm other = (Perm) obj;
		for (int i : nums) {
			ints[i]++;
		}
		for (int i : other.nums) {
			ints[i]--;
			if (ints[i] < 0) {
				return false;
			}
		}
		return true;
	}

}
