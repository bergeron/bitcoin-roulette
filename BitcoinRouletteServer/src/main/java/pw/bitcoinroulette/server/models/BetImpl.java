package main.java.pw.bitcoinroulette.server.models;


import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.OtherPlayer;


@Entity
@Table(name = "bets")
public class BetImpl implements Bet {
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "betId", unique = true, nullable = false)
	private long betId;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private AuthPlayerImpl player;
	
	@ManyToOne()
	@JoinColumn(nullable = false)
	private ServerGameImpl serverGame;
	
	@Column(name = "payout")
	private int payout;
	
	@Column(name = "amount", precision=16, scale=8, nullable = false)
	private BigDecimal amount;
	
	@ElementCollection
	@Column(name="winning")
	private Set<Integer> winning;
	
	@Column(name = "description")
	private String description;
	

	public BetImpl(BigDecimal amount, int payout, Set<Integer> winning, String description){
		this.payout = payout;
		this.amount = amount;
		this.winning = winning;
		this.description = description;
	}
	
	public int getPayout(){
		return payout;
	}
	
	public BigDecimal getAmount(){
		return amount;
	}
	
	public boolean cameTrue(int resultOfSpin){
		return winning.contains(resultOfSpin);
	}

	public String getDescription() {
		return description;
	}

	public OtherPlayer getPlayer() {
		return player;
	}
	
	public ServerGameImpl getServerGame(){
		return serverGame;
	}

}

