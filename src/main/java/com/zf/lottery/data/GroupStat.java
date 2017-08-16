package com.zf.lottery.data;

public class GroupStat {
	public static final String KEY_MAX_STAT = "MaxStat";

	private int type = -1;
	private int[] number = null;
	private int[] absences = null;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int[] getNumber() {
		return number;
	}

	public void setNumber(int... number) {
		this.number = number;
	}

	public int[] getAbsences() {
		return absences;
	}

	public void setAbsences(int... absences) {
		this.absences = absences;
	}

}
