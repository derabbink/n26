package com.abbink.n26.api.http;

import javax.annotation.Nonnull;

import com.abbink.n26.service.data.Transaction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A container type specifically designed to pass back-and-forth
 * between API clients in JSON format.
 */
@JsonInclude(Include.NON_NULL)
public class JsonTransaction {
	private double amount;
	private String type;
	private Long parentId;
	
	public static JsonTransaction fromTransaction(Transaction t) {
		return new JsonTransaction(t.getAmount(), t.getType(), t.getParentId());
	}
	
	public JsonTransaction(double amount, String type) {
		this(amount, type, null);
	}
	
	@JsonCreator
	public JsonTransaction(
		@JsonProperty("amount") double amount,
		@JsonProperty("type") @Nonnull String type,
		@JsonProperty("parent_id") Long parentId
	) {
		this.amount = amount;
		this.type = type;
		this.parentId = parentId;
	}
	
	@JsonGetter
	public double getAmount() {
		return amount;
	}
	
	@JsonGetter
	public String getType() {
		return type;
	}
	
	@JsonGetter("parent_id")
	public Long getParentId() {
		return parentId;
	}
	
	public Transaction toTransaction() {
		return new Transaction(amount, type, parentId);
	}
}
