package com.zf.lottery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zf.lottery.dao.LotteryManager;
import com.zf.lottery.data.GroupStat;
import com.zf.lottery.data.MaxStat;
import com.zf.lottery.data.StarType;
import com.zf.lottery.push.PushService;

public class StatService {
	private static final int THRESHOLD_MAX = 100;
	private static final int THRESHOLD_MAX_THREE = 8000;
	private static final int THRESHOLD_GROUP_TWO = 420;
	private static final int THRESHOLD_GROUP_THREE = 289;
	private static final int THRESHOLD_GROUP_FOUR = 225;
	private static final int THRESHOLD_GROUP_FIVE = 193;
	
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
			if (maxFirstThree[i] - firstThree[i] <= THRESHOLD_MAX && firstThree[i] >= THRESHOLD_MAX_THREE) {
				stats.add(new MaxStat(StarType.FirstThree.ordinal(), i, firstThree[i], maxFirstThree[i]));
			}
		}

		int[] lastThree = manager.getLastThree();
		int[] maxLastThree = manager.getMaxLastThree();
		for (int i = 0; i < maxLastThree.length; i++) {
			if (maxLastThree[i] - lastThree[i] <= THRESHOLD_MAX && lastThree[i] >= THRESHOLD_MAX_THREE) {
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

	public void name() {
		List<GroupStat> stats = new ArrayList<>();
		int[] firstTwo = manager.getFirstTwo();
		for (int i = 0; i < firstTwo.length; i++) {
			if (firstTwo[i] >= THRESHOLD_GROUP_TWO) {
				for (int j = i + 1; j < firstTwo.length; j++) {
					if (firstTwo[j] >= THRESHOLD_GROUP_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((StarType.FirstTwo.ordinal()));
						stat.setNumber(i, j);
						stat.setAbsences(firstTwo[i], firstTwo[j]);
						stats.add(stat);
					}
				}
			}
		}

		for (int i = 0; i < firstTwo.length; i++) {
			if (firstTwo[i] >= THRESHOLD_GROUP_THREE) {
				for (int j = i + 1; j < firstTwo.length; j++) {
					if (firstTwo[j] >= THRESHOLD_GROUP_THREE) {
						for (int m = j + 1; m < firstTwo.length; m++) {
							if (firstTwo[m] >= THRESHOLD_GROUP_THREE) {
								GroupStat stat = new GroupStat();
								stat.setType((StarType.FirstTwo.ordinal()));
								stat.setNumber(i, j, m);
								stat.setAbsences(firstTwo[i], firstTwo[j], firstTwo[m]);
								stats.add(stat);
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < firstTwo.length; i++) {
			if (firstTwo[i] >= THRESHOLD_GROUP_FOUR) {
				for (int j = i + 1; j < firstTwo.length; j++) {
					if (firstTwo[j] >= THRESHOLD_GROUP_FOUR) {
						for (int m = j + 1; m < firstTwo.length; m++) {
							if (firstTwo[m] >= THRESHOLD_GROUP_FOUR) {
								for (int n = j + 1; n < firstTwo.length; n++) {
									if (firstTwo[n] >= THRESHOLD_GROUP_FOUR) {
										GroupStat stat = new GroupStat();
										stat.setType((StarType.FirstTwo.ordinal()));
										stat.setNumber(i, j, m, n);
										stat.setAbsences(firstTwo[i], firstTwo[j], firstTwo[m], firstTwo[n]);
										stats.add(stat);
									}
								}
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < firstTwo.length; i++) {
			if (firstTwo[i] >= THRESHOLD_GROUP_FIVE) {
				for (int j = i + 1; j < firstTwo.length; j++) {
					if (firstTwo[j] >= THRESHOLD_GROUP_FIVE) {
						for (int m = j + 1; m < firstTwo.length; m++) {
							if (firstTwo[m] >= THRESHOLD_GROUP_FIVE) {
								for (int n = j + 1; n < firstTwo.length; n++) {
									if (firstTwo[n] >= THRESHOLD_GROUP_FIVE) {
										for (int k = j + 1; k < firstTwo.length; k++) {
											if (firstTwo[k] >= THRESHOLD_GROUP_FIVE) {
												GroupStat stat = new GroupStat();
												stat.setType((StarType.FirstTwo.ordinal()));
												stat.setNumber(i, j, m, n, k);
												stat.setAbsences(firstTwo[i], firstTwo[j], firstTwo[m], firstTwo[n],
														firstTwo[k]);
												stats.add(stat);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		int[] lastTwo = manager.getLastTwo();
		for (int i = 0; i < lastTwo.length; i++) {
			if (lastTwo[i] >= THRESHOLD_GROUP_TWO) {
				for (int j = i + 1; j < lastTwo.length; j++) {
					if (lastTwo[j] >= THRESHOLD_GROUP_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((StarType.LastTwo.ordinal()));
						stat.setNumber(i, j);
						stat.setAbsences(lastTwo[i], lastTwo[j]);

					}
				}
			}

		}
	}
}
