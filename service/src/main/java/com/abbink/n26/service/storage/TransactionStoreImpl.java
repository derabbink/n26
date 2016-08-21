package com.abbink.n26.service.storage;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.abbink.n26.service.data.Transaction;

/**
 * Simple in-memory storage implementation for the graph of linked transactions.
 * The graph is stored in Maps, because those could be more easily translated to
 * persistent storage (NoSQL or RDBMS), but for all intents and purposes think of
 * this as an actual graph.
 * This takes `O(n)` memory; for every transaction that is stored, we also store
 * the sum of it and its children in its tree of transactions, and its id. That
 * costs `O(n)` space, where `n` is the total number of transactions.
 */
@Singleton
public class TransactionStoreImpl implements TransactionStore {
	private Map<Long, Transaction> transactions;
	private Map<Long, Double> sumsBySubtree;
	
	public TransactionStoreImpl() {
		transactions = new HashMap<Long, Transaction>();
		sumsBySubtree = new HashMap<Long, Double>();
	}
	
	@Override
	public boolean contains(long id) {
		return transactions.containsKey(id);
	}
	
	@Override
	public Transaction get(long id) {
		return transactions.get(id);
	}
	
	/**
	 * This depends on {@link #propagateSum(double, Long)}, which takes `O(k)` time,
	 * so this does too.
	 */
	@Override
	public void add(long id, Transaction t) {
		if (contains(id)) {
			throw new StorageError("Duplicate entry for id " + id);
		}
		if (t.getParentId() != null && !contains(t.getParentId())) {
			throw new StorageError("Parent id " + id + " does not exist");
		}
		
		transactions.put(id, t);
		sumsBySubtree.put(id, t.getAmount());
		propagateSum(t.getAmount(), t.getParentId());
	}
	
	/**
	 * This depends on {@link #propagateSum(double, Long)}, which takes `O(k)` time,
	 * so this does too.
	 */
	@Override
	public void update(long id, Transaction t) {
		if (!contains(id)) {
			throw new StorageError("Entry with id " + id + "does not exist");
		}
		
		Transaction oldT = get(id);
		double amountDelta = t.getAmount() - oldT.getAmount();
		double sum = sumsBySubtree.get(id);
		if (oldT.getParentId() == t.getParentId()) {
			if (oldT.getAmount() != t.getAmount()) {
				propagateSum(amountDelta, t.getParentId());
			}
		} else {
			propagateSum(-sum, oldT.getParentId());
			propagateSum(sum + amountDelta, t.getParentId());
		}
		transactions.put(id, t);
		sumsBySubtree.put(id, sum + amountDelta);
	}
	
	/**
	 * This takes `O(1)` time, since we did all the hard work during
	 * {@link #add(long, Transaction)} and {@link #update(long, Transaction)}.
	 */
	public double getSubtreeSum(long rootId) {
		if (!contains(rootId)) {
			throw new StorageError("Entry with id " + rootId + " does not exist");
		}
		
		return sumsBySubtree.get(rootId);
	}
	
	/**
	 * Propagates a change in sums through the tree of transactions, starting at a given node.
	 * Takes `O(k)` time, where `k` is the length of the path in the transaction tree. No
	 * additional memory is required.
	 * @param amountDelta difference to add to every node in the parent chain
	 * @param id Node to start the propagation at. Will iterate upwards until an absolute root is found.
	 */
	private void propagateSum(double amountDelta, Long id) {
		while (id != null) {
			double sum = sumsBySubtree.get(id) + amountDelta;
			sumsBySubtree.put(id,  sum);
			id = get(id).getParentId();
		}
	}
}
