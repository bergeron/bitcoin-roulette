package pw.bitcoinroulette;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	/**
	 * First: start derby: sudo java -jar $DERBY_HOME/lib/derbyrun.jar server start
	 * 
	 * SCHEMA:
	 * 		create table players(username varchar(255), password varchar(255));
	 * 
	 * 
	 */
	public static void main(String[] args) {
		Connection db = connectDB();
		exportRMI(db);
	}

	public static Connection connectDB() {
		String driver = "org.apache.derby.jdbc.ClientDriver";

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		String dbName = "bitcoinRoulette";
		String connectionURL = "jdbc:derby://localhost:1527/" + dbName + ";";

		Connection conn = null;
		
		/* Connect to DB */
		try {
			conn = DriverManager.getConnection(connectionURL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Connected to database " + dbName);
		
		return conn;  
	}

	public static void exportRMI(Connection db) {

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
			RouletteServer rouletteServer = new RouletteServerImpl(db);
			RouletteServer stub = (RouletteServer) UnicastRemoteObject.exportObject(rouletteServer, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind("RouletteServer", stub);
			System.out.println("RouletteServer bound");
		} catch (Exception e) {
			System.err.println("Exception binding RouletteServer:");
			e.printStackTrace();
		}
	}
}
