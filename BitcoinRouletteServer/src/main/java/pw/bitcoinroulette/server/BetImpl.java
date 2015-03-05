package main.java.pw.bitcoinroulette.server;


import java.util.HashSet;

import main.java.pw.bitcoinroulette.library.AuthPlayer;
import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.ServerGame;


public class BetImpl implements Bet {
	private AuthPlayer authPlayer;
	private ServerGame serverGame;
	public int payout;
	public double amount;
	public HashSet<Integer> winning = new HashSet<Integer>();
	private String description;
	

	public BetImpl(double amount, int payout, HashSet<Integer> winning, String description){
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

	public AuthPlayer getPlayer() {
		return authPlayer;
	}

	public void setPlayer(AuthPlayer authPlayer) {
		this.authPlayer = authPlayer;
	}
}

