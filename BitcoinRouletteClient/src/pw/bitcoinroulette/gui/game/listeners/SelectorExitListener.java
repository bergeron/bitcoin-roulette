package pw.bitcoinroulette.gui.game.listeners;


import java.util.Arrays;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import pw.bitcoinroulette.gui.game.Coord;
import pw.bitcoinroulette.gui.game.GameCtrl;

public class SelectorExitListener implements EventHandler<Event>{

	private GameCtrl gameCtrl;
	private List<Integer> blackNums = Arrays.asList(1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36);
	
	public SelectorExitListener(GameCtrl gameCtrl) {
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
				n.setEffect(new ColorAdjust());
				n.setStyle("-fx-border-color: transparent;\n");
				
				if(blackNums.contains(gameCtrl.coordToNumber.get(c))){
					n.setStyle("-fx-background-color: #191919;\n");
				}
			}
		}
	}
}
