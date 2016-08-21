package com.abbink.n26.service;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.abbink.n26.service.data.Transaction;
import com.abbink.n26.service.storage.TransactionStore;
import com.abbink.n26.service.storage.TypeStore;

/**
 * This class takes care of coordinating data reads & writes across different storage components.
 * It makes sure the global data representation is consistent, and it acts as a thread-safety barrier.
 * The {@link #transactionStore} makes sure the corpus of transactions (and the trees they form) is
 * consistent. The {@link #typeStore} is merely a means to speed up the per-type retrieval.
 */
@Singleton
public class TransactionService {
	private TransactionStore transactionStore;
	private TypeStore typeStore;
	
	@Inject
	public TransactionService(
		TransactionStore transactionStore,
		TypeStore typeStore
	) {
		this.transactionStore = transactionStore;
		this.typeStore = typeStore;
	}
	
	private void add(long id, Transaction t) {
		transactionStore.add(id, t);
		typeStore.add(t.getType(), id);
	}
	
	private void update(long id, Transaction t) {
		Transaction oldT = transactionStore.get(id);
		transactionStore.update(id, t);
		if (!t.getType().equals(oldT.getType())) {
			typeStore.delete(oldT.getType(), id);
			typeStore.add(t.getType(), id);
		}
	}
	
	public synchronized void storeTransaction(long id, Transaction t) {
		if (transactionStore.contains(id)) {
			update(id, t);
		} else {
			add(id, t);
		}
	}
	
	public synchronized Transaction getTransaction(long id) {
		return transactionStore.get(id);
	}
	
	public synchronized Collection<Long> getTransactionIdsByType(@Nonnull String type) {
		return typeStore.getIdsByType(type);
	}
	
	public synchronized double getTransactionSumForSubtree(long rootId) {
		return transactionStore.getSubtreeSum(rootId);
	}
}
