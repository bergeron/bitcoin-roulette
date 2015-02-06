package pw.bitcoinroulette;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Lobby extends Remote {
	
	public Game lookupGame(String name) throws RemoteException;
	
	public Game addGame(String name) throws RemoteException;

}
