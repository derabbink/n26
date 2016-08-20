package com.abbink.n26.common.error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public abstract class WebAppError extends WebApplicationException {
	private static final long serialVersionUID = 4638973790086123455L;
	
	private int code;
	private String message;
	
	public WebAppError(int code, String message) {
		this(code, message, Status.INTERNAL_SERVER_ERROR);
	}
	
	public WebAppError(int code, String message, Status status) {
		super(status);
		this.code = code;
		this.message = message;
	}
	
	public WebAppError(int code, String message, Throwable cause) {
		this(code, message, Status.INTERNAL_SERVER_ERROR, cause);
	}
	
	public WebAppError(int code, String message, Status status, Throwable cause) {
		super(cause, status);
		this.code = code;
		this.message = message;
	}
	
	public WebAppError(Response response) {
		super(response);
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
}
