package main.java.pw.bitcoinroulette.client.lobby;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;


public class JoinButton extends TableCell<LobbyRow, Boolean> {
	
	final Button cellButton = new Button("Delete");

	public JoinButton() {
		cellButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {

				System.out.println("CLiCked!");
			}
		});
	}

	// Display button if the row is not empty
	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			setGraphic(cellButton);
		}
	}
}
