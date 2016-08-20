package com.abbink.n26.service.di;

import com.abbink.n26.service.TransactionService;
import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule{
	
	@Override
	protected void configure() {
		bind(TransactionService.class);
	}
	
}
