package main.java.pw.bitcoinroulette.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import main.java.pw.bitcoinroulette.library.Lobby;
import main.java.pw.bitcoinroulette.library.ServerGame;
import main.java.pw.bitcoinroulette.server.models.ServerGameImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class LobbyImpl extends UnicastRemoteObject implements Lobby {

	private static final long serialVersionUID = -3245535716307120524L;
	
	private SessionFactory sessionFactory;

	protected LobbyImpl() throws RemoteException {
		super();
	}
	
	public LobbyImpl(SessionFactory sessionFactory) throws RemoteException{
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Return first <limit> games matching <name>
	 */
	@Override
	public List<ServerGame> lookupGame(String name, int limit) throws RemoteException {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<ServerGame> games = (List<ServerGame>)session
			.createCriteria(ServerGameImpl.class)
			.add(Restrictions.ilike("name", name, MatchMode.ANYWHERE))
			.setMaxResults(limit)
			.list();
		
		session.getTransaction().commit();
		session.close();
		
		return games;
	}

	@Override
	public ServerGame addGame(String name) throws RemoteException {
		
		ServerGameImpl g = new ServerGameImpl(name);
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(g);
		session.getTransaction().commit();
		session.close();
		
		return g;
	}
	
}
