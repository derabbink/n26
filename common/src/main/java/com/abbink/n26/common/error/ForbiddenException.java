package com.abbink.n26.common.error;

import javax.ws.rs.core.Response.Status;

public class ForbiddenException extends WebAppError {
	private static final long serialVersionUID = 4456064593268686139L;

	public ForbiddenException(int code, String message) {
		// disguise as 404
		super(code, message, Status.NOT_FOUND);
	}
	
	public ForbiddenException(int code, String message, Throwable cause) {
		// disguise as 404
		super(code, message, Status.NOT_FOUND, cause);
	}
}
