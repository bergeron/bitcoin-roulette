package main.java.pw.bitcoinroulette.client.account;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;

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
import main.java.pw.bitcoinroulette.client.Main;
import main.java.pw.bitcoinroulette.library.Transaction;

import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;



public class AccountCtrl {
	private Main main;
	@FXML public Label balanceLabel;
	@FXML public TableView<TransactionRow> tableView;
	@FXML public TableColumn<TransactionRow, String> fromAddressCol;
	@FXML public TableColumn<TransactionRow, BigDecimal> amountCol;
	@FXML public TableColumn<TransactionRow, String> statusCol;
	@FXML Button gameBtn;

	public AccountCtrl(Main main) {
		this.main = main;
	}

	public void initialize() {
		gameBtn.setOnMouseClicked((e) -> main.setGameScene(main.serverGame));
		
		fromAddressCol.setCellValueFactory(new PropertyValueFactory<TransactionRow, String>("address"));
		amountCol.setCellValueFactory(new PropertyValueFactory<TransactionRow, BigDecimal>("amount"));
		statusCol.setCellValueFactory(new PropertyValueFactory<TransactionRow, String>("status"));
		tableView.setPlaceholder(new Text("No transactions"));
		refresh(new ActionEvent());
	}

	public void refresh(ActionEvent e) {
		ObservableList<TransactionRow> rows = tableView.getItems();
		rows.clear();

		List<Transaction> txs;
		try {
			txs = main.authPlayer.getTransactions();
		} catch (RemoteException e1) {
			e1.printStackTrace();
			return;
		}

		for (Transaction t : txs) {
			TransactionRow tr;
			try {
				tr = new TransactionRow(/* t.getAddress() */"addr", t.getAmount(), t.getConfirmations() >= 6 ? "Confirmed"
						: t.getConfirmations() + " confirmations");
				rows.add(tr);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		BigDecimal balance;
		try {
			balance = main.authPlayer.getBalance();
		} catch (RemoteException e1) {
			e1.printStackTrace();
			return;
		}
		balanceLabel.setText(String.format("%.8f฿", balance));
	}

	public void fundAccount() {
		Dialog dlg = new Dialog(null, "Fund Account", true);
		VBox content = new VBox(20);
		content.setPadding(new Insets(10, 10, 10, 10));
		Label l = new Label("Send Bitcoin to this address:");
		l.setFont(new Font(25));
		content.getChildren().add(l);

		String address;
		try {
			address = main.authPlayer.getBitcoinAddress();
		} catch (RemoteException e) {
			e.printStackTrace();
			return;
		}
		TextField tf = new TextField(address);

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

	public void withdraw() {
		Dialog dlg = new Dialog(null, "Withdraw", true);
		GridPane gridPane = new GridPane();
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		gridPane.setPadding(new Insets(10, 10, 10, 10));
		gridPane.add(new Label("Amount (BTC)"), 0, 0);
		gridPane.add(new Label("Address"), 0, 1);

		Label error = new Label("Error");
		error.setTextFill(Color.RED);
		error.setVisible(false);
		gridPane.add(error, 1, 2);

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
			{
				ButtonBar.setType(this, ButtonType.OK_DONE);
			}

			@Override
			public void handle(ActionEvent ae) {
				Dialog dlg = (Dialog) ae.getSource();
				String address = addressField.getText();
				error.setVisible(false);

				BigDecimal amount;
				try{
					amount = new BigDecimal(amountField.getText());
					if (amount.compareTo(BigDecimal.ZERO) < 0) {
						throw new NumberFormatException("Withdraw amount must be positive");
					}
				} catch (NumberFormatException e){
					error.setText("Invalid Amount");
					error.setVisible(true);
					return;
				}
				
				String txHash = "";
				try {
					txHash = main.authPlayer.withdraw(address, amount);
				} catch (RemoteException e) {
					
					System.out.println(e.getCause().getMessage());
					error.setText(e.getCause().getMessage());
					error.setVisible(true);
					return;
				}
				
				System.out.println(txHash);

				dlg.hide();
			}
		};

		dlg.getActions().add(submitAction);
		dlg.setContent(gridPane);
		dlg.show();
	}
}
