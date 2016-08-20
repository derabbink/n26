package com.abbink.n26.api.http;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class Transaction {
	public double amount;
	public String type;
	public long parent_id;
}
