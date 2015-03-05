package main.java.pw.bitcoinroulette.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

import main.java.pw.bitcoinroulette.library.Lobby;
import main.java.pw.bitcoinroulette.library.ServerGame;

public class LobbyImpl extends UnicastRemoteObject implements Lobby {

	private static final long serialVersionUID = -3245535716307120524L;
	private HashSet<ServerGame> serverGames = new HashSet<ServerGame>();

	protected LobbyImpl() throws RemoteException {
		super();
	}

	public ServerGame lookupGame(String name) throws RemoteException {
		for (ServerGame g : serverGames) {
			if (g.getName().equals(name)) {
				return g;
			}
		}

		throw new RemoteException("No game found");
	}

	@Override
	public ServerGame addGame(String name) throws RemoteException {
		for (ServerGame g : serverGames) {
			if (g.getName().equals(name)) {
				throw new RemoteException("Game already exists");
			}
		}
		
		ServerGame g = new ServerGameImpl(name);
		serverGames.add(g);
		return g;
	}
	
}
