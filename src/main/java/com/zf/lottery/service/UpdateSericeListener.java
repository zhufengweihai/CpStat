package com.zf.lottery.service;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class UpdateSericeListener implements ServletContextListener {
	private static final int DAY_HOUR_START = 10;
	private static final int DAY_HOUR_END = 22;
	private static final int NIGHT_HOUR_END = 2;
	private static final int INTERVAL_DAY = 10;
	private static final int INTERVAL_NIGHT = 5;
	private static final int POOL_COUNT = 1;
	private ScheduledExecutorService service;
	private UpdateService updateService = new UpdateService();

	public UpdateSericeListener() {
		service = Executors.newScheduledThreadPool(POOL_COUNT);
	}

	public void contextDestroyed(ServletContextEvent e) {
		if (service != null && !service.isShutdown()) {
			service.shutdown();
		}
		updateService.clear();
	}

	public void contextInitialized(ServletContextEvent e) {
		updateService.init();

		Calendar now = Calendar.getInstance();
		int minute = now.get(Calendar.MINUTE);
		int second = now.get(Calendar.SECOND);
		int time = (INTERVAL_NIGHT - minute % INTERVAL_NIGHT + 2) * 60 - second;

		service.scheduleAtFixedRate(createJob(), 0, INTERVAL_NIGHT * 60, TimeUnit.SECONDS);
	}

	private Runnable createJob() {
		Runnable command = new Runnable() {

			@Override
			public void run() {
				if (isTimeToUpdate()) {
					updateService.update();
				}
			}
		};
		return command;
	}

	private boolean isTimeToUpdate() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		if (hour >= DAY_HOUR_START && hour < DAY_HOUR_END) {
			if (minute % INTERVAL_DAY < INTERVAL_NIGHT) {
				return true;
			}
		} else if (hour >= DAY_HOUR_END || hour < NIGHT_HOUR_END) {
			return true;
		}
		return true;
	}
}