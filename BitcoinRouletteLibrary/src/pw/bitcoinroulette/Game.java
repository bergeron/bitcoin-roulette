package pw.bitcoinroulette;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Game extends Remote {

	public String addBet(Bet b) throws RemoteException;
	
	public String getName() throws RemoteException;
	
}

