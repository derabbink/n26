package com.abbink.n26.api.http.transactionservice;

import static com.abbink.n26.api.utils.Constants.BASE_PATH;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import com.abbink.n26.api.error.InvalidInputError;
import com.abbink.n26.api.http.JsonTransaction;
import com.abbink.n26.api.http.StatusResult;
import com.abbink.n26.common.jersey.aop.OverrideInputType;
import com.abbink.n26.service.TransactionService;
import com.abbink.n26.service.data.Transaction;
import com.sun.jersey.api.NotFoundException;

@Slf4j
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Path(BASE_PATH + "transactionservice/transaction/{id}")
public class TransactionResource {
	@Inject private TransactionService transactionService;
	
	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
	@OverrideInputType(MediaType.APPLICATION_JSON)
	public StatusResult put(
		@PathParam("id") long id,
		JsonTransaction transaction
	) {
		log.trace("PUT {}, {}, {}", transaction.getAmount(), transaction.getType(), transaction.getParentId());
		// This should probably be more sophisticated and allow only characters that fit in a URL-path:
		String type = transaction.getType().trim();
		if (type.isEmpty()) {
			throw new InvalidInputError();
		}
		transaction = new JsonTransaction(transaction.getAmount(), type, transaction.getParentId());
		transactionService.storeTransaction(id, transaction.toTransaction());
		return new StatusResult(true);
	}
	
	@GET
	public JsonTransaction get(@PathParam("id") long id) {
		log.trace("GET {}", id);
		Transaction t = transactionService.getTransaction(id);
		if (t != null) {
			return JsonTransaction.fromTransaction(t);
		}
		
		throw new NotFoundException();
	}
	
}
