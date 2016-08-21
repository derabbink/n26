package com.abbink.n26.api.http;

import com.fasterxml.jackson.annotation.JsonGetter;

public class StatusResult {
	private final String status;
	
	public StatusResult(boolean ok) {
		this.status = ok ? "ok" : "error";
	}
	
	@JsonGetter
	public String getStatus() {
		return status;
	}
}
