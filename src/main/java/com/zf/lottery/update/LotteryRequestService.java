package com.zf.lottery.update;

import java.util.List;

import com.zf.lottery.data.Lottery;

public interface LotteryRequestService {
	List<Lottery> requestLatest() throws Exception;
}
