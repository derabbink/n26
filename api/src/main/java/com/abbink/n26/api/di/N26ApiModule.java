package com.abbink.n26.api.di;

import com.abbink.n26.api.error.jersey.di.ExceptionMappersModule;
import com.abbink.n26.api.http.transactionservice.di.TransactionServiceModule;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.sun.jersey.api.core.PackagesResourceConfig;

/**
 * The way Jersey and Jackson work together is inspired by this blog post series:
 * http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
 */
public class N26ApiModule extends AbstractModule {
	
	@Override
	protected void configure() {
		// hook Jackson into Jersey as the POJO <-> JSON mapper
		bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
		
		// install API-specific error mappers
		install(new ExceptionMappersModule());
		
		// install resources
		install(new TransactionServiceModule());
	}
	
	/**
	 * package name that contains jersey resources
	 * see {@linkplain PackagesResourceConfig.PROPERTY_PACKAGES} for more details
	 * Only required if you don't want to bind all resources using Guice
	 */
	public static String getResourcePackage() {
		return "com.abbink.n26.api.http";
	}
}
