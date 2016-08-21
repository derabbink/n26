package com.abbink.n26.api.error;

import javax.ws.rs.core.Response.Status;

import com.abbink.n26.common.error.WebAppError;

public class InvalidInputError extends WebAppError {
	private static final long serialVersionUID = 5013207214476453596L;

	public InvalidInputError(Throwable cause) {
		super(100, "Invalid input.", Status.NOT_ACCEPTABLE, cause);
	}
}
