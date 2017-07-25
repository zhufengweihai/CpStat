package com.zf.lottery.common;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

public class Utils {
	private static String URL = "http://www.caipiaow.com";

	public static Calendar getNetDatetime() {
		try {
			URL url = new URL(URL);
			URLConnection uc = url.openConnection();
			uc.connect();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(uc.getDate());
			return calendar;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getComTowMin(int num) {
		int num1 = (num % 10) * 10 + num / 10;
		return num > num1 ? num1 : num;
	}
	
	public static Integer arrange3(int num) {
		int[] nums = new int[3];
		nums[0] = num / 100;
		nums[1] = (num / 10) % 10;
		nums[2] = num % 10;
		int st = 0;
		int len = nums.length;
		List<Integer> result = new ArrayList<Integer>(6);
		arrange3(nums, st, len, result);
		return Collections.min(result);
	}

	public static void arrange3(int[] nums, int st, int len, List<Integer> result) {
		if (st == len - 1) {
			result.add(nums[0] * 100 + nums[1] * 10 + nums[2]);
		} else {
			for (int i = st; i < len; i++) {
				ArrayUtils.swap(nums, st, i);
				arrange3(nums, st + 1, len, result);
				ArrayUtils.swap(nums, st, i);
			}
		}
	}
}
