package main.java.pw.bitcoinroulette.server;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import main.java.pw.bitcoinroulette.library.AuthPlayer;
import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.ServerGame;
import main.java.pw.bitcoinroulette.library.Transaction;

import com._37coins.bcJsonRpc.BitcoindInterface;

@Entity
@Table(name = "players")
public class AuthPlayerImpl extends OtherPlayerImpl implements AuthPlayer {

	private BitcoindInterface bitcoin;
	private String password;
	private double balance;
	private String bitcoinAddress;
	private List<TransactionImpl> transactions = new ArrayList<TransactionImpl>();
	
	public AuthPlayerImpl(){
		super();
	}

	public AuthPlayerImpl(BitcoindInterface bitcoin, String username, String password) {
		super(username);
		this.password = password;
		this.balance = -1;

		String bitcoinAddress = bitcoin.getnewaddress("roulette");
		this.setBitcoinAddress(bitcoinAddress);
	}

	
	@Column(name="balance")
	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	@OneToMany(mappedBy = "player")
	public List<TransactionImpl> getTransactions() {
		return transactions;
	}
	
	public void setTransactions(List<TransactionImpl> transactions) {
		this.transactions = transactions;
	}

	@Transient
	@Override
	public List<Transaction> getTransactionsInterface() {
		List<Transaction> l = new ArrayList<Transaction>();
		for(Transaction t: transactions){
			l.add((Transaction)t);
		}
		return l;
	}
	
	@Column(name="bitcoinaddress")
	public String getBitcoinAddress() {
		return bitcoinAddress;
	}

	private void setBitcoinAddress(String bitcoinAddress) {
		this.bitcoinAddress = bitcoinAddress;
	}

	@Column(name="password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String withdraw(String address, double amount) {

		if (!bitcoin.validateaddress(address).isIsvalid()) {
			return "Invalid address";
		}

		if (amount > balance) {
			return "Insufficient funds";
		}
		
		return bitcoin.sendtoaddress(address, new BigDecimal(amount));

	}

	public Bet makeBet(ServerGame g, double amount, int payout, HashSet<Integer> winning, String description)
			throws RemoteException {
		return new BetImpl(amount, payout, winning, description);
	}

}
