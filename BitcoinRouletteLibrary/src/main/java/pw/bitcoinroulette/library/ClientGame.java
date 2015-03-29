package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientGame extends Remote {

	public void spin(int result) throws RemoteException;
	
	public void betAdded(Bet b, OtherPlayer p) throws RemoteException;
}
