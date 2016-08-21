package com.abbink.n26.service.storage;

import java.util.Collection;

import javax.inject.Singleton;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * This is a simple in-memory implementation of the {@link TypeStore}. The purpose of
 * this is to speed up the per-type lookup of transactions. It requires `O(n)` space,
 * where `n` is the total number of transactions. For every type (of which there are
 * at most n), a list of its corresponding transaction IDs is stored.
 * All operations work in `O(1)` time, since {@link #idsByType} is a HashMap of HashSets.
 */
@Singleton
public class TypeStoreImpl implements TypeStore {
	private Multimap<String, Long> idsByType;
	
	public TypeStoreImpl() {
		idsByType = HashMultimap.create();
	}
	
	@Override
	public boolean contains(String type, long id) {
		return idsByType.containsEntry(type, id);
	}
	
	@Override
	public Collection<Long> getIdsByType(String type) {
		return idsByType.get(type);
	}
	
	@Override
	public void add(String type, long id) {
		if (contains(type, id)) {
			throw new StorageError("Type " + type + " already contains id " + id);
		}
		
		idsByType.put(type, id);
	}

	@Override
	public void delete(String type, long id) {
		if (!contains(type, id)) {
			throw new StorageError("Type " + type + " does not contain " + id);
		}
		
		idsByType.remove(type, id);
	}
	
}
