package pw.bitcoinroulette.gui.game.listeners;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pw.bitcoinroulette.Bet;
import pw.bitcoinroulette.gui.game.GameCtrl;

public class SpinFinishedListener implements EventHandler<ActionEvent> {
	private int result;
	private GameCtrl gameCtrl;

	public SpinFinishedListener(GameCtrl gameCtrl, int result) {
		this.gameCtrl = gameCtrl;
		this.result = result;
	}

	@Override
	public void handle(ActionEvent event) {
		
		for(Bet b: gameCtrl.bets){
			System.out.println((b.cameTrue(result) ? "True: " : "False: ") + b.winning);
			if(b.cameTrue(result)){
				b.p.setBalance(b.account.getBalance() + (b.amount *b .payout));
			}
		}
		
	}

}
