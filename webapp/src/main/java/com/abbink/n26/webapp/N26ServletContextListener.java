package com.abbink.n26.webapp;

import javax.servlet.ServletContextEvent;

import com.abbink.n26.webapp.di.N26Module;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class N26ServletContextListener extends GuiceServletContextListener {
	
	private Injector injector;
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
		
		JmxReporter reporter = JmxReporter.forRegistry(injector.getInstance(MetricRegistry.class)).build();
		reporter.start();
	}
	
	@Override
	protected Injector getInjector() {
		injector = Guice.createInjector(new N26Module());
		return injector;
	}
}
