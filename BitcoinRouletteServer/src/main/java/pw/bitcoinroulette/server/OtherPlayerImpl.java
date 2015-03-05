package main.java.pw.bitcoinroulette.server;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import main.java.pw.bitcoinroulette.library.OtherPlayer;

@MappedSuperclass
public class OtherPlayerImpl implements OtherPlayer {

	private Long playerId;
	private String username;
	
	public OtherPlayerImpl(){
		
	}
	
	public OtherPlayerImpl(String username){
		this.username = username;
	}
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "playerId", unique = true, nullable = false)
	public Long getPlayerId() {
		return playerId;
	}

	public void setplayerId(long playerId) {
		this.playerId = playerId;
	}

	
	@Column(name = "username", unique = true, nullable = false, length = 255)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
