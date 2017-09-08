package com.zf.lottery.update;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.zf.lottery.common.Commons;
import com.zf.lottery.dao.LotteryManager;
import com.zf.lottery.data.Lottery;

public class LotteryRequestServiceTest implements LotteryRequestService {
	private static final int BASE_TIME = 10000;

	@Override
	public List<Lottery> requestLatest() throws Exception {
		List<Lottery> lotteries = new ArrayList<Lottery>(15);
		Lottery lottery = new Lottery();
		lottery.setTerm(LotteryManager.instance().getMaxTerm() + 1);
		lottery.setTime((int) ((Calendar.getInstance().getTimeInMillis() - Commons.TIME_SART) / BASE_TIME));
		lottery.setNumber(12345);
		lotteries.add(lottery);
		return lotteries;
	}
}
