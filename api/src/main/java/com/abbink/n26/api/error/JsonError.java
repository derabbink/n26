package com.abbink.n26.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonError {
	private int code;
	private String message;
	
	public JsonError(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@JsonProperty
	public int getCode() {
		return code;
	}
	
	@JsonProperty
	public String getMessage() {
		return message;
	}
}
