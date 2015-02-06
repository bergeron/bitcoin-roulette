package pw.bitcoinroulette;


import java.util.HashSet;


public class Bet {
	public int payout;
	public double amount;
	public HashSet<Integer> winning = new HashSet<Integer>();
	private String description;
	

	public Bet(double amount, int payout, HashSet<Integer> winning, String description){
		this.payout = payout;
		this.amount = amount;
		this.winning = winning;
		this.description = description;
	}
	
	public double getAmount(){
		return amount;
	}
	
	public boolean cameTrue(int resultOfSpin){
		return winning.contains(resultOfSpin);
	}

	public String getDescription() {
		return description;
	}
}

