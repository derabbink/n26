package com.abbink.n26.service.storage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Multimap;

public class TypeStoreImplTest {
	private TypeStoreImpl typeStore = new TypeStoreImpl();
	private Multimap<String, Long> idsByType;
	
	@Before
	public void before() {
		typeStore = new TypeStoreImpl();
		idsByType = typeStore.getIdsByType();
		typeStore = spy(typeStore);
	}
	
	@Test
	public void newInstanceIsEmpty() {
		verifyZeroInteractions(typeStore);
		assertThat(idsByType.size(), equalTo(0));
	}
	
	@Test
	public void adding1stTransactionCreatesOneKeyAndValue() {
		verifyZeroInteractions(typeStore);
		long id = 1;
		String type = "type";
		typeStore.add(type, id);
		
		assertThat(idsByType.keySet().size(), equalTo(1));
		assertThat(idsByType.size(), equalTo(1));
		assertThat(type, isIn(idsByType.keySet()));
		assertThat(id, isIn(idsByType.values()));
		verify(typeStore).add(anyString(), anyLong());
	}
	
	@Test
	public void addingTransactionsWithSameTypeAddsOneKey() {
		verifyZeroInteractions(typeStore);
		long[] ids = new long[]{1, 2, 3, 4};
		String type = "type";
		for(long id : ids) {
			typeStore.add(type, id);
		}
		
		assertThat(idsByType.keySet().size(), equalTo(1));
		assertThat(idsByType.size(), equalTo(ids.length));
		assertThat(type, isIn(idsByType.keySet()));
		for(long id : ids) {
			assertThat(id, isIn(idsByType.values()));
		}
		verify(typeStore, times(ids.length)).add(anyString(), anyLong());
	}
	
	@Test
	public void addingMultipleTypesCreatesMultipleKeys() {
		verifyZeroInteractions(typeStore);
		long[] ids = new long[]{1, 2, 3, 4, 6, 7, 8, 9, 10};
		String[] types = new String[]{"a", "b", "c"};
		for (int i=0; i<ids.length; i++) {
			long id = ids[i];
			String type = types[i % types.length];
			typeStore.add(type, id);
		}
		
		assertThat(idsByType.keySet().size(), equalTo(types.length));
		assertThat(idsByType.size(), equalTo(ids.length));
		for(String type : types) {
			assertThat(type, isIn(idsByType.keySet()));
		}
		for(long id : ids) {
			assertThat(id, isIn(idsByType.values()));
		}
		verify(typeStore, times(ids.length)).add(anyString(), anyLong());
	}
	
	@Test
	public void deletingLastEntryOfTypeErasesKey() {
		verifyZeroInteractions(typeStore);
		long[] idsA = new long[]{1, 2, 3, 4};
		long idB = 10;
		String typeA = "a";
		String typeB = "b";
		for(long idA : idsA) {
			typeStore.add(typeA, idA);
		}
		typeStore.add(typeB, idB);
		
		assertThat(idsByType.keySet().size(), equalTo(2));
		assertThat(idsByType.size(), equalTo(idsA.length + 1));
		assertThat(typeA, isIn(idsByType.keySet()));
		assertThat(typeB, isIn(idsByType.keySet()));
		for(long idA : idsA) {
			assertThat(idA, isIn(idsByType.values()));
		}
		assertThat(idB, isIn(idsByType.values()));
		verify(typeStore, times(idsA.length + 1)).add(anyString(), anyLong());
		
		typeStore.delete(typeB, idB);
		assertThat(idsByType.keySet().size(), equalTo(1));
		assertThat(idsByType.size(), equalTo(idsA.length));
		assertThat(typeA, isIn(idsByType.keySet()));
		assertThat(typeB, not(isIn(idsByType.keySet())));
		for(long idA : idsA) {
			assertThat(idA, isIn(idsByType.values()));
		}
		assertThat(idB, not(isIn(idsByType.values())));
		verify(typeStore).delete(anyString(), anyLong());
	}
	
	@Test(expected=StorageError.class)
	public void insertingSameTwiceFails() {
		verifyZeroInteractions(typeStore);
		long id = 1;
		String type = "type";
		typeStore.add(type, id);
		
		assertThat(idsByType.keySet().size(), equalTo(1));
		assertThat(idsByType.size(), equalTo(1));
		assertThat(type, isIn(idsByType.keySet()));
		assertThat(id, isIn(idsByType.values()));
		verify(typeStore).add(anyString(), anyLong());
		
		typeStore.add(type, id);
	}
	
	@Test(expected=StorageError.class)
	public void deletingNonexistentFails() {
		verifyZeroInteractions(typeStore);
		long id = 1;
		String type = "type";
		typeStore.delete(type, id);
	}
	
	@Test
	public void gettingNonexistentTypeReturnsEmptyCollection() {
		verifyZeroInteractions(typeStore);
		
		assertThat(typeStore.getIdsByType("t"), is(empty()));
	}
}
