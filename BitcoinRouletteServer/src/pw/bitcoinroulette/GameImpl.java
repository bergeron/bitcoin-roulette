package pw.bitcoinroulette;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameImpl extends UnicastRemoteObject implements Game {

	private static final long serialVersionUID = -8990600027847804294L;
	private String name;

	protected GameImpl(String name) throws RemoteException {
		super();
		this.name = name;
	}

	@Override
	public double addBet(Player p, Bet b) throws RemoteException {
		return -1;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GameImpl)) {
			return false;
		} else {
			GameImpl other = (GameImpl)o;
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
}
