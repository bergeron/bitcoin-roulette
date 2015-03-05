package main.java.pw.bitcoinroulette.library;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OtherPlayer extends Remote {

	public String getUsername() throws RemoteException;
	
	public Long getPlayerId() throws RemoteException;

}
