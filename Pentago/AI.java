/* AI can be created for any possible current board
 * Can also have a given next move
 * Creates more AI's for every possible next move, until it reaches a certain depth
 * Then the highest AI makes a move according to the best possiblity, which changes the main board
 */
public class AI extends Board{
	
	public static final int[] scores = {0, 0, 10, 100, 1000, 100000};
	
	public static final int maxDepth = 2;
	public AI parent;
	
	private AI[] children;
	private int depth; 
	
	public int best = 0;
	

	
	//Constructor for starting AI before future moves
	public AI(int[][] b, int depth){
		
		super(b);
		
		
		this.depth = depth;
		
		if(depth < maxDepth){
			
			children = new AI[8 * (36-numMarbles)];
			createChildren();
		
			
		
		}
	}
	
	//Constructor for AI within given move
	public AI(AI parent, int[][] b, int depth, int x, int y, int q, int d){
		
		super(b);
		
		
		this.depth = depth;
		this.parent = parent;
		
		add(x,y);
		rotate(q,d);
		
		
					
					
		if(depth < maxDepth){
			
			children = new AI[8 * (36-numMarbles)];
			
		
		}
	}
	
	
	//Creates new AI class for all possible next moves
	public void createChildren(){
		int filled = 0;
		
		
		for(int i = 0; i < 36; i++){
			if(empty(i/6,i%6)){
				for(int j = 0; j < 8; j++){	
					
					
					children[filled] = new AI(this, copy(board), depth + 1, i/6, i%6, j/2,  2*(j%2) - 1);
					if(depth + 1 < maxDepth){
						children[filled].createChildren();
					}
					
					
					
					
					//System.out.print(i/6 + " " + i%6 + " " + j/2 + " " + ( 2*(j%2) - 1));
					
					if(children[filled].boardScore() < children[best].boardScore() && (children[filled].numMarbles % 2 == 1)){
						best = filled;
						
						//Prunes game possiblities that do not need to be searched
						if(depth == 1){
							int a = parent.boardScore();
							if(children[filled].boardScore() < a){
								
								rotate(j/2, 1 - 2*(j%2));
								board[i/6][i%6]=0;
					
								return;			
							}
						}
					}
					if(children[filled].boardScore() > children[best].boardScore() && (children[filled].numMarbles % 2 == 0)){
						best = filled;
						
						/*if(depth == 2){
							int a = parent.children[parent.best].boardScore();
							if(children[filled].boardScore() > a){
								return;
							}
						}*/
					}
					
					
					
					
					filled++;
					
					
					
					
					
				}
			}
		}
	}
	
	
	public void move(){
		add(children[best].lastMove[0],children[best].lastMove[1]);
		rotate(children[best].lastMove[2],children[best].lastMove[3]);
		
	}
	public void stuff(){
		
	}
	//Returns heuristic score of board
	public int boardScore(){
		
		
		
		if(this.checkWin() == 2){
			return 1000000000;
		}
		else if(depth < maxDepth){
			return children[best].boardScore();
		}
		
		else{
			int score  = 0;
			
			//Points for line length
			for(int x = 0; x < 6; x++){
				for(int y = 0; y < 6; y++){
					for(int i = 0; i < 4; i++){
						int line = length(x, y, (i<3)?1:0, i%3 - 1);
						score += scores[Math.abs(line)] * ((line > 0)?1:-1);
					}
				}
			}
			
			//Points for center of squares
			for(int i = 0; i < 4; i++){
				score += (board[3 * (i % 2) + 1][3 * (i / 2) + 1]) * 3;
			}
			
			return score;
		}	
	}
	
	
	/*
	//Just for testing
	public boolean same(int index){
		boolean s = true;
		for(int i = 0; i < 36; i ++){
			s &= board[i/6][i%6] == children[index].board[i/6][i%6];
		}
		return s;
	}
	*/
}
