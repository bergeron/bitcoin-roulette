package pw.bitcoinroulette.core;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import pw.bitcoinroulette.Game;
import pw.bitcoinroulette.Player;
import pw.bitcoinroulette.RouletteServer;


public class Core {
	
	public RouletteServer rouletteServer;
	public Player player;
	public Game game;
	
	
	public Core(){
		
		try {
			Registry registry = LocateRegistry.getRegistry();
			this.rouletteServer = (RouletteServer)registry.lookup("RouletteServer");
		} catch(RemoteException | NotBoundException e){
			e.printStackTrace();
		}
	}
	
}
