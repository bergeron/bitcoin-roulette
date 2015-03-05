package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Lobby extends Remote {
	
	public ServerGame lookupGame(String name) throws RemoteException;
	
	public ServerGame addGame(String name) throws RemoteException;

}
