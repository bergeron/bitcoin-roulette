package main.java.pw.bitcoinroulette.library;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bet extends Remote {
	
	public int getPayout() throws RemoteException;
	
	public BigDecimal getAmount() throws RemoteException;
	
	public boolean cameTrue(int resultOfSpin) throws RemoteException;
	
	public String getDescription() throws RemoteException;
	
	public OtherPlayer getPlayer() throws RemoteException;
	
}
