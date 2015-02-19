package pw.bitcoinroulette;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

public class Main {

	/**
	 * Schema:
	 * 		 create table players(id serial primary key, username varchar(255), bitcoinAddress varchar(255));

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

		/* Connect to database */
		String driver = "org.postgresql.Driver";
		String url = String.format("jdbc:postgresql://localhost:5432/BitcoinRoulette?user=postgres&password=%s",
				dbPassword);

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		Connection db;
		try {
			db = DriverManager.getConnection(url);
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Connected to db");

		/* Connect to Bitcoin RPC */
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(bitcoinRPCuser, bitcoinRPCpassword.toCharArray());
			}
		});

		BitcoinJSONRPCClient bitcoin;
		try {
			bitcoin = new BitcoinJSONRPCClient("http://localhost:" + 8332);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Connected to Bitcoin client");
		new Thread(new BitcoinListener(bitcoin)).start();

		/* Export RMI */

		// TODO !!!
		// if (System.getSecurityManager() == null) {
		// System.setSecurityManager(new SecurityManager());
		// }

		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
			System.out.printf("RMI registry running on port %d\n", Registry.REGISTRY_PORT);
		} catch (RemoteException e1) {
			System.err.printf("Exception starting RMI registry on port %d\n", Registry.REGISTRY_PORT);
			e1.printStackTrace();
		}

		try {
			RouletteServer rouletteServer = new RouletteServerImpl(db, bitcoin);
			RouletteServer stub = (RouletteServer) UnicastRemoteObject.exportObject(rouletteServer, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("RouletteServer", stub);
			System.out.println("RouletteServer bound");
		} catch (Exception e) {
			System.err.println("Exception binding RouletteServer:");
			e.printStackTrace();
		}
		
		
		
		
		//Test
		
         Configuration configuration = new Configuration();
         configuration.configure("pw/bitcoinroulette/hibernate/hibernate.cfg.xml");
         StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
         SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
         Session session = sessionFactory.openSession();
         
         Player p = new PlayerImpl("Sdaf", "2345");
         
         session.beginTransaction();
         session.save(p);
         session.getTransaction().commit();
		
	}
}
