package com.abbink.n26.common.metrics.timing;


import static com.abbink.n26.common.metrics.MetricUtils.getMetricBaseName;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.sun.jersey.api.model.AbstractResourceMethod;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;
import com.sun.jersey.spi.dispatch.RequestDispatcher;

public class TimingResourceMethodDispatchProvider implements ResourceMethodDispatchProvider {
	private final MetricRegistry metricRegistry;
	private final ResourceMethodDispatchProvider wrappedProvider;
	
	TimingResourceMethodDispatchProvider(
		MetricRegistry metricsRegistry,
		ResourceMethodDispatchProvider wrappedProvider
	) {
		this.metricRegistry = metricsRegistry;
		this.wrappedProvider = wrappedProvider;
	}
	
	public RequestDispatcher create(AbstractResourceMethod abstractResourceMethod) {
		String metricBaseName = getMetricBaseName(abstractResourceMethod);
		Timer timer = metricRegistry.timer(abstractResourceMethod.getResource().getResourceClass().getName()
			+ "#" + metricBaseName + " timer");
		return new TimingRequestDispatcher(wrappedProvider.create(abstractResourceMethod), timer);
	}
}
