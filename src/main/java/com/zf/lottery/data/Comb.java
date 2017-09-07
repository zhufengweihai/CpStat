package com.zf.lottery.data;

public class Comb {
	private String number = null;

	public Comb(String number) {
		super();
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		int length = number.length();
		int sum = number.charAt(0) - 30;
		for (int i = 1; i < length; i++) {
			sum *= number.charAt(i) - 30;
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
		Comb other = (Comb) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		}
		return isPermutation(number, other.number);
	}

	public static boolean isPermutation(String strA, String strB) {
		if (strA.length() != strB.length()) {
			return false;
		}

		int[] chars = new int[128];
		for (int i = 0; i < strA.length(); i++) {
			chars[strA.charAt(i)] += 1;
		}

		for (int i = 0; i < strB.length(); i++) {
			chars[strB.charAt(i)] -= 1;

			if (chars[strB.charAt(i)] < 0) {
				return false;
			}
		}
		return true;
	}
}
