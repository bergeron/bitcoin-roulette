package pw.bitcoinroulette;



import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

public class RouletteServerImpl implements RouletteServer{
	
	private Connection db;
	private BitcoinJSONRPCClient bitcoin;

	protected RouletteServerImpl(Connection db, BitcoinJSONRPCClient bitcoin) throws RemoteException {
		super();
		this.db = db;
		this.bitcoin = bitcoin;
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		
		/* Check username availability */
		String sql = String.format("select count(*) as count from players where username='%s'", username);
		System.out.println(sql);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(sql);
			if(rs.next()){
				boolean available = (rs.getInt("count") == 0);
				System.out.printf("%s is" + (available ? "" : " not") + " available", username);
				if(!available){
					throw new RemoteException("Username Taken");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("Registration failed");
		}

		/* Hash/salt password */
		String hashedPassword;
		try {
			hashedPassword = PasswordHash.createHash(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			throw new RemoteException("Registration failed");
		}
		
		/* Insert into db */
		sql = String.format("insert into players(username, password) values ('%s', '%s')", username, hashedPassword);
		System.out.println(sql);
		
		try {
			db.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RemoteException("Registration failed");
		}
		
		return true;
	}


	@Override
	public Object[] login(String username, String password) throws RemoteException {
		
		String sql = String.format("select password from players where username='%s'", username);
		System.out.println(sql);
		
		try {
			ResultSet rs = db.createStatement().executeQuery(sql);
			if(!rs.next()){
				throw new RemoteException("No user found");
			} else {
				String correctHash = rs.getString("password");
				if(!PasswordHash.validatePassword(password, correctHash)){
					throw new RemoteException("Wrong password");
				}
			}
		} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		System.out.println(username + " logged in");
		return new Object[]{null, new LobbyImpl()};
	}

	@Override
	public String withdraw(Player p, String address, double amount) throws RemoteException {
		
		try {
			if (!bitcoin.validateAddress(address).isValid())
				throw new BitcoinException();
		} catch (BitcoinException e) {
			e.printStackTrace();
			//TODO
			return "TODO";
		}

		/* Amount and address valid */
		try {
			double balance = p.getBalance();
			if (amount > balance) {
				//TODO
				return "TODO";
			}

			String txHash = bitcoin.sendToAddress(address, amount);
			return txHash;

		} catch (BitcoinException e) {
			e.printStackTrace();
			return "TODO";//TODO
		}
	}
}
