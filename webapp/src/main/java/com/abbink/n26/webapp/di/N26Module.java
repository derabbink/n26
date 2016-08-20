package com.abbink.n26.webapp.di;

import java.util.HashMap;
import java.util.Map;

import com.abbink.n26.api.di.N26ApiModule;
import com.abbink.n26.common.error.jersey.di.ExceptionMappersModule;
import com.abbink.n26.common.jersey.OverrideInputTypeResourceFilterFactory;
import com.abbink.n26.common.jersey.di.JerseyModule;
import com.abbink.n26.common.metrics.di.MetricsModule;
import com.abbink.n26.common.metrics.response.HttpStatusCodeMetricResourceFilterFactory;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * The general structure of how Servlets, Guice, Jersey and Metrics are used, was inspired
 * by the following blog post series:
 * http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
 */
public class N26Module extends ServletModule {
	@Override
	protected void configureServlets() {
		install(new ConfigModule());
		
		install(new MetricsModule());
//		install(new DataModule());
		install(new JerseyModule());
		
		// hook Jersey into Guice Servlet
		bind(GuiceContainer.class);
		// bind everything that can produce HTTP responses
		install(new ExceptionMappersModule());
		install(new N26ApiModule());
		// if the app had a web UI component, you'd bind it here
		
		
		Map<String, String> guiceContainerConfig = new HashMap<String, String>();
		guiceContainerConfig.put(
			ResourceConfig.PROPERTY_RESOURCE_FILTER_FACTORIES,
			HttpStatusCodeMetricResourceFilterFactory.class.getCanonicalName() +","+
			OverrideInputTypeResourceFilterFactory.class.getCanonicalName()
		);
		// If you do this, you can skip binding resource classes using Guice, but I prefer
		// explicit bindings since that will fail sooner if you forget anything:
		/*
		guiceContainerConfig.put(
			PackagesResourceConfig.PROPERTY_PACKAGES,
			SwsApiModule.getResourcePackage() + ";" +SwsUiModule.getResourcePackage()
		);
		*/
		serve("/*").with(GuiceContainer.class, guiceContainerConfig);
	}
}
