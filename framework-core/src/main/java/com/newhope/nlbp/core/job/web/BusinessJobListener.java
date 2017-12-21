package com.newhope.nlbp.core.job.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.newhope.nlbp.core.job.support.JobMonitor;

@WebListener
public class BusinessJobListener implements ServletContextListener{

	@Autowired
	private JobMonitor monitor;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		if (monitor != null && !monitor.isActive()) {
			monitor.start();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if (monitor != null && monitor.isActive()) {
			monitor.stop();
		}
	}
}
