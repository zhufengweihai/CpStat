package com.zf.lottery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zf.lottery.dao.LotteryManager;
import com.zf.lottery.data.MaxStat;
import com.zf.lottery.data.StarType;
import com.zf.lottery.push.PushService;

public class StatService {
	private static final int THRESHOLD_MAX = 100;
	private static final int THRESHOLD_THREE = 8000;
	private LotteryManager manager = LotteryManager.instance();

	public void checkMax() {
		List<MaxStat> stats = statMax();
		PushService.push(stats);
	}

	private List<MaxStat> statMax() {
		List<MaxStat> stats = new ArrayList<>();
		int[] firstThree = manager.getFirstThree();
		int[] maxFirstThree = manager.getMaxFirstThree();
		for (int i = 0; i < maxFirstThree.length; i++) {
			if (maxFirstThree[i] - firstThree[i] <= THRESHOLD_MAX && firstThree[i] >= THRESHOLD_THREE) {
				stats.add(new MaxStat(StarType.FirstThree.ordinal(), i, firstThree[i], maxFirstThree[i]));
			}
		}

		int[] lastThree = manager.getLastThree();
		int[] maxLastThree = manager.getMaxLastThree();
		for (int i = 0; i < maxLastThree.length; i++) {
			if (maxLastThree[i] - lastThree[i] <= THRESHOLD_MAX && lastThree[i] >= THRESHOLD_THREE) {
				stats.add(new MaxStat(StarType.LastThree.ordinal(), i, lastThree[i], maxLastThree[i]));
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

		Map<Integer, Integer> combThreeMap = manager.getCombThree();
		Set<Entry<Integer, Integer>> maxCombThreeEntries = manager.getMaxCombThree().entrySet();
		for (Entry<Integer, Integer> entry : maxCombThreeEntries) {
			Integer number = entry.getKey();
			Integer maxCombThree = entry.getValue();
			Integer combThree = combThreeMap.get(number);
			if (maxCombThree - combThree <= THRESHOLD_MAX) {
				stats.add(new MaxStat(StarType.CombThree.ordinal(), number, combThree, maxCombThree));
			}
		}

		Map<Integer, Integer> combTwoMap = manager.getCombTwo();
		Set<Entry<Integer, Integer>> maxCombTwoEntries = manager.getMaxCombTwo().entrySet();
		for (Entry<Integer, Integer> entry : maxCombTwoEntries) {
			Integer number = entry.getKey();
			Integer maxCombTwo = entry.getValue();
			Integer combTwo = combTwoMap.get(number);
			if (maxCombTwo - combTwo <= THRESHOLD_MAX) {
				stats.add(new MaxStat(StarType.CombTwo.ordinal(), number, combTwo, maxCombTwo));
			}
		}
		return stats;
	}
}
