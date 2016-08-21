package com.abbink.n26.api.http.transactionservice;

import static com.abbink.n26.api.utils.Constants.BASE_PATH;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import com.abbink.n26.service.TransactionService;

@Slf4j
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Path(BASE_PATH + "transactionservice/types/{type}")
public class TypesResource {
	@Inject private TransactionService transactionService;
	
	@GET
	public Collection<Long> get(@PathParam("type") @Nonnull String type) {
		log.trace("GET {}", type);
		return transactionService.getTransactionIdsByType(type);
	}
}
