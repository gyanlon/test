package com.newhope.nlbp.core.job.support;

import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scheduler Manager class.
 * 
 * @author JackDou
 * @version 2017-4-17
 */
public class SchedulerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerManager.class);

	public static final String QUARTZ_THREAD_POOL_COUNT = "5";

	private static Scheduler scheduler;

	/**
	 * Private constructor
	 */
	private SchedulerManager() {
	}

	/**
	 * Get the scheduler instanse.
	 * 
	 * @return
	 */
	public static Scheduler getInstanse() {
		if (scheduler == null) {
			init();
		}
		return scheduler;
	}

	/**
	 * Initialization the scheduler object.
	 */
	public static void init() {
		try {
			Properties props = new Properties();
			props.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
			// set other properties ...such as
			props.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
			props.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
			props.setProperty("org.quartz.threadPool.threadCount", QUARTZ_THREAD_POOL_COUNT);
			SchedulerFactory schedulerFactory = new StdSchedulerFactory(props);
			scheduler = schedulerFactory.getScheduler();
		} catch (Exception e) {
			LOGGER.error("The Scheduler initialization error!", e);
		}
		LOGGER.info("The Scheduler initialization completed!");
	}

	/**
	 * destroy the scheduler object.
	 */
	public static void destroy() {
		try {
			if (scheduler != null) {
				if (!scheduler.isShutdown()) {
					scheduler.shutdown(true);
					scheduler = null;
				}
			}
		} catch (Exception e) {
			LOGGER.error("The Scheduler shutdown error!", e);
		}
		LOGGER.info("The Scheduler shutdown completed!");
	}
}
