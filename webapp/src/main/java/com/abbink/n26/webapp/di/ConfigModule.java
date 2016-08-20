package com.abbink.n26.webapp.di;

import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {
	
	@Override
	protected void configure() {
		// here you can use guice to bind any config params (e.g. JDBC strings)
	}
	
}
