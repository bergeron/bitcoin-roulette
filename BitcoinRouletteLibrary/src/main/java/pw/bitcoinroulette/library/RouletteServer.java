package main.java.pw.bitcoinroulette.library;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RouletteServer extends Remote {
	
	public boolean register(String username, String password) throws RemoteException;
	
	public Object[] login(String username, String password) throws RemoteException;
	
}
