package main.java.pw.bitcoinroulette.client.game.listeners;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.java.pw.bitcoinroulette.client.game.GameCtrl;

public class SpinFinishedListener implements EventHandler<ActionEvent> {
	private int result;
	private GameCtrl gameCtrl;

	public SpinFinishedListener(GameCtrl gameCtrl, int result) {
		this.gameCtrl = gameCtrl;
		this.result = result;
	}

	@Override
	public void handle(ActionEvent event) {
		//TODO;
		
	}

}
