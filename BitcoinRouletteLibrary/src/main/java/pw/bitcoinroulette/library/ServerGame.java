package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;


public interface ServerGame extends Remote {

	
	public String getName() throws RemoteException;
	
	public Set<OtherPlayer> getPlayers() throws RemoteException;
	
	public Set<Bet> getBets()  throws RemoteException;
	
}
