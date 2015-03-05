package main.java.pw.bitcoinroulette.client.game.listeners;


import java.util.Arrays;
import java.util.List;

import main.java.pw.bitcoinroulette.client.game.Coord;
import main.java.pw.bitcoinroulette.client.game.GameCtrl;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class SelectorEnterListener implements EventHandler<Event>{

	private GameCtrl gameCtrl;
	private List<Integer> blackNums = Arrays.asList(1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36);
	
	public SelectorEnterListener(GameCtrl gameCtrl) {
		this.gameCtrl = gameCtrl;
	}
	
	@Override
	public void handle(Event event) {
		Pane p = (Pane)event.getSource();
		int row = GridPane.getRowIndex(p);
		int col = GridPane.getColumnIndex(p);
		
		Coord[] selection = gameCtrl.coordToSelection.get(new Coord(row, col));
		if(selection != null){
			for(Coord c : selection){
				Node n = gameCtrl.getPaneFromCoord(c.row, c.col);
				
				if(blackNums.contains(gameCtrl.coordToNumber.get(c))){
					n.setStyle("-fx-background-color: black;\n");
					n.setStyle("-fx-border-color: white;\n");
				} else {
					ColorAdjust colorAdjust = new ColorAdjust();
					colorAdjust.setBrightness(0.2);
					n.setEffect(colorAdjust);
					n.setStyle("-fx-border-color: white;\n");
				}
			}
		}
	}
}
