package com.abbink.n26.api.error;

import javax.ws.rs.core.Response.Status;

import com.abbink.n26.common.error.WebAppError;

public class InvalidInputError extends WebAppError {
	public InvalidInputError(Throwable cause) {
		super(100, "Invalid input.", Status.NOT_ACCEPTABLE, cause);
	}
}
