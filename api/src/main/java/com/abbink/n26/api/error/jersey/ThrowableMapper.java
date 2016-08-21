package com.abbink.n26.api.error.jersey;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.abbink.n26.api.error.InvalidInputError;
import com.abbink.n26.api.error.JsonError;
import com.abbink.n26.common.error.jersey.SpecializedExceptionMapper;
import com.abbink.n26.service.storage.StorageError;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.ParamException;

public class ThrowableMapper implements SpecializedExceptionMapper<Throwable> {
	private WebAppErrorMapper webappErrorMapper;
	
	@Inject
	public ThrowableMapper(WebAppErrorMapper webappErrorMapper) {
		this.webappErrorMapper = webappErrorMapper;
	}

	@Override
	public Response toResponse(Throwable exception, HttpHeaders headers, HttpServletRequest request) {
		try {
			throw exception;
		} catch (JsonMappingException e) {
			return webappErrorMapper.toResponse(new InvalidInputError(e), headers, request);
		} catch (ParamException e) {
			return webappErrorMapper.toResponse(new InvalidInputError(e), headers, request);
		} catch (StorageError e) {
			// this can only happen if the client attempts to create an invalid state.
			return webappErrorMapper.toResponse(new InvalidInputError(e), headers, request);
		} catch (Throwable e) {}
		return Response
			.status(Status.INTERNAL_SERVER_ERROR)
			.entity(new JsonError(0, "An unknown error occurred."))
			.type(MediaType.APPLICATION_JSON)
			.build();
	}
	
}
