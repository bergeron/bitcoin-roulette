package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bet extends Remote {
	
	public double getAmount() throws RemoteException;
	
	public boolean cameTrue(int resultOfSpin) throws RemoteException;
	
	public String getDescription() throws RemoteException;
	
	public OtherPlayer getPlayer() throws RemoteException;
	
}
