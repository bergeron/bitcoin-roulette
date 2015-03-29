package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Lobby extends Remote {
	
	public List<ServerGame> lookupGame(String name, int limit) throws RemoteException;
	
	public ServerGame addGame(String name) throws RemoteException;
	
}
