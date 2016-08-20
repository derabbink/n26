package com.abbink.n26.service.data;


public class Transaction {
	private double amount;
	private String type;
	private Long parentId;
	
	public Transaction(double amount, String type) {
		this(amount, type, null);
	}
	
	public Transaction(
		double amount,
		String type,
		Long parentId
	) {
		this.amount = amount;
		this.type = type;
		this.parentId = parentId;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getType() {
		return type;
	}
	
	public Long getParentId() {
		return parentId;
	}
}
