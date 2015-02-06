package pw.bitcoinroulette;


import java.net.MalformedURLException;
import java.util.ArrayList;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

public class Core {
	
	private BitcoinJSONRPCClient bitcoin;
	private ArrayList<PlayerImpl> playerImpls;
	private BitcoinListener bitcoinListener;

	public Core(){
		
//		Auth.authenticate();
//		
//		try {
//			bitcoin = new BitcoinJSONRPCClient("http://localhost:"+ 18332);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			return;
//		}
//		
//		
//		bitcoinListener = new BitcoinListener(bitcoin);
//		new Thread(bitcoinListener).start();
		playerImpls = new ArrayList<PlayerImpl>();
	}
	
	public PlayerImpl getCurrentAccount(){
		return playerImpls.get(0);
	}

	public BitcoinJSONRPCClient getBitcoin(){
		return bitcoin;
	}

	public void addAccount() throws BitcoinException {
//		String address = bitcoin.getNewAddress("roulette");
		String address = "ASdfasdf";
		PlayerImpl playerImpl = new PlayerImpl("username", address);
		playerImpls.add(playerImpl);
//		bitcoinListener.addListener(address,account);
	}
}
