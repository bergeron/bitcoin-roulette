package main.java.pw.bitcoinroulette.client.login;

import java.io.IOException;
import java.rmi.RemoteException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import main.java.pw.bitcoinroulette.client.Main;
import main.java.pw.bitcoinroulette.client.lobby.LobbyCtrl;
import main.java.pw.bitcoinroulette.library.AuthPlayer;
import main.java.pw.bitcoinroulette.library.Lobby;

import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.ButtonBar.ButtonType;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;

public class LoginCtrl {

	private Main main;

	public LoginCtrl(Main main) {
		this.main = main;
	}

	public void initialize() {

	}
	
	public void dialog(String barTitle, String title, Action action, TextField username, PasswordField password, Label validationError){
		Dialog dlg = new Dialog(main.stage, barTitle);

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));
		
		validationError.setStyle("-fx-text-fill: red");
		validationError.setVisible(false);
		grid.add(validationError, 1, 2);
		
		username.setPromptText("Username");
		password.setPromptText("Password");
		username.setOnMouseClicked((e) -> validationError.setVisible(false));
		password.setOnMouseClicked((e) -> validationError.setVisible(false));
		
		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);
		
		
		final Action actionCancel = new AbstractAction("Cancel") {
			public void handle(ActionEvent ae) {
				dlg.hide();
			}
		};
		
		ButtonBar.setType(action, ButtonType.OK_DONE);
		action.disabledProperty().set(true);
		
		ButtonBar.setType(actionCancel, ButtonType.OK_DONE);

		username.textProperty().addListener((observable, oldValue, newValue) -> {
			action.disabledProperty().set(newValue.trim().isEmpty());
		});

		dlg.setMasthead(title);
		dlg.setContent(grid);
		dlg.getActions().addAll(action, actionCancel);

		/* Request focus on the username field by default */
		Platform.runLater(() -> username.requestFocus());

		dlg.show();
		
	}

	public void login() {
		
		final TextField username = new TextField();
		final PasswordField password = new PasswordField();
		final Label validationError = new Label("Wrong username/password.");
		
		final Action action = new AbstractAction("Login") {
			
			public void handle(ActionEvent ae) {
				Dialog d = (Dialog) ae.getSource();
				
				try {
					Object[] login = main.loginServer.login(username.getText(), password.getText());
					
					if(login == null){
						throw new RemoteException("Invalid username or password");
					}
					
					main.authPlayer = (AuthPlayer)login[0];
					Lobby lobby = (Lobby)login[1];
					main.lobbyCtrl = new LobbyCtrl(main, lobby);
					main.setLobbyScene();
					
				} catch (RemoteException e) {
					e.printStackTrace();
					/* Wrong username pass */
					validationError.setVisible(true);
					password.clear();
					return;
				}
				d.hide();
			}
		};
		
		dialog("Login", "Login to Bitcoin Roulette", action, username, password, validationError);

	}

	public void register() {
		final TextField username = new TextField();
		final PasswordField password = new PasswordField();
		final Label validationError = new Label("Username already taken.");
		
		final Action action = new AbstractAction("Register") {
			/* This method is called when the login button is clicked */
			public void handle(ActionEvent ae) {
				Dialog d = (Dialog) ae.getSource();
				
				try {
					main.loginServer.register(username.getText(), password.getText());
				} catch (RemoteException e) {
					
					//TODO set to e.message.  username taken? password too short?
					/* Wrong username pass */
					validationError.setVisible(true);
					password.clear();
					return;
				}
				d.hide();
			}
		};
		
		dialog("Register", "Register for Bitcoin Roulette", action, username, password, validationError);
	}
}
