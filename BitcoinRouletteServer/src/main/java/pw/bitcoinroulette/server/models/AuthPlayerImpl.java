package main.java.pw.bitcoinroulette.server.models;

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
import main.java.pw.bitcoinroulette.server.BetImpl;

import com._37coins.bcJsonRpc.BitcoindInterface;

@Entity
@Table(name = "players")
public class AuthPlayerImpl extends OtherPlayerImpl implements AuthPlayer {

	@Transient
	private BitcoindInterface bitcoin;

	@Column(name = "bitcoinaddress", unique = true, nullable = false)
	private String bitcoinAddress;

	@Column(name = "balance", precision=16, scale=8, nullable = false)
	private double balance;

	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(mappedBy = "player")
	private List<TransactionImpl> transactions = new ArrayList<TransactionImpl>();

	public AuthPlayerImpl() {
		super();
	}

	public AuthPlayerImpl(BitcoindInterface bitcoin, String username, String password) {
		super(username);
		this.bitcoinAddress = bitcoin.getnewaddress("roulette");
		this.balance = 0;
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public List<Transaction> getTransactions() {
		List<Transaction> l = new ArrayList<Transaction>();
		for (Transaction t : transactions) {
			l.add((Transaction) t);
		}
		return l;
	}

	public String getBitcoinAddress() {
		return bitcoinAddress;
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
	
	public String getPassword(){
		return this.password;
	}

}
