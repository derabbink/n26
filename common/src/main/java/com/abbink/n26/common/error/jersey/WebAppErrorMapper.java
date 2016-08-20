package com.abbink.n26.common.error.jersey;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

import com.abbink.n26.common.error.WebAppError;
import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.api.model.AbstractResourceMethod;

@Singleton
@Provider
@Slf4j
public class WebAppErrorMapper implements ExceptionMapper<WebAppError> {
	
	private Map<MediaType, SpecializedExceptionMapper<WebAppError>> specializedExceptionMappers;
	@Context private ExtendedUriInfo xUriInfo;
	@Context private HttpHeaders headers;
	@Context private HttpServletRequest request;
	
	@Inject
	public WebAppErrorMapper(
		Map<MediaType, SpecializedExceptionMapper<WebAppError>> specializedExceptionMappers
	) {
		this.specializedExceptionMappers = specializedExceptionMappers;
	}
	
	@Override
	public Response toResponse(WebAppError exception) {
		log.info("Mapping WebAppError to response", exception);
		AbstractResourceMethod am = xUriInfo.getMatchedMethod();
		List<MediaType> mediaTypes = null;
		if (am != null) {
			mediaTypes = am.getSupportedOutputTypes();
		}
		List<MediaType> mediaTypes2 = headers.getAcceptableMediaTypes();
		if (mediaTypes == null) {
			mediaTypes = mediaTypes2;
		} else {
			mediaTypes.addAll(mediaTypes2);
		}
		if (mediaTypes != null) {
			SpecializedExceptionMapper<WebAppError> mapper = null;
			Iterator<MediaType> iter = mediaTypes.iterator();
			while (mapper == null && iter.hasNext()) {
				mapper = specializedExceptionMappers.get(iter.next());
			}
			if (mapper != null) {
				return mapper.toResponse(exception, headers, request);
			}
		}
		
		// default behavior
		StringBuilder sb = new StringBuilder()
			.append("Error\n")
			.append("Code: ").append(exception.getCode()).append("\n");
		if (exception.getMessage() != null) {
			sb.append("Message: ").append(exception.getMessage()).append("\n");
		}
		return Response
			.status(exception.getResponse().getStatus())
			.entity(sb.toString())
			.type(MediaType.TEXT_PLAIN)
			.build();
	}
}
