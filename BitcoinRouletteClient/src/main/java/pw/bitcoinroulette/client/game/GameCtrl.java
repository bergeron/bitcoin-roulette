package main.java.pw.bitcoinroulette.client.game;


import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.pw.bitcoinroulette.client.Main;
import main.java.pw.bitcoinroulette.client.game.listeners.ChipClickListener;
import main.java.pw.bitcoinroulette.client.game.listeners.SelectorClickListener;
import main.java.pw.bitcoinroulette.client.game.listeners.SelectorEnterListener;
import main.java.pw.bitcoinroulette.client.game.listeners.SelectorExitListener;
import main.java.pw.bitcoinroulette.client.game.listeners.SpinFinishedListener;
import main.java.pw.bitcoinroulette.library.Bet;
import main.java.pw.bitcoinroulette.library.ClientGame;
import main.java.pw.bitcoinroulette.library.ServerGame;

public class GameCtrl implements ClientGame {
	@FXML public AnchorPane boardPane;
	@FXML public GridPane grid;
	@FXML public ImageView wheel;
	@FXML public Circle ball;
	@FXML public Text balanceText;
	@FXML public ImageView whiteChip;
	@FXML public ImageView redChip;
	@FXML public ImageView blueChip;
	@FXML public ImageView greenChip;
	@FXML public ImageView blackChip;
	@FXML public TextField whiteChipAmt;
	@FXML public TextField redChipAmt;
	@FXML public TextField blueChipAmt;
	@FXML public TextField greenChipAmt;
	@FXML public TextField blackChipAmt;
	@FXML public TableView<Bet> betTable;
	@FXML public TableColumn<Bet, Double> amountColumn;
	@FXML public TableColumn<Bet, String> descriptionColumn;
	@FXML public TableColumn<Bet, Button> deleteColumn;
	@FXML public Button accountBtn;
	
	
	public Main main;
	public ServerGame serverGame;
	public Coord[] numberToCoord = new Coord[37];
	public HashMap<Coord, Integer> coordToNumber = new HashMap<Coord, Integer>();
	public HashMap<Coord, Coord[]> coordToSelection = new HashMap<Coord, Coord[]>();
	public HashMap<Coord, String> specialDescriptions  = new HashMap<Coord, String>();
	public ObservableList<Bet> bets = FXCollections.observableArrayList();
	public BigDecimal[] chipAmounts = new BigDecimal[]{new BigDecimal(.05), new BigDecimal(.1), new BigDecimal(.2), new BigDecimal(.3), new BigDecimal(4)};
	public int currChip = -1;
	public ImageView[] chips;
	public ImageView floatingChip = new ImageView();
	public Group chipsOnBoard = new Group();
	public HashMap<Bet, ImageView> betToChip = new HashMap<Bet, ImageView>();
	

	public GameCtrl(Main main, ServerGame serverGame){
		this.main = main;
		this.serverGame = serverGame;
	}

	/* Called after scene graph loads */
	public void initialize(){
		
		accountBtn.setOnMouseClicked((e) -> main.setAccountScene());
		
		/* Bet table */
		betTable.setPlaceholder(new Label("No Bets Placed"));
		betTable.setItems(bets);
		amountColumn.setCellValueFactory(new PropertyValueFactory<Bet, Double>("amount"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Bet, String>("description"));
		deleteColumn.setCellValueFactory(new Callback<CellDataFeatures<Bet, Button>, ObservableValue<Button>>() {
		     public ObservableValue<Button> call(CellDataFeatures<Bet, Button> p) {
		    	 Button button = new Button("Delete");
		    	 button.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent arg0) {
						deleteBet(p.getValue());
					}
				});
		    	 return new ReadOnlyObjectWrapper<Button>(button);
		     }
		  });
		
		
		floatingChip.setMouseTransparent(true);
		floatingChip.setFitHeight(40);
		floatingChip.setFitWidth(40);
		boardPane.getChildren().add(chipsOnBoard);
		boardPane.getChildren().add(floatingChip);
		
		grid.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(currChip != -1){
					floatingChip.setVisible(true);
				}
			}
		});
		
		grid.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				floatingChip.setVisible(false);
			}
		});
		
		grid.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				floatingChip.setX(event.getSceneX()-370);
				floatingChip.setY(event.getSceneY()-23);
			}
		});
		
		whiteChipAmt.setText(chipAmounts[0]+"");
		redChipAmt.setText(chipAmounts[1]+"");
		blueChipAmt.setText(chipAmounts[2]+"");
		greenChipAmt.setText(chipAmounts[3]+"");
		blackChipAmt.setText(chipAmounts[4]+"");
		
		this.chips = new ImageView[]{whiteChip,redChip,blueChip,greenChip,blackChip};
		
		specialDescriptions.put(new Coord(8,9), "Red");
		specialDescriptions.put(new Coord(8,13), "Black");
		specialDescriptions.put(new Coord(7,1), "1-12");
		specialDescriptions.put(new Coord(7,9), "13-24");
		specialDescriptions.put(new Coord(7,17), "25-36");
		specialDescriptions.put(new Coord(8,1), "1-18");
		specialDescriptions.put(new Coord(8,5), "Even");
		specialDescriptions.put(new Coord(8,17), "Odd");
		specialDescriptions.put(new Coord(8,21), "19-36");
		specialDescriptions.put(new Coord(5,24), "1st Column");
		specialDescriptions.put(new Coord(3,24), "2nd Column");
		specialDescriptions.put(new Coord(1,24), "3rd Column");
		
		/* Get coordinate for each number */
		int num = 0;
		int row = 5;
		int col = 1;
		while(++num <= 36){
			Coord c = new Coord(row, col);
			numberToCoord[num] = c;
			coordToNumber.put(c, num);
			if(row == 1){
				col += 2;
				row = 5;
			} else {
				row -= 2;
			}
		}
		
		/* 0 selector */
		numberToCoord[0] = new Coord(1,0);
		coordToNumber.put(new Coord(1,0), 0);

		/* Each number selects itself */
		Arrays.asList(numberToCoord).forEach(c -> coordToSelection.put(c, new Coord[]{c}));

		/* Top and bottom selectors for whole column */
		for(col = 1; col <=23; col+=2){
			Coord[] coords = new Coord[]{
					new Coord(1,col),
					new Coord(3,col),
					new Coord(5,col)
			};
			coordToSelection.put(new Coord(0,col), coords);
			coordToSelection.put(new Coord(6,col), coords);
		}

		/* Top and bottom selectors for double column */
		for(col = 2; col <=22; col+=2){
			Coord[] coords = new Coord[]{
					new Coord(1,col-1),
					new Coord(1,col+1),
					new Coord(3,col-1),
					new Coord(3,col+1),
					new Coord(5,col-1),
					new Coord(5,col+1)
			};
			coordToSelection.put(new Coord(0,col), coords);
			coordToSelection.put(new Coord(6,col), coords);
		}

		/* Four selectors */
		for(int r : new int[]{2,4}){
			for(int c = 2; c <= 22; c+=2){
				coordToSelection.put(new Coord(r, c), new Coord[]{new Coord(r-1, c-1), new Coord(r-1, c+1), new Coord(r+1,c+1), new Coord(r+1,c-1)});
			}
		}

		/* Two selectors (vertical) */
		for(int r : new int[]{2,4}){
			for(int c = 1; c <= 23; c+=2){
				coordToSelection.put(new Coord(r, c), new Coord[]{new Coord(r-1, c), new Coord(r+1, c)});
			}
		}

		/* Two selectors (horizontal) */
		for(int r : new int[]{1,3,5}){
			for(int c = 2; c <= 22; c+=2){
				coordToSelection.put(new Coord(r, c), new Coord[]{new Coord(r, c-1), new Coord(r, c+1)});
			}
		}

		/* 1-12 selector */
		Coord[] ray = new Coord[12];
		for(int i=1; i <= 12; i++){
			ray[i-1] = numberToCoord[i];
		}
		coordToSelection.put(new Coord(7,1), ray);

		/* 13-24 selector */
		ray = new Coord[12];
		for(int i=13; i <= 24; i++){
			ray[i-13] = numberToCoord[i];
		}
		coordToSelection.put(new Coord(7,9), ray);

		/* 25-36 selector */
		ray = new Coord[12];
		for(int i=25; i <= 36; i++){
			ray[i-25] = numberToCoord[i];
		}
		coordToSelection.put(new Coord(7,17), ray);

		/* 1-18 selector */
		ray = new Coord[18];
		for(int i=1; i <= 18; i++){
			ray[i-1] = numberToCoord[i];
		}
		coordToSelection.put(new Coord(8,1), ray);

		/* 19-36 selector */
		ray = new Coord[18];
		for(int i=19; i <= 36; i++){
			ray[i-19] = numberToCoord[i];
		}
		coordToSelection.put(new Coord(8,21), ray);

		/* Parity selector */
		Coord[] evenCoords = new Coord[18];
		Coord[] oddCoords = new Coord[18];
		int eIdx = 0;
		int oIdx = 0;

		for(int i=1; i <= 36; i++){
			if(i % 2 == 0)
				evenCoords[eIdx++] = numberToCoord[i];
			else
				oddCoords[oIdx++] = numberToCoord[i];
		}
		coordToSelection.put(new Coord(8,5), evenCoords);
		coordToSelection.put(new Coord(8,17), oddCoords);

		/* Red/Black selector */
		List<Coord> redCoords = new ArrayList<Coord>();
		List<Coord> blackCoords = new ArrayList<Coord>();
		Coord[] redRay = new Coord[18];
		Coord[] blackRay = new Coord[18];
		
		/* Reds */
		Arrays.asList(2,4,6,8,10,11,13,15,17,20,22,24,26,28,29,31,33,35).forEach
			(i -> redCoords.add(numberToCoord[i]));
		
		/* Blacks */
		Arrays.asList(1,3,5,7,9,12,14,16,18,19,21,23,25,27,30,32,34,36).forEach
			(i -> blackCoords.add(numberToCoord[i]));
		
		coordToSelection.put(new Coord(8,9), redCoords.toArray(redRay));
		coordToSelection.put(new Coord(8,13), blackCoords.toArray(blackRay));

		/* '1st','2nd','3rd' column selectors */
		Coord[] first = new Coord[12];
		Coord[] second = new Coord[12];
		Coord[] third = new Coord[12];
		int firstIdx=0 ,secondIdx=0 ,thirdIdx = 0;
		for(int i=1; i <= 36; i++){
			if(i % 3 == 0){
				third[thirdIdx++] = numberToCoord[i];
			} else if (i % 3 == 2) {
				second[secondIdx++] = numberToCoord[i];
			} else {
				first[firstIdx++] = numberToCoord[i];
			}
		}
		coordToSelection.put(new Coord(5,24), first);
		coordToSelection.put(new Coord(3,24), second);
		coordToSelection.put(new Coord(1,24), third);

		/* Listeners */
		for(Node n : grid.getChildren()){
			n.setOnMouseEntered(new SelectorEnterListener(this));
			n.setOnMouseExited(new SelectorExitListener(this));
			n.setOnMouseClicked(new SelectorClickListener(this));
		}

		EventHandler<MouseEvent> chipClick = new ChipClickListener(this);
		whiteChip.setOnMouseClicked(chipClick);
		redChip.setOnMouseClicked(chipClick);
		blueChip.setOnMouseClicked(chipClick);
		greenChip.setOnMouseClicked(chipClick);
		blackChip.setOnMouseClicked(chipClick);
		
//		try {
//			balanceText.setText(String.format("%.8f฿", main.authPlayer.getBalance()));
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		
		ball.setCenterX(-45);
		ball.setCenterY(13);//TODO not this
		
	}

	public Node getPaneFromCoord(int row, int col){
		for(Node n : grid.getChildren()){
			if(GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col)
				return n;
		}
		return null;
	}


	public void spin(ActionEvent e){
		wheel.setRotate(0);
		
		Path path = new Path();
		path.getElements().add(new MoveTo(0,0));
			
		/* The wheel is a 16 slice pizza */
		double radius = 110;
		double [] xs = new double[]{-45, -75, -101, -110, -102, -69, -47, 0, 43, 76, 105, 115, 108, 80, 50, 0};
		double [] ys = new double[]{13, 35, 70, 105, 155, 201, 215, 228, 222, 202, 160, 115, 80, 33, 12, 0};
		
		/* Choose a random slice for ball to finish on */
		int ballPos = (int)(Math.random() * 16);
		int spins = 12;
		long ticks =  (16 * spins) + ballPos;
		
		for(int i=0; i < ticks; i++){
			double x = xs[i %  xs.length];
			double y = ys[i %  ys.length];
			path.getElements().add(new ArcTo(radius, radius, 0, x, y, false, false));
		}

		long durationMillis = 12 * 1000;
		PathTransition spin = new PathTransition(Duration.millis(durationMillis), path, ball);
		spin.setInterpolator(Interpolator.LINEAR);
		spin.play();

		/* The wheel is a 37 slice pizza.  Choose random slice */
		int wheelPos = (int)(Math.random() * 37);
		int wheelSpins = 3;
		double deg = (360.0 * wheelSpins) + ((360.0/37.0) * wheelPos);
		RotateTransition rt = new RotateTransition(Duration.millis(durationMillis), wheel);
		rt.setInterpolator(Interpolator.EASE_BOTH);
		rt.setByAngle(deg);
		rt.play();
		
		int[] order = new int[]{0,26,3,35,12,28,7,29,18,22,9,31,14,20,1,33,16,24,5,10,23,8,30,11,36,13,27,6,34,17,25,2,21,4,19,15,32};
		int[] ballPosToOffset = new int[]{-1, 1, 3, 5, 7, 10, 13, 14, 17, 19, 21, 24, 26, 28, 31, 33, 35};
		int result = order[(wheelPos + ballPosToOffset[ballPos]) % 37];
		System.out.println("Result: " + result);
		
		rt.setOnFinished(new SpinFinishedListener(this, result));
	}
	


	public void addBet(Bet b) {
		bets.add(b);
	}

	public void deleteBet(Bet bet) {
		bets.remove(bet);
		chipsOnBoard.getChildren().remove(betToChip.get(bet));
	}
	
	public String coordToDescription(Coord coord){
		String description = "";
		if(specialDescriptions.containsKey(coord)){
			return specialDescriptions.get(coord);
		} else {
			HashSet<Integer> winningNumbers = new HashSet<Integer>();
			Arrays.asList(coordToSelection.get(coord)).forEach
				(c -> winningNumbers.add(coordToNumber.get(c)));
			
			String stringRay = winningNumbers.toString();
			return stringRay.substring(1, stringRay.length() -1);
		}
	}

	@Override
	public void spin(int result) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
