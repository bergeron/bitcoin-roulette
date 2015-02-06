package pw.bitcoinroulette;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class LobbyImpl extends UnicastRemoteObject implements Lobby {

	private static final long serialVersionUID = -3245535716307120524L;
	private HashSet<Game> games = new HashSet<Game>();

	protected LobbyImpl() throws RemoteException {
		super();
	}

	public Game lookupGame(String name) throws RemoteException {
		for (Game g : games) {
			if (g.getName().equals(name)) {
				return g;
			}
		}

		throw new RemoteException("No game found");
	}

	@Override
	public Game addGame(String name) throws RemoteException {
		for (Game g : games) {
			if (g.getName().equals(name)) {
				throw new RemoteException("Game already exists");
			}
		}
		
		Game g = new GameImpl(name);
		games.add(g);
		return g;
	}
	
}
