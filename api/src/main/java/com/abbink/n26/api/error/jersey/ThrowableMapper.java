package com.abbink.n26.api.error.jersey;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.abbink.n26.api.error.JsonError;
import com.abbink.n26.common.error.jersey.SpecializedExceptionMapper;

public class ThrowableMapper implements SpecializedExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception, HttpHeaders headers, HttpServletRequest request) {
		return Response
			.status(Status.INTERNAL_SERVER_ERROR)
			.entity(new JsonError(0, "An unknown error occurred."))
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
	
}
