package com.abbink.n26.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.abbink.n26.service.data.Transaction;
import com.abbink.n26.service.storage.TransactionStore;
import com.abbink.n26.service.storage.TypeStore;

/**
 * This is mostly an integration test, testing how calls to {@link TransactionService}
 * affect interactions with {@link TransactionStore} and {@link TypeStore}
 */
public class TransactionServiceTest {
	private TransactionService service;
	private TransactionStore transactionStore;
	private TypeStore typeStore;
	
	@Before
	public void before() {
		transactionStore = mock(TransactionStore.class);
		typeStore = mock(TypeStore.class);
		service = spy(new TransactionService(transactionStore, typeStore));
	}
	
	@Test
	public void addingTouchesAllStores() {
		verifyZeroInteractions(service, transactionStore, typeStore);
		long id = 1;
		// make system think ID 1 does not yet exist
		when(transactionStore.contains(id)).thenReturn(false);
		String type = "t";
		Transaction transaction = new Transaction(0, type);
		service.storeTransaction(id, transaction);
		
		verify(transactionStore).contains(id);
		verify(service).storeTransaction(id, transaction);
		verify(transactionStore).add(id, transaction);
		verify(typeStore).add(type, id);
	}
	
	@Test
	public void updatingTouchesAllStores() {
		verifyZeroInteractions(service, transactionStore, typeStore);
		long id = 1;
		String typeA = "a";
		String typeB = "b";
		Transaction transactionA = new Transaction(0, typeA);
		Transaction transactionB = new Transaction(0, typeB);
		// make system think ID 1 already exists
		when(transactionStore.contains(id)).thenReturn(true);
		when(transactionStore.get(id)).thenReturn(transactionA);
		service.storeTransaction(id, transactionB);
		
		verify(transactionStore).contains(id);
		verify(service).storeTransaction(id, transactionB);
		verify(transactionStore).update(id, transactionB);
		verify(transactionStore).get(id);
		verify(typeStore).delete(typeA, id);
		verify(typeStore).add(typeB, id);
	}
	
	@Test
	public void gettingNonExistentIdReturnsNull() {
		verifyZeroInteractions(service, transactionStore);
		long id = -1;
		when(transactionStore.get(id)).thenReturn(null);
		
		assertThat(service.getTransaction(id), is(nullValue()));
		verify(service).getTransaction(id);
		verify(transactionStore).get(id);
	}
	
	@Test
	public void gettingTransactionsByTypeTouchesTypeStore() {
		verifyZeroInteractions(service, typeStore);
		String type = "t";
		service.getTransactionIdsByType(type);
		
		verify(service).getTransactionIdsByType(type);
		verify(typeStore).getIdsByType(type);
	}
	
	@Test
	public void gettingSumTouchesTransactionStore() {
		verifyZeroInteractions(service, transactionStore);
		long id = 1;
		// make system think ID 1 already exists
		when(transactionStore.contains(id)).thenReturn(true);
		
		service.getTransactionSumForSubtree(id);
		verify(service).getTransactionSumForSubtree(id);
		verify(transactionStore).getSubtreeSum(id);
	}
	
}
