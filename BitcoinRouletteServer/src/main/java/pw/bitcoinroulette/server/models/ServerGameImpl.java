package main.java.pw.bitcoinroulette.server.models;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.OtherPlayer;
import main.java.pw.bitcoinroulette.library.ServerGame;


@Entity
@Table(name = "games")
public class ServerGameImpl extends UnicastRemoteObject implements ServerGame {

	private static final long serialVersionUID = -6114105589582888658L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "gameId", unique = true, nullable = false)	
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<AuthPlayerImpl> players = new HashSet<AuthPlayerImpl>();
	
	@OneToMany(mappedBy = "serverGame")
	private Set<BetImpl> bets = new HashSet<BetImpl>();
	
	public ServerGameImpl() throws RemoteException{
	}
	
	public ServerGameImpl(String name) throws RemoteException {
		super();
		this.name = name;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
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

	public Set<OtherPlayer> getPlayers() {
		Set<OtherPlayer> s = new HashSet<OtherPlayer>();
		for(AuthPlayerImpl a : players){
			s.add(a);
		}
		return s;
	}

	public Long getId() {
		return id;
	}

	public Set<Bet> getBets() {
		Set<Bet> s = new HashSet<Bet>();
		for(BetImpl b : bets){
			s.add(b);
		}
		return s;
	}
}
