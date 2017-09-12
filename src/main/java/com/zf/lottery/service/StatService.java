package com.zf.lottery.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	private static final int WILL_VALUE = 8;

	private static final int THRESHOLD_THREE = (int) (WILL_VALUE / 0.001f);
	private static final int THRESHOLD_THREE_TWO = (int) (WILL_VALUE / 0.02f);
	private static final int THRESHOLD_THREE_THREE = (int) (WILL_VALUE / 0.03f);
	private static final int THRESHOLD_THREE_FOUR = (int) (WILL_VALUE / 0.04f);
	private static final int THRESHOLD_THREE_FIVE = (int) (WILL_VALUE / 0.05f);

	private static final int THRESHOLD_GROUP_SIX = (int) (WILL_VALUE / 0.006f);
	private static final int THRESHOLD_GROUP_SIX_TWO = (int) (WILL_VALUE / 0.012f);
	private static final int THRESHOLD_GROUP_SIX_THREE = (int) (WILL_VALUE / 0.018f);
	private static final int THRESHOLD_GROUP_SIX_FOUR = (int) (WILL_VALUE / 0.024f);
	private static final int THRESHOLD_GROUP_SIX_FIVE = (int) (WILL_VALUE / 0.03f);

	private static final int THRESHOLD_GROUP_THREE = (int) (WILL_VALUE / 0.003f);
	private static final int THRESHOLD_GROUP_THREE_TWO = (int) (WILL_VALUE / 0.006f);
	private static final int THRESHOLD_GROUP_THREE_THREE = (int) (WILL_VALUE / 0.009f);
	private static final int THRESHOLD_GROUP_THREE_FOUR = (int) (WILL_VALUE / 0.012f);
	private static final int THRESHOLD_GROUP_THREE_FIVE = (int) (WILL_VALUE / 0.015f);

	private static final int THRESHOLD_TWO = (int) (WILL_VALUE / 0.01f);
	private static final int THRESHOLD_TWO_TWO = (int) (WILL_VALUE / 0.02f);
	private static final int THRESHOLD_TWO_THREE = (int) (WILL_VALUE / 0.03f);
	private static final int THRESHOLD_TWO_FOUR = (int) (WILL_VALUE / 0.04f);
	private static final int THRESHOLD_TWO_FIVE = (int) (WILL_VALUE / 0.05f);

	private static final int THRESHOLD_COMB_TWO = (int) (WILL_VALUE / 0.02f);
	private static final int THRESHOLD_COMB_TWO_TWO = (int) (WILL_VALUE / 0.04f);
	private static final int THRESHOLD_COMB_TWO_THREE = (int) (WILL_VALUE / 0.06f);
	private static final int THRESHOLD_COMB_TWO_FOUR = (int) (WILL_VALUE / 0.08f);
	private static final int THRESHOLD_COMB_TWO_FIVE = (int) (WILL_VALUE / 0.1f);

	private LotteryManager manager = LotteryManager.instance();

	public void statAndPush() {
		List<MaxStat> maxStats = checkMax();
		List<GroupStat> groupStats = checkProbability();
		PushService.push(maxStats, groupStats);
	}

	private List<MaxStat> checkMax() {
		List<MaxStat> stats = new ArrayList<>();

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
			if (maxCombTwo - combTwo <= THRESHOLD_MAX) {
				stats.add(new MaxStat(StarType.CombTwo.ordinal(), number, combTwo, maxCombTwo));
			}
		}
		return stats;
	}

	private List<GroupStat> checkProbability() {
		List<GroupStat> stats = new ArrayList<>();
		List<GroupStat> firstTwoStats = checkTwoProbability(manager.getFirstTwo(), StarType.FirstTwo);
		stats.addAll(firstTwoStats);
		List<GroupStat> lastTwoStats = checkTwoProbability(manager.getLastTwo(), StarType.LastTwo);
		stats.addAll(lastTwoStats);
		List<GroupStat> firstThreeStats = checkThreeProbability(manager.getFirstThree(), StarType.FirstThree);
		stats.addAll(firstThreeStats);
		List<GroupStat> lastThreeStats = checkThreeProbability(manager.getLastThree(), StarType.LastThree);
		stats.addAll(lastThreeStats);
		List<GroupStat> combTwoStats = checkCombTwoProbability(manager.getCombTwo(), StarType.CombTwo);
		stats.addAll(combTwoStats);
		List<GroupStat> groupSixStats = checkGroupSixProbability(manager.getGroupSix(), StarType.GroupSix);
		stats.addAll(groupSixStats);
		List<GroupStat> groupThreeStats = checkGroupThreeProbability(manager.getGroupSix(), StarType.GroupThree);
		stats.addAll(groupThreeStats);
		return stats;
	}

	private List<GroupStat> checkTwoProbability(int[] absences, StarType type) {
		List<GroupStat> stats = new ArrayList<>();

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_TWO) {
				GroupStat stat = new GroupStat();
				stat.setType((type.ordinal()));
				stat.setNumber(i);
				stat.setAbsences(absences[i]);
				stats.add(stat);
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_TWO_TWO) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_TWO_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((type.ordinal()));
						stat.setNumber(i, j);
						stat.setAbsences(absences[i], absences[j]);
						stats.add(stat);
					}
				}
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_TWO_THREE) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_TWO_THREE) {
						for (int m = j + 1; m < absences.length; m++) {
							if (absences[m] >= THRESHOLD_TWO_THREE) {
								GroupStat stat = new GroupStat();
								stat.setType((type.ordinal()));
								stat.setNumber(i, j, m);
								stat.setAbsences(absences[i], absences[j], absences[m]);
								stats.add(stat);
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_TWO_FOUR) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_TWO_FOUR) {
						for (int m = j + 1; m < absences.length; m++) {
							if (absences[m] >= THRESHOLD_TWO_FOUR) {
								for (int n = j + 1; n < absences.length; n++) {
									if (absences[n] >= THRESHOLD_TWO_FOUR) {
										GroupStat stat = new GroupStat();
										stat.setType((type.ordinal()));
										stat.setNumber(i, j, m, n);
										stat.setAbsences(absences[i], absences[j], absences[m], absences[n]);
										stats.add(stat);
									}
								}
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_TWO_FIVE) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_TWO_FIVE) {
						for (int m = j + 1; m < absences.length; m++) {
							if (absences[m] >= THRESHOLD_TWO_FIVE) {
								for (int n = j + 1; n < absences.length; n++) {
									if (absences[n] >= THRESHOLD_TWO_FIVE) {
										for (int k = j + 1; k < absences.length; k++) {
											if (absences[k] >= THRESHOLD_TWO_FIVE) {
												GroupStat stat = new GroupStat();
												stat.setType((type.ordinal()));
												stat.setNumber(i, j, m, n, k);
												stat.setAbsences(absences[i], absences[j], absences[m], absences[n],
														absences[k]);
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
		return stats;
	}

	private void name(int[] absences, int threshold) {
		Map<Integer, Integer> twoMap = new HashMap<>();
		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= threshold) {
				twoMap.put(i, absences[i]);
			}
		}
		if (threshold <= 1) {
			return;
		}

	}

	/**
	 * 组合选择（从列表中选择n个组合）
	 * 
	 * @param dataList
	 *            待选列表
	 * @param n
	 *            选择个数
	 */
	public static void combinationSelect(int[] dataList, int n) {
		List<int[]> list = new ArrayList<>();
		combinationSelect(dataList, list, 0, new int[n], 0);
		for (int[] is : list) {
			System.out.println(Arrays.toString(is));
		}
		
	}

	/**
	 * 组合选择
	 * 
	 * @param dataList
	 *            待选列表
	 * @param dataIndex
	 *            待选开始索引
	 * @param resultList
	 *            前面（resultIndex-1）个的组合结果
	 * @param resultIndex
	 *            选择索引，从0开始
	 */
	private static void combinationSelect(int[] dataList, List<int[]> list, int dataIndex, int[] resultList,
			int resultIndex) {
		int resultLen = resultList.length;
		int resultCount = resultIndex + 1;
		if (resultCount > resultLen) { // 全部选择完时，输出组合结果
			list.add(resultList.clone());
			return;
		}

		// 递归选择下一个
		for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
			resultList[resultIndex] = dataList[i];
			combinationSelect(dataList, list, i + 1, resultList, resultIndex + 1);
		}
	}
	
	private List<GroupStat> checkCombTwoProbability(Map<Integer, Integer> absenceMap, StarType type) {
		List<GroupStat> stats = new ArrayList<>();

		Set<Entry<Integer, Integer>> entrySet = absenceMap.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			if (entry.getValue() >= THRESHOLD_COMB_TWO) {
				GroupStat stat = new GroupStat();
				stat.setType((type.ordinal()));
				stat.setNumber(entry.getKey());
				stat.setAbsences(entry.getValue());
				stats.add(stat);
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_COMB_TWO_TWO) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_COMB_TWO_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((type.ordinal()));
						stat.setNumber(entry1.getKey(), entry2.getKey());
						stat.setAbsences(entry1.getValue(), entry2.getValue());
						stats.add(stat);
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_COMB_TWO_THREE) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_COMB_TWO_THREE) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_COMB_TWO_THREE) {
								GroupStat stat = new GroupStat();
								stat.setType((type.ordinal()));
								stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey());
								stat.setAbsences(entry1.getValue(), entry2.getValue(), entry3.getValue());
								stats.add(stat);
							}
						}
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_COMB_TWO_FOUR) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_COMB_TWO_FOUR) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_COMB_TWO_FOUR) {
								for (Entry<Integer, Integer> entry4 : entrySet) {
									if (entry4.getValue() >= THRESHOLD_COMB_TWO_FOUR) {
										GroupStat stat = new GroupStat();
										stat.setType((type.ordinal()));
										stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey(),
												entry4.getKey());
										stat.setAbsences(entry1.getValue(), entry2.getValue(), entry3.getValue(),
												entry4.getValue());
										stats.add(stat);
									}
								}
							}
						}
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_COMB_TWO_FIVE) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_COMB_TWO_FIVE) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_COMB_TWO_FIVE) {
								for (Entry<Integer, Integer> entry4 : entrySet) {
									if (entry4.getValue() >= THRESHOLD_COMB_TWO_FIVE) {
										for (Entry<Integer, Integer> entry5 : entrySet) {
											if (entry5.getValue() >= THRESHOLD_COMB_TWO_FIVE) {
												GroupStat stat = new GroupStat();
												stat.setType((type.ordinal()));
												stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey(),
														entry4.getKey(), entry5.getKey());
												stat.setAbsences(entry1.getValue(), entry2.getValue(),
														entry3.getValue(), entry4.getValue(), entry5.getValue());
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

		return stats;
	}

	private List<GroupStat> checkThreeProbability(int[] absences, StarType type) {
		List<GroupStat> stats = new ArrayList<>();
		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_THREE) {
				GroupStat stat = new GroupStat();
				stat.setType((type.ordinal()));
				stat.setNumber(i);
				stat.setAbsences(absences[i]);
				stats.add(stat);
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_THREE_TWO) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_THREE_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((type.ordinal()));
						stat.setNumber(i, j);
						stat.setAbsences(absences[i], absences[j]);
						stats.add(stat);
					}
				}
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_THREE_THREE) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_THREE_THREE) {
						for (int m = j + 1; m < absences.length; m++) {
							if (absences[m] >= THRESHOLD_THREE_THREE) {
								GroupStat stat = new GroupStat();
								stat.setType((type.ordinal()));
								stat.setNumber(i, j, m);
								stat.setAbsences(absences[i], absences[j], absences[m]);
								stats.add(stat);
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_THREE_FOUR) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_THREE_FOUR) {
						for (int m = j + 1; m < absences.length; m++) {
							if (absences[m] >= THRESHOLD_THREE_FOUR) {
								for (int n = j + 1; n < absences.length; n++) {
									if (absences[n] >= THRESHOLD_THREE_FOUR) {
										GroupStat stat = new GroupStat();
										stat.setType((type.ordinal()));
										stat.setNumber(i, j, m, n);
										stat.setAbsences(absences[i], absences[j], absences[m], absences[n]);
										stats.add(stat);
									}
								}
							}
						}
					}
				}
			}
		}

		for (int i = 0; i < absences.length; i++) {
			if (absences[i] >= THRESHOLD_THREE_FIVE) {
				for (int j = i + 1; j < absences.length; j++) {
					if (absences[j] >= THRESHOLD_THREE_FIVE) {
						for (int m = j + 1; m < absences.length; m++) {
							if (absences[m] >= THRESHOLD_THREE_FIVE) {
								for (int n = j + 1; n < absences.length; n++) {
									if (absences[n] >= THRESHOLD_THREE_FIVE) {
										for (int k = j + 1; k < absences.length; k++) {
											if (absences[k] >= THRESHOLD_THREE_FIVE) {
												GroupStat stat = new GroupStat();
												stat.setType((type.ordinal()));
												stat.setNumber(i, j, m, n, k);
												stat.setAbsences(absences[i], absences[j], absences[m], absences[n],
														absences[k]);
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
		return stats;
	}

	private List<GroupStat> checkGroupSixProbability(Map<Integer, Integer> absenceMap, StarType type) {
		List<GroupStat> stats = new ArrayList<>();

		Set<Entry<Integer, Integer>> entrySet = absenceMap.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			if (entry.getValue() >= THRESHOLD_GROUP_SIX) {
				GroupStat stat = new GroupStat();
				stat.setType((type.ordinal()));
				stat.setNumber(entry.getKey());
				stat.setAbsences(entry.getValue());
				stats.add(stat);
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_SIX_TWO) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_SIX_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((type.ordinal()));
						stat.setNumber(entry1.getKey(), entry2.getKey());
						stat.setAbsences(entry1.getValue(), entry2.getValue());
						stats.add(stat);
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_SIX_THREE) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_SIX_THREE) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_GROUP_SIX_THREE) {
								GroupStat stat = new GroupStat();
								stat.setType((type.ordinal()));
								stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey());
								stat.setAbsences(entry1.getValue(), entry2.getValue(), entry3.getValue());
								stats.add(stat);
							}
						}
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_SIX_FOUR) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_SIX_FOUR) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_GROUP_SIX_FOUR) {
								for (Entry<Integer, Integer> entry4 : entrySet) {
									if (entry4.getValue() >= THRESHOLD_GROUP_SIX_FOUR) {
										GroupStat stat = new GroupStat();
										stat.setType((type.ordinal()));
										stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey(),
												entry4.getKey());
										stat.setAbsences(entry1.getValue(), entry2.getValue(), entry3.getValue(),
												entry4.getValue());
										stats.add(stat);
									}
								}
							}
						}
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_SIX_FIVE) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_SIX_FIVE) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_GROUP_SIX_FIVE) {
								for (Entry<Integer, Integer> entry4 : entrySet) {
									if (entry4.getValue() >= THRESHOLD_GROUP_SIX_FIVE) {
										for (Entry<Integer, Integer> entry5 : entrySet) {
											if (entry5.getValue() >= THRESHOLD_GROUP_SIX_FIVE) {
												GroupStat stat = new GroupStat();
												stat.setType((type.ordinal()));
												stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey(),
														entry4.getKey(), entry5.getKey());
												stat.setAbsences(entry1.getValue(), entry2.getValue(),
														entry3.getValue(), entry4.getValue(), entry5.getValue());
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

		return stats;
	}

	private List<GroupStat> checkGroupThreeProbability(Map<Integer, Integer> absenceMap, StarType type) {
		List<GroupStat> stats = new ArrayList<>();

		Set<Entry<Integer, Integer>> entrySet = absenceMap.entrySet();
		for (Entry<Integer, Integer> entry : entrySet) {
			if (entry.getValue() >= THRESHOLD_GROUP_THREE) {
				GroupStat stat = new GroupStat();
				stat.setType((type.ordinal()));
				stat.setNumber(entry.getKey());
				stat.setAbsences(entry.getValue());
				stats.add(stat);
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_THREE_TWO) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_THREE_TWO) {
						GroupStat stat = new GroupStat();
						stat.setType((type.ordinal()));
						stat.setNumber(entry1.getKey(), entry2.getKey());
						stat.setAbsences(entry1.getValue(), entry2.getValue());
						stats.add(stat);
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_THREE_THREE) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_THREE_THREE) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_GROUP_THREE_THREE) {
								GroupStat stat = new GroupStat();
								stat.setType((type.ordinal()));
								stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey());
								stat.setAbsences(entry1.getValue(), entry2.getValue(), entry3.getValue());
								stats.add(stat);
							}
						}
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_THREE_FOUR) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_THREE_FOUR) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_GROUP_THREE_FOUR) {
								for (Entry<Integer, Integer> entry4 : entrySet) {
									if (entry4.getValue() >= THRESHOLD_GROUP_THREE_FOUR) {
										GroupStat stat = new GroupStat();
										stat.setType((type.ordinal()));
										stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey(),
												entry4.getKey());
										stat.setAbsences(entry1.getValue(), entry2.getValue(), entry3.getValue(),
												entry4.getValue());
										stats.add(stat);
									}
								}
							}
						}
					}
				}
			}
		}

		for (Entry<Integer, Integer> entry1 : entrySet) {
			if (entry1.getValue() >= THRESHOLD_GROUP_THREE_FIVE) {
				for (Entry<Integer, Integer> entry2 : entrySet) {
					if (entry2.getValue() >= THRESHOLD_GROUP_THREE_FIVE) {
						for (Entry<Integer, Integer> entry3 : entrySet) {
							if (entry3.getValue() >= THRESHOLD_GROUP_THREE_FIVE) {
								for (Entry<Integer, Integer> entry4 : entrySet) {
									if (entry4.getValue() >= THRESHOLD_GROUP_THREE_FIVE) {
										for (Entry<Integer, Integer> entry5 : entrySet) {
											if (entry5.getValue() >= THRESHOLD_GROUP_THREE_FIVE) {
												GroupStat stat = new GroupStat();
												stat.setType((type.ordinal()));
												stat.setNumber(entry1.getKey(), entry2.getKey(), entry3.getKey(),
														entry4.getKey(), entry5.getKey());
												stat.setAbsences(entry1.getValue(), entry2.getValue(),
														entry3.getValue(), entry4.getValue(), entry5.getValue());
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

		return stats;
	}
}
