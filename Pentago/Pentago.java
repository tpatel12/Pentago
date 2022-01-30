/* Pentago class is the main class for the game
 * Has the main method and start method for JavaFx
 * Creates Board and AI objects for parts of the game
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
import javafx.scene.shape.Circle;

public class Pentago extends Application{
	
	public static final Color[] colors = {Color.BLACK, Color.TRANSPARENT, Color.WHITE}; //-1, 0, 1 in the array
	
	public static final int[] scores = {0, 1, 10, 100, 1000, 100000};
	
	public static int[][] board = new int[6][6];
	public static Circle[][] marbles = new Circle[6][6];
	
	
	public static int value = 0;
	
	public static Board b = new Board(new int[6][6]);
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Group group = new Group(b.getGUI());
		b.updateMarbles();
        
		Scene scene = new Scene(group, 600, 600); 

		scene.setFill(Color.rgb(150,150,150));  
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Pentago"); 
		primaryStage.show();
		
	}	
		
	public static void main(String[] args){	
		Application.launch(args);
	}

	/* Only for testing
	public static void print(int[][] x){
		for(int i = 0; i < 6; i ++){
			System.out.println();
			for(int j = 0; j < 6; j++){
				System.out.print(x[i][j]+ " ");
			}
			
		}
	}
	*/
	
	//Creates a copy of an array
	public static int[][] copy(int[][]arr){
		int[][]copyBoard = new int[6][6];
		for(int i=0; i <36;i++){
			copyBoard[i/6][i%6]=arr[i/6][i%6];
		}
		return copyBoard;
	}
	
	
	//Moves for AI
	public static void turn(){
		AI ai = new AI(b.board,0);
				
		ai.move();
		
		b.updateMarbles();
		
		
	}
}
