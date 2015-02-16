package pw.bitcoinroulette;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Game extends Remote {

	public double addBet(Player p, Bet b) throws RemoteException;
	
	public String getName() throws RemoteException;
	
}
