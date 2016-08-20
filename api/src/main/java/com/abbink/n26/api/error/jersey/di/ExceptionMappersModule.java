package com.abbink.n26.api.error.jersey.di;

import javax.ws.rs.core.MediaType;

import com.abbink.n26.api.error.jersey.NotFoundExceptionMapper;
import com.abbink.n26.api.error.jersey.ThrowableMapper;
import com.abbink.n26.api.error.jersey.WebAppErrorMapper;
import com.abbink.n26.common.error.WebAppError;
import com.abbink.n26.common.error.jersey.RedirectMapper;
import com.abbink.n26.common.error.jersey.SpecializedExceptionMapper;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.sun.jersey.api.NotFoundException;

public class ExceptionMappersModule extends AbstractModule {
	
	@Override
	protected void configure() {
		requireBinding(RedirectMapper.class);
		requireBinding(WebAppErrorMapper.class);
		requireBinding(NotFoundExceptionMapper.class);
		requireBinding(ThrowableMapper.class);
		
		configureWebAppErrorMapper();
		configureNotFoundExceptionMapper();
		configureThrowableMapper();
	}
	
	private void configureWebAppErrorMapper() {
		MapBinder<MediaType, SpecializedExceptionMapper<WebAppError>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<WebAppError>>() {}
		);
		mapBinder.addBinding(MediaType.APPLICATION_JSON_TYPE).to(WebAppErrorMapper.class);
	}
	
	private void configureNotFoundExceptionMapper() {
		MapBinder<MediaType, SpecializedExceptionMapper<NotFoundException>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<NotFoundException>>() {}
		);
		mapBinder.addBinding(MediaType.APPLICATION_JSON_TYPE).to(NotFoundExceptionMapper.class);
	}
	
	private void configureThrowableMapper() {
		MapBinder<MediaType, SpecializedExceptionMapper<Throwable>> mapBinder = MapBinder.newMapBinder(
			binder(),
			new TypeLiteral<MediaType>() {},
			new TypeLiteral<SpecializedExceptionMapper<Throwable>>() {}
		);
		mapBinder.addBinding(MediaType.APPLICATION_JSON_TYPE).to(ThrowableMapper.class);
	}
}
