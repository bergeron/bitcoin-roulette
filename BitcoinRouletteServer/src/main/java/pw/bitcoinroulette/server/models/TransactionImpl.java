package main.java.pw.bitcoinroulette.server.models;

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

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "transactionId", unique = true, nullable = false)
	private long transactionId;

	@ManyToOne()
	@JoinColumn(nullable = false)
	private AuthPlayerImpl player;

	@Column(name = "hash", unique = true, nullable = false, length = 255)
	private String hash;

	@Column(name = "amount", precision=16, scale=8, nullable = false)
	private BigDecimal amount;

	@Column(name = "confirmations")
	private long confirmations;

	@Column(name = "dateEpoch", nullable = false)
	private long dateEpoch;

	public TransactionImpl() {
		super();
	}

	public TransactionImpl(String hash, BigDecimal amount, long confirmations, long dateEpoch, AuthPlayerImpl player) {
		this.hash = hash;
		this.amount = amount;
		this.confirmations = confirmations;
		this.dateEpoch = dateEpoch;
		this.player = player;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public String getTxHash() {
		return hash;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public long getDateEpoch() {
		return dateEpoch;
	}

	public long getConfirmations() {
		return confirmations;
	}

}
