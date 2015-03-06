package main.java.pw.bitcoinroulette.server.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import main.java.pw.bitcoinroulette.library.OtherPlayer;

@MappedSuperclass
public class OtherPlayerImpl implements OtherPlayer {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "playerId", unique = true, nullable = false)
	private Long playerId;
	
	@Column(name = "username", unique = true, nullable = false, length = 255)
	private String username;
	
	public OtherPlayerImpl(){
		super();
	}
	
	public OtherPlayerImpl(String username){
		this.username = username;
	}
	
	public Long getPlayerId() {
		return playerId;
	}

	public String getUsername() {
		return username;
	}
}
