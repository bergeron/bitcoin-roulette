package main.java.pw.bitcoinroulette.server;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import main.java.pw.bitcoinroulette.library.Transaction;

@Entity
@Table(name = "transactions")
public class TransactionImpl implements Transaction {
	
	private long transactionId;
	private String hash;
	private BigDecimal amount;
	private long confirmations;
	private long dateEpoch;
	private AuthPlayerImpl player;
	
	public TransactionImpl(){
		super();
	}
	
	public TransactionImpl(String hash, BigDecimal amount, long confirmations, long dateEpoch, AuthPlayerImpl player) {
		this.hash = hash;
		this.amount = amount;
		this.confirmations = confirmations;
		this.dateEpoch = dateEpoch;
		this.player = player;
	}
	

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "transactionId", unique = true, nullable = false)
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "hash", unique = true, nullable = false, length = 255)
	public String getTxHash() {
		return hash;
	}

	public void setTxHash(String txHash) {
		this.hash = txHash;
	}

	@Column(name = "amount", nullable = false)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "dateEpoch")
	public long getDateEpoch() {
		return dateEpoch;
	}

	public void setDateEpoch(long dateEpoch) {
		this.dateEpoch = dateEpoch;
	}

	@ManyToOne()
	@JoinColumn(nullable = false)
	public AuthPlayerImpl getPlayer() {
		return player;
	}

	public void setPlayer(AuthPlayerImpl player) {
		this.player = player;
	}

	@Column(name = "confirmations")
	public long getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(long confirmations) {
		this.confirmations = confirmations;
	}
}
