package pw.bitcoinroulette.gui.account;

public class TransactionRow{
	private String address;
	private double amount;
	private String status;
	
	public TransactionRow(String address, double amount, String status){
		this.address = address;
		this.amount = amount;
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public double getAmount() {
		return amount;
	}

	public String getStatus() {
		return status;
	}

}