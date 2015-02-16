package pw.bitcoinroulette;

import java.util.ArrayList;
import java.util.List;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinPaymentListener;


public class PlayerImpl implements BitcoinPaymentListener, Player{
	
	private String username;
	private double balance;
	private String bitcoinAddress;
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private final static int numConfirmations = 1;
	
	public PlayerImpl(String username, String bitcoinAddress){
		this.username = username;
		this.balance = -1;
		this.bitcoinAddress = bitcoinAddress;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getAddress(){
		return bitcoinAddress;
	}
	
	public void block(String blockHash) {
		return; 	/* Dont care about new blocks */
	}
	
	public List<Transaction> getTransactions(){
		return transactions;
	}
	
	private Transaction getAlreadySeenTransaction(Transaction transaction){
		for(Transaction tx : transactions){
			if(tx.txId().equals(transaction.txId())){	/* Adding confirmations. */
				return tx;
			}
		}
		
		return transaction;
	}
	
	 /* New transaction has arrived	or added confirmations */
	public void transaction(Transaction transaction) {
		
		Transaction tx = getAlreadySeenTransaction(transaction);
		
		switch (tx.confirmations()){
			case 0:
				transactions.add(transaction);
				break;
			case numConfirmations:
				balance += tx.confirmations();
				break;
		}
		
	}

	public String getUsername() {
		return username;
	}
}
