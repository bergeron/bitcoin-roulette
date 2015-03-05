package main.java.pw.bitcoinroulette.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import main.java.pw.bitcoinroulette.library.AuthPlayer;
import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.ServerGame;


public class ServerGameImpl extends UnicastRemoteObject implements ServerGame {

	private static final long serialVersionUID = -8990600027847804294L;
	private Long id;
	private String name;
	private Set<AuthPlayer> authPlayers = new HashSet<AuthPlayer>();
	private Set<Bet> bets = new HashSet<Bet>();
	
	public ServerGameImpl() throws RemoteException{
	}
	
	protected ServerGameImpl(String name) throws RemoteException {
		super();
		this.name = name;
	}



	@Override
	public String getName() throws RemoteException {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ServerGameImpl)) {
			return false;
		} else {
			ServerGameImpl other = (ServerGameImpl)o;
			try {
				return other.getName().equals(name);
			} catch (RemoteException e) {
				return false;
			}
		}
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public Set<AuthPlayer> getPlayers() {
		return authPlayers;
	}

	public void setPlayers(Set<AuthPlayer> authPlayers) {
		this.authPlayers = authPlayers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Bet> getBets() {
		return bets;
	}

	public void setBets(Set<Bet> bets) {
		this.bets = bets;
	}
}
