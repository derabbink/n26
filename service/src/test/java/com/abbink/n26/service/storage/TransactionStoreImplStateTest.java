package com.abbink.n26.service.storage;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.abbink.n26.service.data.Transaction;

public class TransactionStoreImplStateTest {
	private TransactionStoreImpl store;
	
	@Before
	public void before() {
		store = spy(new TransactionStoreImpl());
	}
	
	@Test(expected=StorageError.class)
	public void addingDuplicateIdThrows() {
		verifyZeroInteractions(store);
		long id = 1;
		Transaction transaction = new Transaction(0, "t");
		store.add(id, transaction);
		
		verify(store).add(anyLong(), anyObject());
		
		store.add(id, transaction);
	}
	
	@Test(expected=StorageError.class)
	public void addingInvalidParentIdThrows() {
		verifyZeroInteractions(store);
		long id = 1;
		long parentId = -1;
		Transaction transaction = new Transaction(0, "t", parentId);
		store.add(id, transaction);
	}
	
	@Test(expected=StorageError.class)
	public void updatingNonexistentIdThrows() {
		verifyZeroInteractions(store);
		long id = -1;
		Transaction transaction = new Transaction(0, "t");
		store.update(id, transaction);
	}
	
	@Test(expected=StorageError.class)
	public void gettingSumForNonexistentIdThrows() {
		verifyZeroInteractions(store);
		long id = -1;
		store.getSubtreeSum(id);
	}
}
