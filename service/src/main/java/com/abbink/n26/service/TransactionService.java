package com.abbink.n26.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.abbink.n26.service.data.Transaction;

@Singleton
public class TransactionService {
	private Map<Long, Transaction> transactions;
	
	public TransactionService() {
		transactions = new HashMap<Long, Transaction>();
	}
	
	public synchronized void storeTransaction(long id, Transaction t) {
		transactions.put(id, t);
	}
	
	public synchronized Transaction getTransaction(long id) {
		return transactions.get(id);
	}
}
