package com.sun.jersey.spi.container;

import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

/**
 * This class merely exists as a way to alter the {@link #getMediaType()} behavior of an
 * otherwise read-only class. In order to make use of {@link com.sun.jersey.spi.container.ContainerRequest#ContainerRequest(ContainerRequest)},
 * this has to live in {@link com.sun.jersey.spi.container}
 */
@Slf4j
public class WrappedContainerRequest extends ContainerRequest {
	private MediaType targetType;
	
	public WrappedContainerRequest(ContainerRequest original, MediaType targetType) {
		super(original);
		this.targetType = targetType;
	}
	
	@Override
	public MediaType getMediaType() {
		log.trace("Overriding getMediaType from {} to {}", super.getMediaType(), targetType);
		return targetType;
	}

}
