package main.java.pw.bitcoinroulette.client.account;

import java.math.BigDecimal;

public class TransactionRow{
	private String address;
	private BigDecimal amount;
	private String status;
	
	public TransactionRow(String address, BigDecimal amount, String status){
		this.address = address;
		this.amount = amount;
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getStatus() {
		return status;
	}

}