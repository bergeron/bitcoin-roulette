package main.java.pw.bitcoinroulette.server.models;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import main.java.pw.bitcoinroulette.library.AuthPlayer;
import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.ServerGame;
import main.java.pw.bitcoinroulette.library.Transaction;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com._37coins.bcJsonRpc.BitcoindInterface;

@Entity
@Table(name = "players")
public class AuthPlayerImpl extends OtherPlayerImpl implements AuthPlayer {

	private static final long serialVersionUID = 2772558738676419928L;

	@Transient
	public static BitcoindInterface bitcoin;
	
	@Transient
	public static SessionFactory sessionFactory;

	@Column(name = "bitcoinaddress", unique = true, nullable = false)
	private String bitcoinAddress;

	@Column(name = "balance", precision=16, scale=8, nullable = false)
	private BigDecimal balance;

	@Column(name = "password", nullable = false)
	private String password;

	@OneToMany(mappedBy = "player", fetch=FetchType.EAGER)
	private Set<TransactionImpl> transactions = new HashSet<TransactionImpl>();
	
	@OneToMany(mappedBy = "player", fetch=FetchType.EAGER)
	private Set<BetImpl> bets = new HashSet<BetImpl>();
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<ServerGameImpl> games = new HashSet<ServerGameImpl>();

	public AuthPlayerImpl() throws RemoteException {
		super();
	}

	public AuthPlayerImpl(String username, String password) throws RemoteException {
		super(username);
		this.bitcoinAddress = bitcoin.getnewaddress("roulette");
		this.balance = BigDecimal.ZERO;
		this.password = password;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public List<Transaction> getTransactions() {
		List<Transaction> l = new ArrayList<Transaction>();
		for (Transaction t : transactions) {
			l.add(t);
		}
		return l;
	}

	public String getBitcoinAddress() {
		return bitcoinAddress;
	}

	public String withdraw(String address, BigDecimal amount) throws RemoteException {
		
		if (!bitcoin.validateaddress(address).isIsvalid()) {
			throw new RemoteException("Invalid address");
		} else if(balance.compareTo(amount) < 0){
			throw new RemoteException("Insufficient funds");
		}

		String hash = bitcoin.sendtoaddress(address, amount);
		System.out.println(hash);
		
		return "hash";
	}

	public Bet makeBet(ServerGame g, BigDecimal amount, int payout, HashSet<Integer> winning, String description)
			throws RemoteException {
		
		BetImpl b = new BetImpl(amount, payout, winning, description);
		bets.add(b);
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(b);
		session.update(this);
		session.getTransaction().commit();
		session.close();
		
		return b;
	}
	
	public String getPassword(){
		return this.password;
	}

	@Override
	public void joinGame(ServerGame sg) throws RemoteException {
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		ServerGameImpl game = (ServerGameImpl)session
			.createCriteria(ServerGameImpl.class)
			.add(Restrictions.idEq(sg.getId()))
			.setMaxResults(1)
			.uniqueResult();
		
		if(games.contains(game)){
			System.out.println("Already in game");
		} else {
			games.add(game);
			session.update(this);
		}
		
		session.getTransaction().commit();
		session.close();
	}
}
