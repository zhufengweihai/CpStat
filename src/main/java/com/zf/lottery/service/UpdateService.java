package com.zf.lottery.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.lottery.dao.LotteryDao;
import com.zf.lottery.dao.LotteryManager;
import com.zf.lottery.data.Lottery;
import com.zf.lottery.push.PushService;
import com.zf.lottery.update.LotteryRequestService;
import com.zf.lottery.update.LotteryRequestServiceImpl;

public class UpdateService {
	private static Logger logger = LoggerFactory.getLogger(UpdateService.class);
	private LotteryManager manager = LotteryManager.instance();
	private LotteryDao lotteryDao = new LotteryDao();
	private StatService statService = new StatService();

	public void init() {
		try {
			lotteryDao.init();
			List<Lottery> lotteries = lotteryDao.readData();

			manager.setMaxTerm(lotteries.get(lotteries.size() - 1).getTerm());
			manager.setCount(lotteries.size());

			CalcService calcService = new CalcService();
			// manager.setMaxFirstThree(calcService.calcMaxFirstThree(lotteries));
			// manager.setMaxLastThree(calcService.calcMaxLastThree(lotteries));
			manager.setMaxLastOne(calcService.calcMaxLastOne(lotteries));
			manager.setMaxFirstTwo(calcService.calcMaxFirstTwo(lotteries));
			manager.setMaxLastTwo(calcService.calcMaxLastTwo(lotteries));
			// manager.setMaxCombThree(calcService.calcMaxCombThree(lotteries));
			manager.setMaxCombTwo(calcService.calcMaxCombTwo(lotteries));

			manager.setLastOne(calcService.calcLatestLastOne(lotteries));
			manager.setFirstThree(calcService.calcLatestFirstThree(lotteries));
			manager.setLastThree(calcService.calcLatestLastThree(lotteries));
			manager.setFirstTwo(calcService.calcLatestFirstTwo(lotteries));
			manager.setLastTwo(calcService.calcLatestLastTwo(lotteries));
			manager.setGroupSix(calcService.calcLatestGroupSix(lotteries));
			manager.setGroupThree(calcService.calcLatestGroupThree(lotteries));
			manager.setCombTwo(calcService.calcLatestCombTwo(lotteries));
		} catch (Exception e) {
			logger.error("UpdateService init failed", e);
		}
	}

	public void clear() {
		lotteryDao.clear();
	}

	public void update() {
		try {
			List<Lottery> latestData = requestData();
			if (manager.getRealCount() != manager.getCount() + latestData.size()) {
				PushService.pushError();
				return;
			}
			for (Lottery lottery : latestData) {
				manager.increaseCount();
				manager.setMaxTerm(lottery.getTerm());
				int number = lottery.getNumber();
				manager.updateLastOne(number);
				manager.updateFirstThree(number);
				manager.updateLastThree(number);
				manager.updateFirstTwo(number);
				manager.updateLastTwo(number);
				manager.updateGroupSix(number);
				manager.updateGroupThree(number);
				manager.updateCombTwo(number);
			}

			statService.statAndPush();
			lotteryDao.saveLatestData(latestData);
		} catch (Exception e) {
			logger.error("update failed", e);
		}
	}

	private List<Lottery> requestData() throws Exception {
		LotteryRequestService requestService = new LotteryRequestServiceImpl();
		List<Lottery> latestData = requestService.requestLatest();
		int maxTerm = manager.getMaxTerm();
		for (Iterator<Lottery> it = latestData.iterator(); it.hasNext();) {
			Lottery lottery = (Lottery) it.next();
			if (lottery.getTerm() <= maxTerm) {
				it.remove();
			}
		}
		return latestData;
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		UpdateService updateService = new UpdateService();
		updateService.init();
		LotteryManager manager = LotteryManager.instance();
		System.out.println(Arrays.toString(manager.getFirstThree()));
		System.out.println(Arrays.toString(manager.getLastThree()));
		System.out.println(Arrays.toString(manager.getFirstTwo()));
		System.out.println(Arrays.toString(manager.getLastTwo()));
		System.out.println(manager.getGroupSix());
		System.out.println(manager.getCombTwo());
	}
}
