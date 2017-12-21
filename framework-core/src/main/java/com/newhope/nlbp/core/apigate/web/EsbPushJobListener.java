package com.newhope.nlbp.core.apigate.web;

import java.util.concurrent.ExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.newhope.nlbp.core.apigate.suport.EsbPushDataJobService;

@WebListener
public class EsbPushJobListener implements ServletContextListener{

	@Autowired
	private EsbPushDataJobService esbPushJobService;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ExecutorService pool = esbPushJobService.getPool();
		if (pool != null && !pool.isShutdown()) {
			pool.shutdown();
		}
	}
}
