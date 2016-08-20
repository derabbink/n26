package com.abbink.n26.common.jersey.di;

import com.abbink.n26.common.jersey.OverrideInputTypeResourceFilterFactory;
import com.google.inject.AbstractModule;

/**
 * Module to tweak/extend Jersey's behavior
 */
public class JerseyModule extends AbstractModule {
	@Override
	protected void configure() {
		// add @OverrideInputType resource method interceptor to jersey
		bind(OverrideInputTypeResourceFilterFactory.class);
	}
}
