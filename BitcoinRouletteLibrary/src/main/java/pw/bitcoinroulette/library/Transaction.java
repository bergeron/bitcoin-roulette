package main.java.pw.bitcoinroulette.library;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Transaction extends Remote{

	public long getTransactionId() throws RemoteException;

	public String getTxHash() throws RemoteException;

	public BigDecimal getAmount() throws RemoteException;

	public long getDateEpoch() throws RemoteException;

	public long getConfirmations() throws RemoteException;
}
