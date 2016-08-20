package com.abbink.n26.api.http.transactionservice;

import static com.abbink.n26.api.utils.Constants.BASE_PATH;

import javax.inject.Singleton;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import com.abbink.n26.api.http.SuccessResult;
import com.abbink.n26.api.http.Transaction;
import com.abbink.n26.common.jersey.aop.OverrideInputType;

@Slf4j
@Singleton
@Produces(MediaType.APPLICATION_JSON)
@Path(BASE_PATH + "transactionservice/transaction/{transaction_id}")
public class TransactionResource {
	
	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
	@OverrideInputType(MediaType.APPLICATION_JSON)
	public SuccessResult put(
		@PathParam("transaction_id") String id,
		Transaction transaction
	) {
		log.trace("PUT {}, {}, {}", transaction.amount, transaction.type, transaction.parent_id);
		return new SuccessResult(true);
	}
	
}
