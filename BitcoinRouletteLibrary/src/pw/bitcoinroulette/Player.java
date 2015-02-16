package pw.bitcoinroulette;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;

public interface Player extends Remote {

	public double getBalance() throws RemoteException;

	public String getAddress() throws RemoteException;

	public List<Transaction> getTransactions() throws RemoteException;

	public String getUsername() throws RemoteException;

}
