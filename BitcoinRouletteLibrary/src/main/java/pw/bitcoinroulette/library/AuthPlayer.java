package main.java.pw.bitcoinroulette.library;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;

public interface AuthPlayer extends Remote, OtherPlayer{

	public BigDecimal getBalance() throws RemoteException;
	
	public String getBitcoinAddress() throws RemoteException;
	
	public List<Transaction> getTransactions() throws RemoteException;

	public Bet makeBet(ServerGame g, BigDecimal amount, int payout, HashSet<Integer> winning, String description) throws RemoteException;

	public String withdraw(String address, BigDecimal amount) throws RemoteException;
	
	public void joinGame(ServerGame sg) throws RemoteException;

}
