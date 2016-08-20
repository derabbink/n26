package com.abbink.n26.common.http;

import java.net.URI;

import javax.ws.rs.core.Response;

import com.abbink.n26.common.error.WebAppError;

public class Redirect extends WebAppError {
	private static final long serialVersionUID = 921247825781585946L;

	public Redirect(URI destination) {
		super(Response.seeOther(destination).build());
	}
	
}
