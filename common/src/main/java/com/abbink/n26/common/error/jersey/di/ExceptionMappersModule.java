package com.abbink.n26.common.error.jersey.di;

import javax.ws.rs.core.MediaType;

import com.abbink.n26.common.error.WebAppError;
import com.abbink.n26.common.error.jersey.NotFoundExceptionMapper;
import com.abbink.n26.common.error.jersey.RedirectMapper;
import com.abbink.n26.common.error.jersey.SpecializedExceptionMapper;
import com.abbink.n26.common.error.jersey.ThrowableMapper;
import com.abbink.n26.common.error.jersey.WebAppErrorMapper;
import com.abbink.n26.common.http.Redirect;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.sun.jersey.api.NotFoundException;

public class ExceptionMappersModule extends AbstractModule {
	
	@Override
	protected void configure() {
		configureRedirectMappers();
		configureWebAppErrorMappers();
		configureNotFoundExceptionMappers();
		configureThrowableMappers();
	}
	
	private void configureRedirectMappers() {
		bind(RedirectMapper.class);
		@SuppressWarnings("unused")
		MapBinder<MediaType, SpecializedExceptionMapper<Redirect>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<Redirect>>() {}
		);
		// nothing else to do here right now; no common error handlers are bound here.
	}
	
	private void configureWebAppErrorMappers() {
		bind(WebAppErrorMapper.class);
		@SuppressWarnings("unused")
		MapBinder<MediaType, SpecializedExceptionMapper<WebAppError>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<WebAppError>>() {}
		);
		// nothing else to do here right now; no common error handlers are bound here.
	}
	
	private void configureNotFoundExceptionMappers() {
		bind(NotFoundExceptionMapper.class);
		@SuppressWarnings("unused")
		MapBinder<MediaType, SpecializedExceptionMapper<NotFoundException>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<NotFoundException>>() {}
		);
		// nothing else to do here right now; no common error handlers are bound here.
	}
	
	private void configureThrowableMappers() {
		bind(ThrowableMapper.class);
		@SuppressWarnings("unused")
		MapBinder<MediaType, SpecializedExceptionMapper<Throwable>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<Throwable>>() {}
		);
		// nothing else to do here right now; no common error handlers are bound here.
	}
}

