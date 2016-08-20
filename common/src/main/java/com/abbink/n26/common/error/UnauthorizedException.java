package com.abbink.n26.common.error;

import javax.ws.rs.core.Response.Status;

public class UnauthorizedException extends WebAppError {
	private static final long serialVersionUID = 6494575658326412522L;

	public UnauthorizedException(int code, String message) {
		super(code, message, Status.UNAUTHORIZED);
	}
	
	public UnauthorizedException(int code, String message, Throwable cause) {
		super(code, message, Status.UNAUTHORIZED, cause);
	}
}
