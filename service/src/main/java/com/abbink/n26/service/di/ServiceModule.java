package com.abbink.n26.service.di;

import com.abbink.n26.service.TransactionService;
import com.abbink.n26.service.storage.di.StorageModule;
import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule{
	
	@Override
	protected void configure() {
		install(new StorageModule());
		bind(TransactionService.class);
	}
	
}
