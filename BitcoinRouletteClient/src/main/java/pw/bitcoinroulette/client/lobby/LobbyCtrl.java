package main.java.pw.bitcoinroulette.client.lobby;



import java.rmi.RemoteException;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import main.java.pw.bitcoinroulette.client.Main;
import main.java.pw.bitcoinroulette.library.Lobby;
import main.java.pw.bitcoinroulette.library.ServerGame;

public class LobbyCtrl {

	private Main main;
	private Lobby lobby;
	
	@FXML public TableView<LobbyRow> tableView;
	@FXML public TableColumn<LobbyRow, String> nameCol;
	@FXML public TableColumn<LobbyRow, String> playersCol;
	@FXML public TableColumn<LobbyRow, Button> joinCol;
	@FXML public Button searchBtn;
	@FXML public Button createBtn;
	@FXML public TextField searchText;
	@FXML public TextField createText;

	public LobbyCtrl(Main main, Lobby lobby) {
		this.main = main;
		this.lobby = lobby;
	}

	public void initialize() {
		
		nameCol.setCellValueFactory(new PropertyValueFactory<LobbyRow, String>("name"));
		playersCol.setCellValueFactory(new PropertyValueFactory<LobbyRow, String>("players"));
		joinCol.setCellValueFactory(new PropertyValueFactory<LobbyRow, Button>("joinBtn"));
		
		searchBtn.setOnMouseClicked((e) -> search(searchText.getText()));
		createBtn.setOnMouseClicked((e) -> create(createText.getText()));
	}
	
	private void search(String name) {
		ObservableList<LobbyRow> rows = tableView.getItems();
		rows.clear();
		
		List<ServerGame> serverGames;
		try {
			serverGames = lobby.lookupGame(name, 8);
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		
		for (ServerGame sg : serverGames) {
			LobbyRow lr = new LobbyRow(main, lobby, sg);
			rows.add(lr);
		}
	}
	
	private void create(String name){
		try {
			ServerGame g = lobby.addGame(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
