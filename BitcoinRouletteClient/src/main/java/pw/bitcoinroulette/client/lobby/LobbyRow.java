package main.java.pw.bitcoinroulette.client.lobby;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import main.java.pw.bitcoinroulette.client.Main;
import main.java.pw.bitcoinroulette.library.OtherPlayer;
import main.java.pw.bitcoinroulette.library.ServerGame;

public class LobbyRow {

	private String name;
	private String players;
	private Button joinBtn;
	
	public LobbyRow(Main main, ServerGame sg){

		this.joinBtn = new Button("Join");
		this.joinBtn.setOnMouseClicked((e) -> main.setGameScene(sg));
		
		try{
			this.name = sg.getName();
			
			List<String> playerNames = new ArrayList<String>();
			for(OtherPlayer p : sg.getPlayers()){
				playerNames.add(p.getUsername());
			}
			players = String.join(",", playerNames);
			
		} catch (RemoteException e){
			e.printStackTrace();
		}
		
	}

	public String getName() {
		return name;
	}

	public String getPlayers() {
		return players;
	}
	
	public Button getJoinBtn(){
		return joinBtn;
	}
	
}
