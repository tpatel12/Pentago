/* Represents a Pentago game board
 * Has an integer array representing the current state
 * Has methods to add and rotate for a turn
 * Also creates all of the GUI for the entire game, which is used by the Pentago class
 */
import java.util.ArrayList;
import java.io.File;
import java.util.Random;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color; 
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.Slider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.input.MouseEvent;


public class Board{
	
	
	//public static final Color[] colors = {Color.color(1,0.4,0.4,1), Color.TRANSPARENT, Color.color(0.4,0.4,1,1)}; //-1, 0, 1 in the array
	public static final Color[] colors = {Color.color(0,0,0,1), Color.TRANSPARENT, Color.color(1,1,1,1)}; //-1, 0, 1 in the array
	
	public static boolean clicked = false;
	
	public int[][] board = new int[6][6];
	
	public int numMarbles;
	
	public Circle[][] marbles;	
	public GridPane boardGrid;
	
	public int[] lastMove = new int[4];
	
	public boolean nextAdd = true;
	public boolean done = false;
	
	public static String[] winners = {"Player Wins","Tie","AI Wins"};
	public Text winnerText = new Text();
	
	public Board(int[][] b){
		
		
		//Create board
		board = b;
		
		
		
		
	}
	
	//Creates entire game GUI for the main Pentago class to use
	public VBox getGUI(){
		
		VBox gui = new VBox();
		gui.setAlignment(Pos.CENTER);
		
		
		BorderPane game = new BorderPane();
		StackPane lines = new StackPane();
		GridPane boardGrid = new GridPane();
		marbles = new Circle[6][6];
		
		boardGrid.setHgap(20);
		boardGrid.setVgap(20);
		
		for(int i = 0; i < 6; i ++){
			for(int j = 0; j < 6; j++){		
				marbles[i][j] = new Circle(0,0,30);	
				marbles[i][j].setStroke(Color.BLACK);
				boardGrid.add(marbles[i][j], i, j);
				
				int x = i;
				int y = j;
				
				//Code from http://www.java2s.com/Code/Java/JavaFX/Listentoallmouseevents.htm
				EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
 
					@Override
					public void handle(MouseEvent e) {
						if(empty(x,y) && nextAdd && (!done)){
							add(x,y);
							updateMarbles();
							
						}
					
					}
					
					
				};
				
				marbles[i][j].setOnMouseClicked(mouseHandler);
				
			}
		}
		lines.getChildren().add(boardGrid);
		
		lines.getChildren().add(new Line(0,235,470,235));
		lines.getChildren().add(new Line(235,0,235,470));
		
		
		game.setCenter(lines);
		game.setTop(sidePane(0,1,1,-1));
		game.setAlignment(game.getTop(),Pos.CENTER);
		game.setLeft(sidePane(0,-1,2,1));
		game.setAlignment(game.getLeft(),Pos.CENTER);
		game.setRight(sidePane(1,1,3,-1));
		game.setAlignment(game.getRight(),Pos.CENTER);
		game.setBottom(sidePane(2,-1,3,1));
		game.setAlignment(game.getBottom(),Pos.CENTER);
		
		
		gui.getChildren().add(game);
		
		
		winnerText = new Text();
		winnerText.setFont(Font.font(50));
		
		gui.getChildren().add(winnerText);
	
		return gui;
		
	}
	
	//Creates Panes with buttons to go on the sides of the board
	public Pane sidePane(int q1, int d1, int q2, int d2){
		Pane buttons;
		
		int w = 30;
		int h = 30;
		
		Polygon[] p = new Polygon[2];
		
		if(q2-q1 == 1){
			buttons = new HBox();
			w=150;
			buttons.setMaxHeight(h);
			buttons.setMaxWidth(2 * w);
			
			
			p[0] = new Polygon(0,0,30,10,0,20);
			p[1] = new Polygon(0,0,-30,10,0,20);
			
		} else {
			buttons = new VBox();
			h=150;
			buttons.setMaxHeight(2 * h);
			buttons.setMaxWidth(w);
			p[0] = new Polygon(0,0,20,0,10,30);
			p[1] = new Polygon(0,0,20,0,10,-30);
		
		
		}
		
		
		
		for(int i = 0 ; i < 2; i++){
			
			Button btn = new Button("",p[i]);
			btn.setPrefWidth(w);
			btn.setPrefHeight(h);
		
			
			
			boolean x = (i==0);
			btn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if((!nextAdd) && (!done)){
						rotate(x?q1:q2, x?d1:d2);
						updateMarbles();
						
						if(!done){
							Pentago.turn();
						}
						
					}
							
				}
			});
			
			buttons.getChildren().add(btn);
		}
		
		return buttons;
	}
	
	
	public boolean empty(int x, int y){
		return (board[x][y]==0);
	}
	
	
	public void add(int x, int y){
		
		numMarbles = 0;
		for(int i = 0; i < 36; i ++){
			numMarbles += ((board[i/6][i%6]==0)?0:1);
		}
		
		
		board[x][y] = 2 * (this.numMarbles%2) - 1;
		numMarbles++;
		
		lastMove[0] = x;
		lastMove[1] = y;
		
		nextAdd = false;
		checkWin();
		
	}
	
	public void rotate(int quad, int d){
		
	
		int x = 3 * (quad % 2) + 1;
		int y = 3 * (quad / 2) + 1;
		
		int temp = board[x-1][y-1];
		
		board[x-1][y-1] = board[x-d][y+d];
		board[x-d][y+d] = board[x+1][y+1];
		board[x+1][y+1] = board[x+d][y-d];
		board[x+d][y-d] = temp;
		
		
		temp = board[x][y-1];
		board[x][y-1] = board[x-d][y];
		board[x-d][y] = board[x][y+1];
		board[x][y+1] = board[x+d][y];
		board[x+d][y] = temp;
		
		lastMove[2] = quad;
		lastMove[3] = d;
		
		nextAdd = true;
		checkWin();
		
	}

    public void updateMarbles(){
		for(int i = 0; i < 36; i++){
			marbles[i/6][i%6].setFill(colors[board[i/6][i%6] + 1]);
		}
		checkWin();
	}
	
	
	public int checkWin(){
		boolean b = false;
		boolean w = false;
	
		
		for(int x = 0; x < 6; x++){
			for(int y = 0; y < 6; y++){
				for(int i = 0; i < 4; i++){
					int line = length(x, y, (i<3)?1:0, i%3 - 1);
					
					b |= (line == -5);
					w |= (line == 5);
					
				}
			}
		}
		int winner = (w?1:0) - (b?1:0) + 1;
		if(b || w){
			
			done = true; 
				winnerText.setText(winners[winner]);
			if(b!=w){
				winnerText.setFill(colors[winner]);
			}
		}
		return winner;
		
	}
	
	//Determines length of a line of marbles of the same color starting from a point and given direction
	public int length(int x, int y, int xx, int yy){
		
		
		int l = 0;
		
		try{
			while(board[x][y]!=0 && board[x][y]==board[x + xx * l][y + yy * l]){
				l+=board[x][y];
			}
		}
		catch (Exception e){
		}
		
		return Math.max(Math.min(l,5),-5);
		
		
	}
	public int[][] copy(int[][]arr){
		int[][]copyBoard = new int[6][6];
		for(int i=0; i <36;i++){
			copyBoard[i/6][i%6]=arr[i/6][i%6];
		}
		return copyBoard;
	}
	
	
}
