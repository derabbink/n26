package com.abbink.n26.service.storage;

public class StorageError extends RuntimeException {
	private static final long serialVersionUID = -8179792792094132752L;
	
	public StorageError(String message) {
		super(message);
	}
}
