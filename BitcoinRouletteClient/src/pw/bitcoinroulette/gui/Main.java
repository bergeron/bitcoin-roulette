package pw.bitcoinroulette.gui;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pw.bitcoinroulette.Game;
import pw.bitcoinroulette.Lobby;
import pw.bitcoinroulette.core.Core;
import pw.bitcoinroulette.gui.account.AccountCtrl;
import pw.bitcoinroulette.gui.game.GameCtrl;
import pw.bitcoinroulette.gui.lobby.LobbyCtrl;


public class Main extends Application {
	public Scene lobbyScene;
	public Scene gameScene;
	public Scene accountScene;
	public Stage stage;
	public Core core;
	
	public Main(){
		this.core = new Core();
	}
	
	public void start(Stage stage) throws Exception {
		
		/* Test */
		Lobby lobby = core.rouletteServer.login("ass", "hole");
//		lobby.addGame("game");
//		Game g = lobby.lookupGame("game");
//		System.out.println(g.addBet(null));
//		lobby.lookupGame("gamenothere");
		
		this.stage = stage;
		stage.setResizable(false);
		
		/* Game Screen */
		FXMLLoader lobbyFxml = new FXMLLoader(getClass().getResource("lobby/Lobby.fxml"));
		lobbyFxml.setController(new LobbyCtrl(this, lobby));
		lobbyScene = new Scene(lobbyFxml.load());
		
//		/* Game Screen */
//		FXMLLoader gameFxml = new FXMLLoader(getClass().getResource("game/Game.fxml"));
//		gameFxml.setController(new GameCtrl(this));
//		gameScene = new Scene(gameFxml.load());
//		
//		/* Account Screen */
//	    FXMLLoader accountFxml = new FXMLLoader(getClass().getResource("account/Account.fxml"));
//	    accountFxml.setController(new AccountCtrl(this));
//	    accountScene = new Scene(accountFxml.load());
	    
	    stage.setWidth(1024);
		stage.setHeight(700);
	    stage.setTitle("Bitcoin Roulette");
	    stage.setScene(lobbyScene);
	    stage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
