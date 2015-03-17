package main.java.pw.bitcoinroulette.client.lobby;


import java.math.BigDecimal;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class LobbyCtrl {

	private Main main;
	
	@FXML public TableView<LobbyRow> tableView;
	@FXML public TableColumn<LobbyRow, String> nameCol;
	@FXML public TableColumn<LobbyRow, String> playersCol;
	@FXML public TableColumn<LobbyRow, Boolean> joinCol;
	@FXML public Button searchBtn;
	@FXML public Button createBtn;
	@FXML public TextField searchText;
	@FXML public TextField createText;

	public LobbyCtrl(Main main, Lobby lobby) {
		this.main = main;
	}

	public void initialize() {
		
		nameCol.setCellValueFactory(new PropertyValueFactory<LobbyRow, String>("name"));
		playersCol.setCellValueFactory(new PropertyValueFactory<LobbyRow, String>("players"));
		
		/* Join buttons */
		joinCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<LobbyRow, Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<LobbyRow, Boolean> p) {
				return new SimpleBooleanProperty(p.getValue() != null);
			}
		});
 
		joinCol.setCellFactory(new Callback<TableColumn<LobbyRow, Boolean>, TableCell<LobbyRow, Boolean>>() {
			@Override
			public TableCell<LobbyRow, Boolean> call(TableColumn<LobbyRow, Boolean> p) {
				return new JoinButton();
			}
		});
        
		
		searchBtn.setOnMouseClicked((e) -> search(searchText.getText()));
		createBtn.setOnMouseClicked((e) -> create(createText.getText()));
	}
	
	private void search(String name) {
		System.out.println("Search " + name);
		
//		tableView.setPlaceholder(new Text("sdfsadfsdf"));
		
		ObservableList<LobbyRow> rows = tableView.getItems();
		rows.clear();
		
		LobbyRow lr = new LobbyRow("some name", "p1, p2, p3");
		rows.add(lr);
		rows.add(lr);
		
	}
	
	private void create(String name){
		System.out.println("Create " + name);
	}
}
