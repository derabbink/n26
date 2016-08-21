package com.abbink.n26.service.storage;

import java.util.Collection;

public interface TypeStore {
	boolean contains(String type, long id);
	Collection<Long> getIdsByType(String type);
	void add(String type, long id);
	void delete(String type, long id);
}
