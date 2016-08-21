package com.abbink.n26.service.storage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.abbink.n26.service.data.Transaction;

public class TransactionStoreImplSumTest {
	private TransactionStoreImpl store;
	private final static String TYPE = "T";
	private final static long ID1 = 1;
	private final static long ID2 = 2;
	private final static long ID3 = 3;
	private final static long ID4 = 4;
	private final static long ID5 = 5;
	private final static long ID6 = 6;
	private final static long ID7 = 7;
	private final static long ID101 = 101;
	private final static long ID102 = 102;
	private final static long ID103 = 103;
	private final static long ID104 = 104;
	private final static long ID105 = 105;
	private final static long ID106 = 106;
	private final static double AMOUNT1 = 1;
	private final static double AMOUNT2 = 2;
	private final static double AMOUNT3 = 4;
	private final static double AMOUNT4 = 8;
	private final static double AMOUNT5 = 16;
	private final static double AMOUNT6 = 32;
	private final static double AMOUNT7 = 64;
	private final static double AMOUNT101 = 1;
	private final static double AMOUNT102 = 2;
	private final static double AMOUNT103 = 4;
	private final static double AMOUNT104 = 8;
	private final static double AMOUNT105 = 16;
	private final static double AMOUNT106 = 32;
	private final static double SUM7 = AMOUNT7;
	private final static double SUM6 = AMOUNT6;
	private final static double SUM5 = AMOUNT5;
	private final static double SUM4 = AMOUNT4;
	private final static double SUM3 = AMOUNT3 + SUM6 + SUM7;
	private final static double SUM2 = AMOUNT2 + SUM4 + SUM5;
	private final static double SUM1 = AMOUNT1 + SUM2 + SUM3;
	private final static double SUM106 = AMOUNT106;
	private final static double SUM105 = AMOUNT105;
	private final static double SUM104 = AMOUNT104;
	private final static double SUM103 = AMOUNT103;
	private final static double SUM102 = AMOUNT102 + SUM104 + SUM105 + SUM106;
	private final static double SUM101 = AMOUNT101 + SUM102 + SUM103;
	
	
	/**
	 * Creates two transaction trees:
	 * id 1, amount 1 (sum 127)
	 *  - id 2, amount 2 (sum 26)
	 *    - id 4, amount 8 (sum 8)
	 *    - id 5, amount 16 (sum 16)
	 *  - id 3, amount 4 (sum 100)
	 *    - id 6, amount 32 (sum 32)
	 *    - id 7, amount 64 (sum 64)
	 * 
	 * id 101, amount 1 (sum 63)
	 *  - id 102, amount 2 (sum 58)
	 *    - id 104, amount 8 (sum 8)
	 *    - id 105, amount 16 (sum 16)
	 *    - id 106, amount 32 (sum 32)
	 *  - id 103, amount 4 (sum 4)
	 */
	@Before
	public void before() {
		store = spy(new TransactionStoreImpl());
		store.add(ID1, new Transaction(AMOUNT1, TYPE));
		store.add(ID2, new Transaction(AMOUNT2, TYPE, ID1));
		store.add(ID3, new Transaction(AMOUNT3, TYPE, ID1));
		store.add(ID4, new Transaction(AMOUNT4, TYPE, ID2));
		store.add(ID5, new Transaction(AMOUNT5, TYPE, ID2));
		store.add(ID6, new Transaction(AMOUNT6, TYPE, ID3));
		store.add(ID7, new Transaction(AMOUNT7, TYPE, ID3));
		verify(store, times(7)).add(anyLong(), anyObject());
		
		store.add(ID101, new Transaction(AMOUNT101, TYPE));
		store.add(ID102, new Transaction(AMOUNT102, TYPE, ID101));
		store.add(ID103, new Transaction(AMOUNT103, TYPE, ID101));
		store.add(ID104, new Transaction(AMOUNT104, TYPE, ID102));
		store.add(ID105, new Transaction(AMOUNT105, TYPE, ID102));
		store.add(ID106, new Transaction(AMOUNT106, TYPE, ID102));
		verify(store, times(7 + 6)).add(anyLong(), anyObject());
	}
	
	@Test
	public void sumsAreConstructedCorrectly() {
		assertThat(store.getSubtreeSum(ID1), is(SUM1));
		assertThat(store.getSubtreeSum(ID2), is(SUM2));
		assertThat(store.getSubtreeSum(ID3), is(SUM3));
		assertThat(store.getSubtreeSum(ID4), is(SUM4));
		assertThat(store.getSubtreeSum(ID5), is(SUM5));
		assertThat(store.getSubtreeSum(ID6), is(SUM6));
		assertThat(store.getSubtreeSum(ID7), is(SUM7));
		verify(store, times(7)).getSubtreeSum(anyLong());
	}
	
	@Test
	public void sumPropagatesCorrectlyOnUpdate() {
		long delta = 100;
		store.update(ID5, new Transaction(AMOUNT5 + delta, TYPE, ID2));
		
		assertThat(store.getSubtreeSum(ID5), is(SUM5 + delta));
		assertThat(store.getSubtreeSum(ID2), is(SUM2 + delta));
		assertThat(store.getSubtreeSum(ID1), is(SUM1 + delta));
		verify(store).update(anyLong(), anyObject());
		verify(store, times(3)).getSubtreeSum(anyLong());
	}
	
	@Test
	public void sumPropagatesCorrectlyWhenSplittingTree() {
		store.update(ID2, new Transaction(AMOUNT2, TYPE));
		
		assertThat(store.getSubtreeSum(ID2), is(SUM2));
		assertThat(store.getSubtreeSum(ID1), is(SUM1 - SUM2));
		verify(store).update(anyLong(), anyObject());
		verify(store, times(2)).getSubtreeSum(anyLong());
	}
	
	@Test
	public void sumPropagatesCorrectlyWhenSplittingTreeAndUpdatingAmount() {
		long delta = -100;
		store.update(ID2, new Transaction(AMOUNT2 + delta, TYPE));
		
		assertThat(store.getSubtreeSum(ID2), is(SUM2 + delta));
		assertThat(store.getSubtreeSum(ID1), is(SUM1 - SUM2));
		verify(store).update(anyLong(), anyObject());
		verify(store, times(2)).getSubtreeSum(anyLong());
	}
	
	@Test
	public void sumPropagatesCorrectlyWhenMovingSubtree() {
		store.update(ID2, new Transaction(AMOUNT2, TYPE, ID102));
		
		assertThat(store.getSubtreeSum(ID2), is(SUM2));
		assertThat(store.getSubtreeSum(ID102), is(SUM102 + SUM2));
		assertThat(store.getSubtreeSum(ID101), is(SUM101 + SUM2));
		assertThat(store.getSubtreeSum(ID1), is(SUM1 - SUM2));
		verify(store).update(anyLong(), anyObject());
		verify(store, times(4)).getSubtreeSum(anyLong());
	}
	
	@Test
	public void sumPropagatesCorrectlyWhenMovingSubtreeAndUpdatingAmount() {
		long delta = -100;
		store.update(ID2, new Transaction(AMOUNT2 + delta, TYPE, ID102));
		
		assertThat(store.getSubtreeSum(ID2), is(SUM2 + delta));
		assertThat(store.getSubtreeSum(ID102), is(SUM102 + SUM2 + delta));
		assertThat(store.getSubtreeSum(ID101), is(SUM101 + SUM2 + delta));
		assertThat(store.getSubtreeSum(ID1), is(SUM1 - SUM2));
		verify(store).update(anyLong(), anyObject());
		verify(store, times(4)).getSubtreeSum(anyLong());
	}
}
