package com.abbink.n26.api.http.transactionservice.di;

import com.abbink.n26.api.http.transactionservice.SumResource;
import com.abbink.n26.api.http.transactionservice.TransactionResource;
import com.abbink.n26.api.http.transactionservice.TypesResource;
import com.google.inject.AbstractModule;

public class TransactionServiceModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(TransactionResource.class);
		bind(TypesResource.class);
		bind(SumResource.class);
	}
}
