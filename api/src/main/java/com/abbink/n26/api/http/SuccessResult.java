package com.abbink.n26.api.http;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResult {
	private final boolean success;
	
	public SuccessResult(boolean success) {
		this.success = success;
	}
	
	@JsonProperty
	public boolean getSuccess() {
		return success;
	}
}
