package main.java.pw.bitcoinroulette.client.lobby;

public class LobbyRow {

	private String name;
	private String players;
	
	public LobbyRow(String name, String players){
		this.name = name;
		this.players = players;
	}

	public String getName() {
		return name;
	}

	public String getPlayers() {
		return players;
	}
}
