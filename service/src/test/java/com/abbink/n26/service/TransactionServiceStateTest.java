package com.abbink.n26.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.abbink.n26.service.data.Transaction;
import com.abbink.n26.service.storage.StorageError;
import com.abbink.n26.service.storage.TransactionStoreImpl;
import com.abbink.n26.service.storage.TypeStoreImpl;

public class TransactionServiceStateTest {
	private TransactionService service;
	
	@Before
	public void before() {
		service = spy(new TransactionService(
			new TransactionStoreImpl(),
			new TypeStoreImpl()
		));
	}
	
	@Test
	public void storingSameTransactionTwiceWorks() {
		verifyZeroInteractions(service);
		long id = 1;
		Transaction t = new Transaction(0, "t");
		service.storeTransaction(id, t);
		service.storeTransaction(id, t);
		
		verify(service, times(2)).storeTransaction(id, t);
	}
	
	/**
	 * Arguably, this test is not what you want when you have an actual storage
	 * system. In that case you might want to test for equality, rather than
	 * identity.
	 */
	@Test
	public void gettingTransactionReturnsSameInstanceAsStoringArgument() {
		verifyZeroInteractions(service);
		long id = 1;
		Transaction t = new Transaction(0, "t");
		service.storeTransaction(id, t);
		
		assertThat(service.getTransaction(id), is(sameInstance(t)));
		verify(service).storeTransaction(id, t);
		verify(service).getTransaction(id);
	}
	
	@Test(expected=StorageError.class)
	public void storingInvalidParentIdThrows() {
		verifyZeroInteractions(service);
		long id = 1;
		long parentId = -1;
		Transaction t = new Transaction(0, "t", parentId);
		service.storeTransaction(id, t);
	}
	
	@Test
	public void gettingNonexistentIdReturnsNull() {
		verifyZeroInteractions(service);
		long id = -1;
		assertThat(service.getTransaction(id), is(nullValue()));
	}
	
	@Test
	public void gettingTransactionIdsForNonexistentTypeReturnsEmptyList() {
		verifyZeroInteractions(service);
		String type = "t";
		assertThat(service.getTransactionIdsByType(type), is(empty()));
		verify(service).getTransactionIdsByType(type);
	}
	
	@Test(expected=StorageError.class)
	public void gettingSumForNonexistentIdThrows() {
		verifyZeroInteractions(service);
		long id = -1;
		service.getTransactionSumForSubtree(id);
	}
}
