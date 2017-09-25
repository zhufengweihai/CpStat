package com.zf.lottery.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.lottery.data.GroupStat;
import com.zf.lottery.data.MaxStat;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class PushService {
	private final static String appKey = "df4351eebe0b1d174825853b";
	private final static String masterSecret = "99b9d5b3de7c8adb64367cef";
	private static Logger logger = LoggerFactory.getLogger(PushService.class);
	private static JPushClient jPushClient = new JPushClient(masterSecret, appKey);
	private static boolean lastIsEmpty = true;

	public static void push(List<MaxStat> maxStats, List<GroupStat> groupStats) {
		String content = "";
		if (maxStats.size() > 0 || groupStats.size() > 0) {
			content = toString(maxStats, groupStats);
			lastIsEmpty = false;
		} else if (lastIsEmpty) {
			return;
		} else {
			lastIsEmpty = true;
		}
		
		PushPayload pushPayload = buildPushObject(content);
		try {
			jPushClient.sendPush(pushPayload);
		} catch (APIConnectionException | APIRequestException e) {
			logger.error("Failed to push", e);
		}
	}

	public static void pushError() {
		PushPayload pushPayload = buildPushError();
		try {
			jPushClient.sendPush(pushPayload);
		} catch (APIConnectionException | APIRequestException e) {
			logger.error("Failed to push", e);
		}
	}

	private static String toString(List<MaxStat> maxStats, List<GroupStat> groupStats) {
		return maxStatToString(maxStats) + ":" + groupStatToString(groupStats);
	}

	private static String maxStatToString(List<MaxStat> maxStats) {
		if (maxStats == null || maxStats.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (MaxStat maxStat : maxStats) {
			sb.append(maxStat.getType()).append(',');
			sb.append(maxStat.getNumber()).append(',');
			sb.append(maxStat.getAbsence()).append(',');
			sb.append(maxStat.getMaxAbsence()).append(';');
		}
		return sb.substring(0, sb.length() - 1);
	}

	private static String groupStatToString(List<GroupStat> groupStats) {
		if (groupStats == null || groupStats.size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (GroupStat groupStat : groupStats) {
			sb.append(groupStat.getType()).append(',');
			sb.append(toString(groupStat.getNumber())).append(',');
			sb.append(toString(groupStat.getAbsences())).append(';');
		}
		return sb.substring(0, sb.length() - 1);
	}

	private static String toString(int[] a) {
		int iMax = a.length - 1;
		StringBuilder b = new StringBuilder();
		for (int i = 0;; i++) {
			b.append(a[i]);
			if (i == iMax)
				return b.toString();
			b.append("-");
		}
	}

	private static PushPayload buildPushObject(String content) {
		Map<String, String> extras = new HashMap<>(1);
		extras.put(MaxStat.KEY_MAX_STAT, content);
		return PushPayload.newBuilder().setPlatform(Platform.android())
				.setNotification(Notification.android("通知", "通知", extras)).setAudience(Audience.all()).build();
	}

	private static PushPayload buildPushError() {
		return PushPayload.newBuilder().setPlatform(Platform.android()).setNotification(Notification.alert("错误"))
				.setAudience(Audience.all()).build();
	}
}
