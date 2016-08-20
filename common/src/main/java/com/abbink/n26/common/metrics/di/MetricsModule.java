package com.abbink.n26.common.metrics.di;

import com.abbink.n26.common.metrics.response.HttpStatusCodeMetricResourceFilterFactory;
import com.abbink.n26.common.metrics.timing.TimingResourceMethodDispatchAdapter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Everything in the {@link com.abbink.n26.common.metrics} package
 * is inspired by this blog post series:
 * http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
 */
public class MetricsModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(MetricRegistry.class).in(Scopes.SINGLETON);
		
		bind(TimingResourceMethodDispatchAdapter.class);
		bind(HttpStatusCodeMetricResourceFilterFactory.class);
	}
	
}
