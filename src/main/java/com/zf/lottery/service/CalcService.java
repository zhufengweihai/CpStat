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
	public int[] calcMaxLastOne(List<Lottery> lotteries) {
		ArrayList<Integer>[] absences = new ArrayList[Commons.ONE];
		for (int i = 0; i < Commons.ONE; i++) {
			absences[i] = new ArrayList<Integer>();
		}
		int[] lasts = new int[Commons.ONE];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() % Commons.ONE;
			absences[num].add(i - lasts[num]);
			lasts[num] = i;
		}

		int[] maxLastOne = new int[Commons.ONE];
		for (int i = 0; i < absences.length; i++) {
			maxLastOne[i] = Collections.max(absences[i]);
		}

		return maxLastOne;
	}
	
	public Map<Integer, Integer> calcMaxCombTwo(List<Lottery> lotteries) {
		Map<Integer, ArrayList<Integer>> absencesMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int k = i; k < 10; k++) {
				if (k != i) {
					absencesMap.put(i * 10 + k, new ArrayList<>());
				}
			}
		}
		int[] lasts = new int[Commons.TWO];
		int size = lotteries.size();
		for (int i = 0; i < size; i++) {
			Lottery lottery = lotteries.get(i);
			int num = lottery.getNumber() % Commons.TWO;
			int min = Utils.getCombTwoMin(num);
			ArrayList<Integer> arrayList = absencesMap.get(min);
			if (arrayList != null) {
				int absence = i - lasts[min];
				arrayList.add(absence);
				lasts[min] = i;
			}
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
			int num1 = lottery.getNumber() / Commons.THREE;
			absences[num1].add(i - lasts[num1]);
			lasts[num1] = i;
			int num2 = lottery.getNumber() % Commons.TWO;
			absences[num2].add(i - lasts[num2]);
			lasts[num2] = i;
		}

		int[] maxFirstTwo = new int[Commons.TWO];
		for (int i = 0; i < Commons.TWO; i++) {
			maxFirstTwo[i] = Collections.max(absences[i]);
		}
		return maxFirstTwo;
	}

	public int[] calcLatestLastOne(List<Lottery> lotteries) {
		int[] lastOne = new int[Commons.ONE];
		for (int i = 0; i < lastOne.length; i++) {
			lastOne[i] = -1;
		}
		int size = lotteries.size() - 1;
		int count = 0;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int lt = lottery.getNumber() % Commons.ONE;
			if (lastOne[lt] < 0) {
				lastOne[lt] = size - i;
				if (++count == Commons.ONE) {
					return lastOne;
				}
			}
		}
		return lastOne;
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
			int ft1 = lottery.getNumber() / Commons.TWO;
			if (firstThree[ft1] < 0) {
				firstThree[ft1] = size - i;
				if (++count == Commons.THREE) {
					return firstThree;
				}
			}
			int ft2 = lottery.getNumber() % Commons.THREE;
			if (firstThree[ft2] < 0) {
				firstThree[ft2] = size - i;
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
			int ft1 = lottery.getNumber() / Commons.THREE;
			if (firstTwo[ft1] < 0) {
				firstTwo[ft1] = size - i;
				if (++count == Commons.TWO) {
					return firstTwo;
				}
			}
			int ft2 = lottery.getNumber() % Commons.TWO;
			if (firstTwo[ft2] < 0) {
				firstTwo[ft2] = size - i;
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

	public Map<Integer, Integer> calcLatestGroupSix(List<Lottery> lotteries) {
		Map<Integer, Integer> absenceMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++) {
				for (int k = j + 1; k < 10; k++) {
					absenceMap.put(i * 100 + j * 10 + k, -1);
				}
			}
		}
		int count = 0;
		int size = lotteries.size() - 1;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ct = lottery.getNumber() % Commons.THREE;
			int num = Utils.getCombThreeMin(ct);
			Integer absence = absenceMap.get(num);
			if (absence != null && absence < 0) {
				absenceMap.put(num, size - i);
				count++;
				if (count == Commons.GROUP_SIX) {
					return absenceMap;
				}
			}
		}
		return absenceMap;
	}

	public Map<Integer, Integer> calcLatestGroupThree(List<Lottery> lotteries) {
		Map<Integer, Integer> absenceMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int k = 0; k < 10; k++) {
				if (k != i) {
					absenceMap.put(i * 100 + i * 10 + k, -1);
				}
			}
		}

		int count = 0;
		int size = lotteries.size() - 1;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ct = lottery.getNumber() % Commons.THREE;
			int num = Utils.getCombThreeMin(ct);
			Integer absence = absenceMap.get(num);
			if (absence != null && absence < 0) {
				absenceMap.put(num, size - i);
				count++;
				if (count == Commons.GROUP_THREE) {
					return absenceMap;
				}
			}
		}
		return absenceMap;
	}

	public Map<Integer, Integer> calcLatestCombTwo(List<Lottery> lotteries) {
		Map<Integer, Integer> absenceMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int k = i; k < 10; k++) {
				if (k != i) {
					absenceMap.put(i * 10 + k, -1);
				}
			}
		}
		int count = 0;
		int size = lotteries.size() - 1;
		for (int i = size; i >= 0; i--) {
			Lottery lottery = lotteries.get(i);
			int ct = lottery.getNumber() % Commons.TWO;
			Integer num = Utils.getCombTwoMin(ct);
			Integer absence = absenceMap.get(num);
			if (absence != null && absence < 0) {
				absenceMap.put(num, size - i);
				count++;
				if (count == Commons.COMB_TWO) {
					return absenceMap;
				}
			}
		}
		return absenceMap;
	}
}
