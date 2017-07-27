package com.zf.lottery.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zf.lottery.common.Commons;
import com.zf.lottery.common.Utils;
import com.zf.lottery.data.Lottery;

public class CalcService {

	public Map<Integer, Integer> calcMaxCombTwo(List<Lottery> lotteries) {
		Map<Integer, ArrayList<Integer>> absencesMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int j = i; j < 10; j++) {
				absencesMap.put(i * 10 + j, new ArrayList<>());
			}
		}
		int[] lasts = new int[Commons.TWO];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() % Commons.TWO;
			int min = Utils.getComTowMin(num);
			ArrayList<Integer> arrayList = absencesMap.get(min);
			int absence = i - lasts[min];
			arrayList.add(absence);
			lasts[min] = i;
		}

		Map<Integer, Integer> absenceMap = new HashMap<>();
		Set<Entry<Integer, ArrayList<Integer>>> entrySet = absencesMap.entrySet();
		for (Entry<Integer, ArrayList<Integer>> entry : entrySet) {
			absenceMap.put(entry.getKey(), Collections.max(entry.getValue()));
		}
		return absenceMap;
	}

	public int[] calcMaxLastTwo(List<Lottery> lotteries) {
		ArrayList<Integer>[] absences = new ArrayList[Commons.TWO];
		for (int i = 0; i < Commons.TWO; i++) {
			absences[i] = new ArrayList<Integer>();
		}
		int[] lasts = new int[Commons.TWO];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() % Commons.TWO;
			absences[num].add(i - lasts[num]);
			lasts[num] = i;
		}

		int[] maxLastTwo = new int[Commons.TWO];
		for (int i = 0; i < absences.length; i++) {
			maxLastTwo[i] = Collections.max(absences[i]);
		}

		return maxLastTwo;
	}

	public int[] calcMaxFirstTwo(List<Lottery> lotteries) {
		ArrayList<Integer>[] absences = new ArrayList[Commons.TWO];
		for (int i = 0; i < Commons.TWO; i++) {
			absences[i] = new ArrayList<Integer>();
		}
		int[] lasts = new int[Commons.TWO];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() / Commons.THREE;
			absences[num].add(i - lasts[num]);
			lasts[num] = i;
		}

		int[] maxFirstTwo = new int[Commons.TWO];
		for (int i = 0; i < Commons.TWO; i++) {
			maxFirstTwo[i] = Collections.max(absences[i]);
		}
		return maxFirstTwo;
	}

	public Map<Integer, Integer> calcMaxCombThree(List<Lottery> lotteries) {
		Map<Integer, ArrayList<Integer>> absencesMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int j = i; j < 10; j++) {
				for (int k = j; k < 10; k++) {
					absencesMap.put(i * 100 + j * 10 + k, new ArrayList<>());
				}
			}
		}
		int[] lasts = new int[Commons.THREE];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() % Commons.THREE;
			Integer min = Utils.arrange3(num);
			ArrayList<Integer> arrayList = absencesMap.get(min);
			arrayList.add(i - lasts[min]);
			lasts[min] = i;
		}

		Map<Integer, Integer> absenceMap = new HashMap<>();
		Set<Entry<Integer, ArrayList<Integer>>> entrySet = absencesMap.entrySet();
		for (Entry<Integer, ArrayList<Integer>> entry : entrySet) {
			absenceMap.put(entry.getKey(), Collections.max(entry.getValue()));
		}
		return absenceMap;
	}

	public int[] calcMaxFirstThree(List<Lottery> lotteries) {
		ArrayList<Integer>[] absences = new ArrayList[Commons.THREE];
		for (int i = 0; i < Commons.THREE; i++) {
			absences[i] = new ArrayList<Integer>();
		}
		int[] lasts = new int[Commons.THREE];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() / Commons.TWO;
			absences[num].add(i - lasts[num]);
			lasts[num] = i;
		}

		int[] maxFirstThree = new int[Commons.THREE];
		for (int i = 0; i < absences.length; i++) {
			maxFirstThree[i] = Collections.max(absences[i]);

		}
		return maxFirstThree;
	}

	public int[] calcMaxLastThree(List<Lottery> lotteries) {
		ArrayList<Integer>[] absences = new ArrayList[Commons.THREE];
		for (int i = 0; i < Commons.THREE; i++) {
			absences[i] = new ArrayList<Integer>();
		}
		int[] lasts = new int[Commons.THREE];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() % Commons.THREE;
			absences[num].add(i - lasts[num]);
			lasts[num] = i;
		}

		int[] maxLastThree = new int[Commons.THREE];
		for (int i = 0; i < absences.length; i++) {
			maxLastThree[i] = Collections.max(absences[i]);
		}

		return maxLastThree;
	}

	public int[] calcLatestFirstThree(List<Lottery> lotteries) {
		int[] firstThree = new int[Commons.THREE];
		for (int i = 0; i < firstThree.length; i++) {
			firstThree[i] = -1;
		}
		int size = lotteries.size() - 1;
		int count = 0;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ft = lottery.getNumber() / Commons.TWO;
			if (firstThree[ft] < 0) {
				firstThree[ft] = size - i;
				if (++count == Commons.THREE) {
					return firstThree;
				}
			}
		}
		return firstThree;
	}

	public int[] calcLatestLastThree(List<Lottery> lotteries) {
		int[] lastThree = new int[Commons.THREE];
		for (int i = 0; i < lastThree.length; i++) {
			lastThree[i] = -1;
		}
		int size = lotteries.size() - 1;
		int count = 0;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int lt = lottery.getNumber() % Commons.THREE;
			if (lastThree[lt] < 0) {
				lastThree[lt] = size - i;
				if (++count == Commons.THREE) {
					return lastThree;
				}
			}
		}
		return lastThree;
	}

	public int[] calcLatestFirstTwo(List<Lottery> lotteries) {
		int[] firstTwo = new int[Commons.TWO];
		for (int i = 0; i < firstTwo.length; i++) {
			firstTwo[i] = -1;
		}
		int size = lotteries.size() - 1;
		int count = 0;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ft = lottery.getNumber() / Commons.THREE;
			if (firstTwo[ft] < 0) {
				firstTwo[ft] = size - i;
				if (++count == Commons.TWO) {
					return firstTwo;
				}
			}
		}
		return firstTwo;
	}

	public int[] calcLatestLastTwo(List<Lottery> lotteries) {
		int[] lastTwo = new int[Commons.TWO];
		for (int i = 0; i < lastTwo.length; i++) {
			lastTwo[i] = -1;
		}
		int size = lotteries.size() - 1;
		int count = 0;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int lt = lottery.getNumber() % Commons.TWO;
			if (lastTwo[lt] < 0) {
				lastTwo[lt] = size - i;
				if (++count == Commons.TWO) {
					return lastTwo;
				}
			}
		}
		return lastTwo;
	}

	public Map<Integer, Integer> calcLatestCombThree(List<Lottery> lotteries) {
		Map<Integer, Integer> absenceMap = new HashMap<>();
		int size = lotteries.size() - 1;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ct = lottery.getNumber() % Commons.THREE;
			Integer num = Utils.arrange3(ct);
			boolean put = absenceMap.putIfAbsent(num, size - i) == null;
			if (put && absenceMap.size() == Commons.COMB_THREE) {
				return absenceMap;
			}
		}
		return absenceMap;
	}

	public Map<Integer, Integer> calcLatestCombTwo(List<Lottery> lotteries) {
		Map<Integer, Integer> absenceMap = new HashMap<>();
		int size = lotteries.size() - 1;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ct = lottery.getNumber() % Commons.TWO;
			Integer num = Utils.getComTowMin(ct);
			boolean put = absenceMap.putIfAbsent(num, size - i) == null;
			if (put && absenceMap.size() == Commons.COMB_TWO) {
				return absenceMap;
			}
		}
		return absenceMap;
	}
}
