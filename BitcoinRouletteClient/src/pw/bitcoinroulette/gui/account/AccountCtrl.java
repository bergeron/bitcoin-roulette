package pw.bitcoinroulette.gui.account;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

import pw.bitcoinroulette.gui.Main;

import com.azazar.bitcoin.jsonrpcclient.Bitcoin.Transaction;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;

public class AccountCtrl {
	private Main main;
	@FXML public Label balanceLabel;
	@FXML public TableView<TransactionRow> tableView;
	@FXML public TableColumn<TransactionRow, String> fromAddressCol;
	@FXML public TableColumn<TransactionRow, Double> amountCol;
	@FXML public TableColumn<TransactionRow, String> statusCol;
	
	public AccountCtrl(Main main){
		this.main = main;
	}
	
	public void initialize(){
		fromAddressCol.setCellValueFactory(new PropertyValueFactory<TransactionRow, String>("address"));
		amountCol.setCellValueFactory(new PropertyValueFactory<TransactionRow, Double>("amount"));
		statusCol.setCellValueFactory(new PropertyValueFactory<TransactionRow, String>("status"));
		tableView.setPlaceholder(new Text("No transactions"));
		refresh(new ActionEvent());
	}

	public void refresh(ActionEvent e){
		ObservableList<TransactionRow> rows = tableView.getItems();
		rows.clear();
		
		for(Transaction t : main.core.player.getTransactions()){
			rows.add(new TransactionRow(t.address(), t.amount(), 
					t.confirmations() >= 6 ? "Confirmed" : t.confirmations() + " confirmations"));
		}
		
		balanceLabel.setText(String.format("%.8f฿", main.core.player.getBalance()));
	}
	
	public void fundAccount(){
		 Dialog dlg = new Dialog(null, "Fund Account", true);
		 VBox content = new VBox(20);
		 content.setPadding(new Insets(10, 10, 10, 10));
		 Label l = new Label("Send Bitcoin to this address:");
		 l.setFont(new Font(25));
		 content.getChildren().add(l);
		 TextField tf = new TextField(main.core.player.getAddress());
		 tf.setEditable(false);
		 content.getChildren().add(tf);
		 Button bt = new Button("Okay");
		 bt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dlg.hide();
			}
		});
		 content.getChildren().add(bt);
		 dlg.setContent(content);
		 dlg.show();
	}
	
	public void withdraw(){
		Dialog dlg = new Dialog(null, "Withdraw", true);
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.add(new Label("Amount (BTC)"),0,0);
		gridPane.add(new Label("Address"),0,1);
		
		Label error = new Label("Error");
		error.setTextFill(Color.RED);
		error.setVisible(false);
		gridPane.add(error,1,2);
		
		EventHandler<Event> clearErrors = new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				error.setVisible(false);
			}
		};
		
		TextField amountField = new TextField();
		amountField.setOnMouseClicked(clearErrors);
		gridPane.add(amountField, 1, 0);
		
		TextField addressField = new TextField();
		addressField.setOnMouseClicked(clearErrors);
		gridPane.add(addressField, 1, 1);
		
		final Action submitAction = new AbstractAction("Withdraw") {
			{ButtonBar.setType(this, ButtonType.OK_DONE);}
			@Override
			public void handle(ActionEvent ae) {
				Dialog dlg = (Dialog) ae.getSource();
				String address = addressField.getText();
				error.setVisible(false);
				
				double amount;
				try{
					amount = Double.parseDouble(amountField.getText());
					if(amount <= 0)
						throw new NumberFormatException();
				} catch(NumberFormatException e) {
					error.setText("Invalid Amount");
					error.setVisible(true);
					return;
				}
				
				try {
					if(!main.core.getBitcoin().validateAddress(address).isValid())
						throw new BitcoinException();
				} catch (BitcoinException e) {
					error.setText("Invalid Address");
					error.setVisible(true);
					return;
				}
				
				/* Amount and address valid */
				try {
					double balance = main.core.player.getBalance();
					if(amount > balance){
						error.setText("Insufficient balance");
						error.setVisible(true);
						return;
					}
					
					String txHash = main.core.getBitcoin().sendToAddress(address, amount);
					
				} catch (BitcoinException e) {
					e.printStackTrace();
					//TODO
				}
				
				dlg.hide();
			}
		 };
		
		dlg.getActions().add(submitAction);
		dlg.setContent(gridPane);
		Action response = dlg.show();
	}
	
	public void showGameScene(){
		main.stage.setScene(main.gameScene);
	}
}
