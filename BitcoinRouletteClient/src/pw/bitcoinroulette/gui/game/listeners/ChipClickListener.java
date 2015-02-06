package pw.bitcoinroulette.gui.game.listeners;



import pw.bitcoinroulette.gui.game.GameCtrl;
import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ChipClickListener  implements EventHandler<MouseEvent>{
	private GameCtrl gameCtrl;
	
	public ChipClickListener(GameCtrl gameCtrl){
		this.gameCtrl = gameCtrl;
	}
	
	public void handle(MouseEvent event) {
		ImageView img =(ImageView)event.getSource();
		switch(img.getId()){
			case "whiteChip":
				gameCtrl.currChip = 0;
				break;
			case "redChip":
				gameCtrl.currChip = 1;
				break;
			case "blueChip":
				gameCtrl.currChip = 2;
				break;
			case "greenChip":
				gameCtrl.currChip = 3;
				break;
			case "blackChip":
				gameCtrl.currChip = 4;
				break;
		}
		gameCtrl.floatingChip.setImage(gameCtrl.chips[gameCtrl.currChip].getImage());
		
		ColorAdjust colorDesaturate = new ColorAdjust();
		colorDesaturate.setSaturation(-0.4);
		
		 
		for(int i=0; i < gameCtrl.chips.length; i++){
			if(i != gameCtrl.currChip){
				gameCtrl.chips[i].setEffect(colorDesaturate);
			} else {
				gameCtrl.chips[i].setEffect(new ColorAdjust());
			}
		}
		
	}

}
