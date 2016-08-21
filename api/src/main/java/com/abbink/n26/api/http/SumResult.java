package com.abbink.n26.api.http;

import com.fasterxml.jackson.annotation.JsonGetter;

public class SumResult {
	private final double sum;
	
	public SumResult(double sum) {
		this.sum = sum;
	}
	
	@JsonGetter
	public double getSum() {
		return sum;
	}
}
