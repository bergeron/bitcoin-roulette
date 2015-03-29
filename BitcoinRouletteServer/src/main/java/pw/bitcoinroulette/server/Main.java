package main.java.pw.bitcoinroulette.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import main.java.pw.bitcoinroulette.library.LoginServer;
import main.java.pw.bitcoinroulette.library.ServerGame;
import main.java.pw.bitcoinroulette.server.bitcoin.NewBlockListener;
import main.java.pw.bitcoinroulette.server.bitcoin.NewTransactionListener;
import main.java.pw.bitcoinroulette.server.models.AuthPlayerImpl;
import main.java.pw.bitcoinroulette.server.models.ServerGameImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com._37coins.bcJsonRpc.BitcoindClientFactory;
import com._37coins.bcJsonRpc.BitcoindInterface;
import com._37coins.bcJsonRpc.events.BlockListener;
import com._37coins.bcJsonRpc.events.WalletListener;

public class Main {

	/*
	 * bitcoind -blocknotify="echo '%s' | nc 127.0.0.1 4001" -walletnotify="echo '%s' | nc 127.0.0.1 4002" -alertnotify="echo '%s' | nc 127.0.0.1 4003" -daemon
	 */
	public static void main(String[] args) {

		Scanner cin;
		try {
			cin = new Scanner(new File("Auth.priv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		final String dbPassword = cin.nextLine();
		final String bitcoinRPCuser = cin.nextLine();
		final String bitcoinRPCpassword = cin.nextLine();
		cin.close();
		
		/* Connect Hibernate to db */
		Configuration configuration = new Configuration();
		configuration.configure("main/java/pw/bitcoinroulette/server/hibernate.cfg.xml");
		StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
		
		AuthPlayerImpl.sessionFactory = sessionFactory;
		
		/* Connect to bitcoind */
		BitcoindInterface bitcoin;
		try {
			bitcoin = new BitcoindClientFactory(new URL("http://localhost:18332/"), bitcoinRPCuser, bitcoinRPCpassword)
					.getClient();
			new WalletListener(bitcoin).addObserver(new NewTransactionListener(sessionFactory));
			new BlockListener(bitcoin).addObserver(new NewBlockListener(sessionFactory));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		AuthPlayerImpl.bitcoin = bitcoin;
		System.out.println("Connected to bitcoin");

		/* Export RMI */
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			System.out.printf("RMI registry running on port %d\n", Registry.REGISTRY_PORT);
		} catch (RemoteException e1) {
			System.err.printf("Exception starting RMI registry on port %d\n", Registry.REGISTRY_PORT);
			e1.printStackTrace();
		}

		try {
			LoginServer loginServer = new LoginServerImpl(bitcoin, sessionFactory);
			LoginServer stub = (LoginServer) UnicastRemoteObject.exportObject(loginServer, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("RouletteServer", stub);
			System.out.println("RouletteServer bound");
		} catch (Exception e) {
			System.err.println("Exception binding RouletteServer:");
			e.printStackTrace();
		}
		
	}
}
