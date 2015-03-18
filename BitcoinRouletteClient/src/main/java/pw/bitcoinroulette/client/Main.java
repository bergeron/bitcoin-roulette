package main.java.pw.bitcoinroulette.client;
	
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.InitialContext;

import main.java.pw.bitcoinroulette.client.account.AccountCtrl;
import main.java.pw.bitcoinroulette.client.game.GameCtrl;
import main.java.pw.bitcoinroulette.client.lobby.LobbyCtrl;
import main.java.pw.bitcoinroulette.client.login.LoginCtrl;
import main.java.pw.bitcoinroulette.library.AuthPlayer;
import main.java.pw.bitcoinroulette.library.Lobby;
import main.java.pw.bitcoinroulette.library.RouletteServer;
import main.java.pw.bitcoinroulette.library.ServerGame;


public class Main extends Application {
	public Stage stage;
	public RouletteServer rouletteServer;
	public AuthPlayer authPlayer;
	public ServerGame serverGame;
	
	public Main(){
		try {
			Registry registry = LocateRegistry.getRegistry();
			this.rouletteServer = (RouletteServer)registry.lookup("RouletteServer");
		} catch(RemoteException | NotBoundException e){
			e.printStackTrace();
		}
	}
	
	public void start(Stage stage) {

		this.stage = stage;
		stage.setResizable(false);
	    stage.setWidth(1024);
		stage.setHeight(700);
	    stage.setTitle("Bitcoin Roulette");
	    setLoginScene();
	    stage.show();
	}
	
	private void setLoginScene(){
		
		FXMLLoader loginFxml = new FXMLLoader(getClass().getResource("login/Login.fxml"));
		loginFxml.setController(new LoginCtrl(this));
		Scene loginScene;
		try {
			loginScene = new Scene(loginFxml.load());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		stage.setScene(loginScene);
	}
	
	public void setLobbyScene(Lobby lobby) {
		
		FXMLLoader lobbyFxml = new FXMLLoader(getClass().getResource("lobby/Lobby.fxml"));
		lobbyFxml.setController(new LobbyCtrl(this, lobby));
		Scene lobbyScene;
		try {
			lobbyScene = new Scene(lobbyFxml.load());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		stage.setScene(lobbyScene);
	}
	
	public void setGameScene(ServerGame sg){
		this.serverGame = sg;
		
		FXMLLoader gameFxml = new FXMLLoader(getClass().getResource("game/Game.fxml"));
		gameFxml.setController(new GameCtrl(this, sg));
		Scene gameScene;
		try {
			gameScene = new Scene(gameFxml.load());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		stage.setScene(gameScene);
	}
	
	public void setAccountScene(){
		
	    FXMLLoader accountFxml = new FXMLLoader(getClass().getResource("account/Account.fxml"));
	    accountFxml.setController(new AccountCtrl(this));
	    Scene accountScene;
		try {
			accountScene = new Scene(accountFxml.load());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	    stage.setScene(accountScene);
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
