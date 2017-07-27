package com.zf.lottery.data;

import java.util.Arrays;

public class Lottery {
	private int term = -1;
	private int time = -1;
	private int number = -1;
	private int[] absences = null;

	public int getTerm() {
		return term;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public int getNumber() {
		return number;
	}

	public void setNumbers(int number) {
		this.number = number;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int[] getAbsences() {
		return absences;
	}

	public void setAbsences(int[] absences) {
		this.absences = absences;
	}

	@Override
	public String toString() {
		return "Lottery [term=" + term + ", time=" + time + ", number=" + number + ", absences="
				+ Arrays.toString(absences) + "]";
	}
}
