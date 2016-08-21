package com.abbink.n26.service.storage.di;

import com.abbink.n26.service.storage.TransactionStore;
import com.abbink.n26.service.storage.TransactionStoreImpl;
import com.abbink.n26.service.storage.TypeStore;
import com.abbink.n26.service.storage.TypeStoreImpl;
import com.google.inject.AbstractModule;

public class StorageModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(TransactionStore.class).to(TransactionStoreImpl.class);
		bind(TypeStore.class).to(TypeStoreImpl.class);
	}
	
}
