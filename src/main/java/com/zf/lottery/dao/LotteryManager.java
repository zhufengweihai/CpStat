package com.zf.lottery.dao;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zf.lottery.common.Commons;
import com.zf.lottery.common.Utils;

public class LotteryManager {
	private static LotteryManager instance = new LotteryManager();
	private int count = -1;
	private int realCount = -1;
	private int maxTerm = -1;

	private int[] maxLastOne = null;
	private int[] maxFirstTwo = null;
	private int[] maxLastTwo = null;
	private Map<Integer, Integer> maxCombTwo = null;
	private int[] lastOne = null;
	private int[] firstThree = null;
	private int[] lastThree = null;
	private int[] firstTwo = null;
	private int[] lastTwo = null;
	private Map<Integer, Integer> groupSix = null;
	private Map<Integer, Integer> groupThree = null;
	private Map<Integer, Integer> combTwo = null;

	private LotteryManager() {

	}

	public static LotteryManager instance() {
		return instance;
	}

	public int getMaxTerm() {
		return maxTerm;
	}

	public void setMaxTerm(int maxTerm) {
		this.maxTerm = maxTerm;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRealCount() {
		return realCount;
	}

	public void setRealCount(int realCount) {
		this.realCount = realCount;
	}

	public void increaseCount() {
		count++;
	}

	public int[] getMaxLastOne() {
		return maxLastOne;
	}

	public void setMaxLastOne(int[] maxLastOne) {
		this.maxLastOne = maxLastOne;
	}

	public int[] getMaxFirstTwo() {
		return maxFirstTwo;
	}

	public void setMaxFirstTwo(int[] maxFirstTwo) {
		this.maxFirstTwo = maxFirstTwo;
	}

	public int[] getMaxLastTwo() {
		return maxLastTwo;
	}

	public void setMaxLastTwo(int[] maxLastTwo) {
		this.maxLastTwo = maxLastTwo;
	}

	public Map<Integer, Integer> getMaxCombTwo() {
		return maxCombTwo;
	}

	public void setMaxCombTwo(Map<Integer, Integer> maxCombTwo) {
		this.maxCombTwo = maxCombTwo;
	}

	public int[] getFirstThree() {
		return firstThree;
	}

	public void setFirstThree(int[] firstThree) {
		this.firstThree = firstThree;
	}

	public void updateFirstThree(int number) {
		for (int i = 0; i < firstThree.length; i++) {
			firstThree[i]++;
		}
		firstThree[number / Commons.TWO] = 0;
		firstThree[number % Commons.THREE] = 0;
	}

	public int[] getLastThree() {
		return lastThree;
	}

	public void setLastThree(int[] lastThree) {
		this.lastThree = lastThree;
	}

	public void updateLastThree(int number) {
		for (int i = 0; i < lastThree.length; i++) {
			lastThree[i]++;
		}
		lastThree[number % Commons.THREE] = 0;
	}

	public int[] getFirstTwo() {
		return firstTwo;
	}

	public void setFirstTwo(int[] firstTwo) {
		this.firstTwo = firstTwo;
	}

	public void updateFirstTwo(int number) {
		for (int i = 0; i < firstTwo.length; i++) {
			firstTwo[i]++;
		}
		firstTwo[number / Commons.THREE] = 0;
		firstTwo[number % Commons.TWO] = 0;

		for (int i = 0; i < firstTwo.length; i++) {
			if (firstTwo[i] >= maxFirstTwo[i]) {
				maxFirstTwo[i] = firstTwo[i];
			}
		}
	}

	public int[] getLastTwo() {
		return lastTwo;
	}

	public void setLastTwo(int[] lastTwo) {
		this.lastTwo = lastTwo;
	}

	public void updateLastTwo(int number) {
		for (int i = 0; i < lastTwo.length; i++) {
			lastTwo[i]++;
		}
		lastTwo[number % Commons.TWO] = 0;

		for (int i = 0; i < lastTwo.length; i++) {
			if (lastTwo[i] >= maxLastTwo[i]) {
				maxLastTwo[i] = lastTwo[i];
			}
		}
	}

	public int[] getLastOne() {
		return lastOne;
	}

	public void setLastOne(int[] lastone) {
		this.lastOne = lastone;
	}

	public void updateLastOne(int number) {
		for (int i = 0; i < lastOne.length; i++) {
			lastOne[i]++;
		}
		lastOne[number % Commons.ONE] = 0;
		for (int i = 0; i < lastOne.length; i++) {
			if (lastOne[i] >= maxLastOne[i]) {
				maxLastOne[i] = lastOne[i];
			}
		}
	}
	
	public Map<Integer, Integer> getGroupSix() {
		return groupSix;
	}

	public void setGroupSix(Map<Integer, Integer> groupSix) {
		this.groupSix = groupSix;
	}

	public void updateGroupSix(int number) {
		Set<Entry<Integer, Integer>> entrySet = groupSix.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			entry.setValue(entry.getValue() + 1);
		}
		Integer num = Utils.arrange3(number % Commons.THREE);
		groupSix.replace(num, 0);
	}

	public Map<Integer, Integer> getGroupThree() {
		return groupThree;
	}

	public void setGroupThree(Map<Integer, Integer> groupThree) {
		this.groupThree = groupThree;
	}

	public void updateGroupThree(int number) {
		Set<Entry<Integer, Integer>> entrySet = groupThree.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			entry.setValue(entry.getValue() + 1);
		}
		Integer num = Utils.arrange3(number % Commons.THREE);
		groupThree.replace(num, 0);
	}

	public Map<Integer, Integer> getCombTwo() {
		return combTwo;
	}

	public void setCombTwo(Map<Integer, Integer> combTwo) {
		this.combTwo = combTwo;
	}

	public void updateCombTwo(int number) {
		Set<Entry<Integer, Integer>> entrySet = combTwo.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			entry.setValue(entry.getValue() + 1);
		}
		int num = Utils.getCombTwoMin(number % Commons.TWO);
		combTwo.replace(num, 0);

		for (Entry<Integer, Integer> entry : entrySet) {
			Integer n = entry.getKey();
			Integer absence = entry.getValue();
			if (maxCombTwo.get(n) <= absence) {
				maxCombTwo.put(n, absence);
			}
		}
	}
}
