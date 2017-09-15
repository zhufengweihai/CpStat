package com.zf.lottery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;

import com.zf.lottery.common.Utils;
import com.zf.lottery.dao.LotteryManager;
import com.zf.lottery.data.GroupStat;
import com.zf.lottery.data.MaxStat;
import com.zf.lottery.data.StarType;
import com.zf.lottery.push.PushService;

public class StatService {
	private static final int THRESHOLD_MAX = 100;
	private static final int THRESHOLD_MAX_COMB = 120;
	private static final int THRESHOLD_MAX_ONE = 20;
	private static final int WILL_VALUE = 8;
	private static final int WILL_VALUE_THREE = 9;

	private static final int THRESHOLD_ONE = Math.round(WILL_VALUE / 0.1f);
	
	private static final int THRESHOLD_FIRST_THREE = Math.round(WILL_VALUE / 0.00199f);

	private static final int THRESHOLD_THREE = Math.round(WILL_VALUE / 0.001f);
	private static final int THRESHOLD_THREE_TWO = Math.round(WILL_VALUE / 0.002f);
	private static final int THRESHOLD_THREE_THREE = Math.round(WILL_VALUE / 0.003f);
	private static final int THRESHOLD_THREE_FOUR = Math.round(WILL_VALUE / 0.004f);
	private static final int THRESHOLD_THREE_FIVE = Math.round(WILL_VALUE / 0.005f);

	private static final int THRESHOLD_GROUP_SIX = Math.round(WILL_VALUE / 0.006f);
	private static final int THRESHOLD_GROUP_SIX_TWO = Math.round(WILL_VALUE / 0.012f);
	private static final int THRESHOLD_GROUP_SIX_THREE = Math.round(WILL_VALUE / 0.018f);
	private static final int THRESHOLD_GROUP_SIX_FOUR = Math.round(WILL_VALUE / 0.024f);
	private static final int THRESHOLD_GROUP_SIX_FIVE = Math.round(WILL_VALUE / 0.03f);

	private static final int THRESHOLD_GROUP_THREE = Math.round(WILL_VALUE / 0.003f);
	private static final int THRESHOLD_GROUP_THREE_TWO = Math.round(WILL_VALUE / 0.006f);
	private static final int THRESHOLD_GROUP_THREE_THREE = Math.round(WILL_VALUE / 0.009f);
	private static final int THRESHOLD_GROUP_THREE_FOUR = Math.round(WILL_VALUE / 0.012f);
	private static final int THRESHOLD_GROUP_THREE_FIVE = Math.round(WILL_VALUE / 0.015f);

	private static final int THRESHOLD_FIRST_TWO = Math.round(WILL_VALUE / 0.0199f);

	private static final int THRESHOLD_TWO = Math.round(WILL_VALUE / 0.01f);
	private static final int THRESHOLD_TWO_TWO = Math.round(WILL_VALUE / 0.02f);
	private static final int THRESHOLD_TWO_THREE = Math.round(WILL_VALUE / 0.03f);
	private static final int THRESHOLD_TWO_FOUR = Math.round(WILL_VALUE / 0.04f);
	private static final int THRESHOLD_TWO_FIVE = Math.round(WILL_VALUE / 0.05f);

	private static final int THRESHOLD_COMB_TWO = Math.round(WILL_VALUE / 0.02f);
	private static final int THRESHOLD_COMB_TWO_TWO = Math.round(WILL_VALUE / 0.04f);
	private static final int THRESHOLD_COMB_TWO_THREE = Math.round(WILL_VALUE / 0.06f);
	private static final int THRESHOLD_COMB_TWO_FOUR = Math.round(WILL_VALUE / 0.08f);
	private static final int THRESHOLD_COMB_TWO_FIVE = Math.round(WILL_VALUE / 0.1f);

	private LotteryManager manager = LotteryManager.instance();

	public void statAndPush() {
		List<MaxStat> maxStats = checkMax();
		List<GroupStat> groupStats = checkProbability();
		PushService.push(maxStats, groupStats);
	}

	private List<MaxStat> checkMax() {
		List<MaxStat> stats = new ArrayList<>();

		int[] lastOne = manager.getLastOne();
		int[] maxLastOne = manager.getMaxLastOne();
		for (int i = 0; i < maxLastOne.length; i++) {
			if (maxLastOne[i] - lastOne[i] <= THRESHOLD_MAX_ONE) {
				stats.add(new MaxStat(StarType.LastOne.ordinal(), i, lastOne[i], maxLastOne[i]));
			}
		}

		int[] firstTwo = manager.getFirstTwo();
		int[] maxFirstTwo = manager.getMaxFirstTwo();
		for (int i = 0; i < maxFirstTwo.length; i++) {
			if (maxFirstTwo[i] - firstTwo[i] <= THRESHOLD_MAX) {
				stats.add(new MaxStat(StarType.FirstTwo.ordinal(), i, firstTwo[i], maxFirstTwo[i]));
			}
		}

		int[] lastTwo = manager.getLastTwo();
		int[] maxLastTwo = manager.getMaxLastTwo();
		for (int i = 0; i < maxLastTwo.length; i++) {
			if (maxLastTwo[i] - lastTwo[i] <= THRESHOLD_MAX) {
				stats.add(new MaxStat(StarType.LastTwo.ordinal(), i, lastTwo[i], maxLastTwo[i]));
			}
		}

		Map<Integer, Integer> combTwoMap = manager.getCombTwo();
		Set<Entry<Integer, Integer>> maxCombTwoEntries = manager.getMaxCombTwo().entrySet();
		for (Entry<Integer, Integer> entry : maxCombTwoEntries) {
			Integer number = entry.getKey();
			Integer maxCombTwo = entry.getValue();
			Integer combTwo = combTwoMap.get(number);
			if (maxCombTwo - combTwo <= THRESHOLD_MAX_COMB) {
				stats.add(new MaxStat(StarType.CombTwo.ordinal(), number, combTwo, maxCombTwo));
			}
		}
		return stats;
	}

	private List<GroupStat> checkProbability() {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> lastOneStats = checkOneProbability(manager.getLastOne(), StarType.FirstTwo);
		stats.addAll(lastOneStats);
		List<GroupStat> firstTwoStats = checkFirstTwoProbability(manager.getFirstTwo(), StarType.FirstTwo);
		stats.addAll(firstTwoStats);
		List<GroupStat> lastTwoStats = checkTwoProbability(manager.getLastTwo(), StarType.LastTwo);
		stats.addAll(lastTwoStats);
		List<GroupStat> firstThreeStats = checkFirstThreeProbability(manager.getFirstThree(), StarType.FirstThree);
		stats.addAll(firstThreeStats);
		List<GroupStat> lastThreeStats = checkThreeProbability(manager.getLastThree(), StarType.LastThree);
		stats.addAll(lastThreeStats);
		List<GroupStat> combTwoStats = checkCombTwoProbability(manager.getCombTwo(), StarType.CombTwo);
		stats.addAll(combTwoStats);
		List<GroupStat> groupSixStats = checkGroupSixProbability(manager.getGroupSix(), StarType.GroupSix);
		stats.addAll(groupSixStats);
		List<GroupStat> groupThreeStats = checkGroupThreeProbability(manager.getGroupThree(), StarType.GroupThree);
		stats.addAll(groupThreeStats);
		return stats;
	}

	private List<GroupStat> checkOneProbability(int[] absences, StarType type) {
		return calcGroupStats(absences, THRESHOLD_ONE, 1, type.ordinal());
	}

	private List<GroupStat> checkFirstTwoProbability(int[] absences, StarType type) {
		return calcGroupStats(absences, THRESHOLD_FIRST_TWO, 1, type.ordinal());
	}

	private List<GroupStat> checkTwoProbability(int[] absences, StarType type) {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> oneStats = calcGroupStats(absences, THRESHOLD_TWO, 1, type.ordinal());
		stats.addAll(oneStats);
		// List<GroupStat> twoStats = calcGroupStats(absences,
		// THRESHOLD_TWO_TWO, 2, type.ordinal());
		// stats.addAll(twoStats);
		// List<GroupStat> threeStats = calcGroupStats(absences,
		// THRESHOLD_TWO_THREE, 3, type.ordinal());
		// stats.addAll(threeStats);
		// List<GroupStat> fourStats = calcGroupStats(absences,
		// THRESHOLD_TWO_FOUR, 4, type.ordinal());
		// stats.addAll(fourStats);
		// List<GroupStat> fiveStats = calcGroupStats(absences,
		// THRESHOLD_TWO_FIVE, 5, type.ordinal());
		// stats.addAll(fiveStats);
		return stats;
	}

	private List<GroupStat> checkCombTwoProbability(Map<Integer, Integer> absenceMap, StarType type) {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> oneStats = calcGroupStats(absenceMap, THRESHOLD_COMB_TWO, 1, type.ordinal());
		stats.addAll(oneStats);
		// List<GroupStat> twoStats = calcGroupStats(absenceMap,
		// THRESHOLD_COMB_TWO_TWO, 2, type.ordinal());
		// stats.addAll(twoStats);
		// List<GroupStat> threeStats = calcGroupStats(absenceMap,
		// THRESHOLD_COMB_TWO_THREE, 3, type.ordinal());
		// stats.addAll(threeStats);
		// List<GroupStat> fourStats = calcGroupStats(absenceMap,
		// THRESHOLD_COMB_TWO_FOUR, 4, type.ordinal());
		// stats.addAll(fourStats);
		// List<GroupStat> fiveStats = calcGroupStats(absenceMap,
		// THRESHOLD_COMB_TWO_FIVE, 5, type.ordinal());
		// stats.addAll(fiveStats);
		return stats;
	}

	private List<GroupStat> checkFirstThreeProbability(int[] absences, StarType type) {
		return calcGroupStats(absences, THRESHOLD_FIRST_THREE, 1, type.ordinal());
	}

	private List<GroupStat> checkThreeProbability(int[] absences, StarType type) {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> oneStats = calcGroupStats(absences, THRESHOLD_THREE, 1, type.ordinal());
		stats.addAll(oneStats);
		// List<GroupStat> twoStats = calcGroupStats(absences,
		// THRESHOLD_THREE_TWO, 2, type.ordinal());
		// stats.addAll(twoStats);
		// List<GroupStat> threeStats = calcGroupStats(absences,
		// THRESHOLD_THREE_THREE, 3, type.ordinal());
		// stats.addAll(threeStats);
		// List<GroupStat> fourStats = calcGroupStats(absences,
		// THRESHOLD_THREE_FOUR, 4, type.ordinal());
		// stats.addAll(fourStats);
		// List<GroupStat> fiveStats = calcGroupStats(absences,
		// THRESHOLD_THREE_FIVE, 5, type.ordinal());
		// stats.addAll(fiveStats);
		return stats;
	}

	private List<GroupStat> checkGroupSixProbability(Map<Integer, Integer> absenceMap, StarType type) {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> oneStats = calcGroupStats(absenceMap, THRESHOLD_GROUP_SIX, 1, type.ordinal());
		stats.addAll(oneStats);
		// List<GroupStat> twoStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_SIX_TWO, 2, type.ordinal());
		// stats.addAll(twoStats);
		// List<GroupStat> threeStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_SIX_THREE, 3, type.ordinal());
		// stats.addAll(threeStats);
		// List<GroupStat> fourStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_SIX_FOUR, 4, type.ordinal());
		// stats.addAll(fourStats);
		// List<GroupStat> fiveStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_SIX_FIVE, 5, type.ordinal());
		// stats.addAll(fiveStats);
		return stats;
	}

	private List<GroupStat> checkGroupThreeProbability(Map<Integer, Integer> absenceMap, StarType type) {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> oneStats = calcGroupStats(absenceMap, THRESHOLD_GROUP_THREE, 1, type.ordinal());
		stats.addAll(oneStats);
		// List<GroupStat> twoStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_THREE_TWO, 2, type.ordinal());
		// stats.addAll(twoStats);
		// List<GroupStat> threeStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_THREE_THREE, 3, type.ordinal());
		// stats.addAll(threeStats);
		// List<GroupStat> fourStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_THREE_FOUR, 4, type.ordinal());
		// stats.addAll(fourStats);
		// List<GroupStat> fiveStats = calcGroupStats(absenceMap,
		// THRESHOLD_GROUP_THREE_FIVE, 5, type.ordinal());
		// stats.addAll(fiveStats);
		return stats;
	}

	private List<GroupStat> calcGroupStats(int[] absences, int threshold, int count, int type) {
		List<GroupStat> stats = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= threshold) {
				list.add(i);
			}
		}
		if (list.size() < count) {
			return stats;
		}

		List<int[]> combList = Utils.combination(list, count);
		for (int[] comb : combList) {
			GroupStat stat = new GroupStat();
			stat.setType((type));
			int[] ab = new int[comb.length];
			for (int k = 0; k < ab.length; k++) {
				ab[k] = absences[comb[k]];
			}
			stat.setNumber(comb);
			stat.setAbsences(ab);
			stats.add(stat);
		}
		return stats;
	}

	private List<GroupStat> calcGroupStats(Map<Integer, Integer> absenceMap, int threshold, int count, int type) {
		List<GroupStat> stats = new ArrayList<>();
		List<Integer> list = new ArrayList<>();
		Set<Entry<Integer, Integer>> entrySet = absenceMap.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			if (entry.getValue() >= threshold) {
				list.add(entry.getKey());
			}
		}
		if (list.size() < count) {
			return stats;
		}

		List<int[]> combList = Utils.combination(list, count);
		for (int[] comb : combList) {
			GroupStat stat = new GroupStat();
			stat.setType((type));
			int[] ab = new int[comb.length];
			for (int k = 0; k < ab.length; k++) {
				ab[k] = absenceMap.get(comb[k]);
			}
			stat.setNumber(comb);
			stat.setAbsences(ab);
			stats.add(stat);
		}
		return stats;
	}

	public static void main(String[] args) {
		Map<Integer, Integer> absencesMap = new HashMap<>();
		for (int i = 0; i < 10; i++) {
			for (int k = 0; k < 10; k++) {
				if (k != i) {
					int ab = RandomUtils.nextInt(0, THRESHOLD_GROUP_THREE_TWO + 50);
					int n = i * 100 + i * 10 + k;
					absencesMap.put(n, ab);
					if (ab >= THRESHOLD_GROUP_THREE_TWO) {
						System.out.println(n + "," + ab);
					}
				}
			}
		}

		StatService service = new StatService();
		List<GroupStat> twoStats = service.calcGroupStats(absencesMap, THRESHOLD_GROUP_THREE_TWO, 2, 5);
		System.out.println(twoStats);
		System.out.println(twoStats.size());
	}
}
