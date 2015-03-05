package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;

public interface AuthPlayer extends Remote, OtherPlayer{

	public double getBalance() throws RemoteException;
	
	public String getBitcoinAddress() throws RemoteException;
	
	public List<Transaction> getTransactionsInterface() throws RemoteException;

	public Bet makeBet(ServerGame g, double amount, int payout, HashSet<Integer> winning, String description) throws RemoteException;

	String withdraw(String address, double amount) throws RemoteException;

}
