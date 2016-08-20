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

import com.abbink.n26.common.http.Redirect;
import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.api.model.AbstractResourceMethod;

@Singleton
@Provider
@Slf4j
public class RedirectMapper implements ExceptionMapper<Redirect> {
	
	private Map<MediaType, SpecializedExceptionMapper<Redirect>> specializedExceptionMappers;
	@Context private ExtendedUriInfo xUriInfo;
	@Context private HttpHeaders headers;
	@Context private HttpServletRequest request;
	
	@Inject
	public RedirectMapper(
		Map<MediaType, SpecializedExceptionMapper<Redirect>> specializedExceptionMappers
	) {
		this.specializedExceptionMappers = specializedExceptionMappers;
	}
	
	@Override
	public Response toResponse(Redirect exception) {
		log.info("Mapping Redirect to response", exception);
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
			SpecializedExceptionMapper<Redirect> mapper = null;
			Iterator<MediaType> iter = mediaTypes.iterator();
			while (mapper == null && iter.hasNext()) {
				mapper = specializedExceptionMappers.get(iter.next());
			}
			if (mapper != null) {
				return mapper.toResponse(exception, headers, request);
			}
		}
		
		// default behavior
		return exception.getResponse();
	}
}
