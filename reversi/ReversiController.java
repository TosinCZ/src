package reversi;

import java.awt.Color;

public class ReversiController implements IController{
	
	private IModel model;
    private IView view;
    private int steps;
    private boolean turnMissed;
    
    @Override
	public void initialise(IModel model, IView view ) {
		this.model = model;
		this.view = view;
	}

    @Override
	public void startup() {
    	model.setPlayer(1);
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		for ( int x = 0 ; x < width ; x++ ) {
			for ( int y = 0 ; y < height ; y++ ) {
				model.setBoardContents(x, y, 0);
			}
		}
		
		int midY = height / 2;
	    int midX = width / 2;

	    model.setBoardContents(midX - 1, midY - 1, 1); 
	    model.setBoardContents(midX, midY, 1);        
	    model.setBoardContents(midX - 1, midY, 2);    
	    model.setBoardContents(midX, midY - 1, 2);     
	    
		update();
		view.refreshView();
    }
	
    @Override
	public void update() {
		boolean canMove = checkForMoves(model.getPlayer());
		if (canMove) {
			model.setFinished(false);
			turnMissed = false;
			if (model.getPlayer() == 2) {
				view.feedbackToUser(2, "Black player – choose where to put your piece");
				view.feedbackToUser(1, "White player – not your turn");
			}
			else if (model.getPlayer() == 1) {
				view.feedbackToUser(1, "White player – choose where to put your piece");
				view.feedbackToUser(2, "Black player – not your turn");	
			}
		}
		else {
			if (!turnMissed) {
				turnMissed = true;
				model.setPlayer(model.getPlayer() == 1 ? 2 : 1);
	    		update();
			}	   		
			else {
				model.setFinished(true);
				checkWhoWon();
				turnMissed = false;
			}
		}
		return;
	}
	 
    private void checkWhoWon() {
    	int whiteToken = 0;
    	int blackToken = 0;
    			
    	for (int y = 0; y < model.getBoardHeight(); y++) {
	    	for (int x = 0; x < model.getBoardWidth(); x++) {
	    		if (model.getBoardContents(x, y) == 1) {
	    			whiteToken++;
	    		}
	    		else if (model.getBoardContents(x, y) == 2) {
	    			blackToken++;
	    		}
	    	}
    	}
    	
    	if (whiteToken > blackToken) {
    		view.feedbackToUser(1, "White won. White "+ whiteToken +" to Black "+ blackToken +". Reset game to replay.");
    		view.feedbackToUser(2, "White won. White "+ whiteToken +" to Black "+ blackToken +". Reset game to replay.");
    	}
    	else if (blackToken > whiteToken) {
    		view.feedbackToUser(1, "Black won. Black "+ blackToken +" to White "+ whiteToken +". Reset game to replay.");
    		view.feedbackToUser(2, "Black won. Black "+ blackToken +" to White "+ whiteToken +". Reset game to replay.");
    	}
    	else {
    		view.feedbackToUser(1, "Draw. Both players ended with "+ blackToken +" pieces. Reset game to replay.");
    		view.feedbackToUser(2, "Draw. Both players ended with "+ blackToken +" pieces. Reset game to replay.");
    	}
    	return;
    }
    
	private boolean checkForMoves(int player) {
		for (int y = 0; y < model.getBoardHeight(); y++) {
	    	for (int x = 0; x < model.getBoardWidth(); x++) {
	    		if (model.getBoardContents(x, y) != 0) {
	    			continue;
	    		}
	    		for (int[] dir : new int [][] {{1, 0}, {-1, 0}, {1, 1}, {0, 1},{0, -1}, {-1, -1}, {1, -1}, {-1, 1}}) {
			    	if(checkAndFlip(x, y, dir[0], dir[1], model.getPlayer(), 0, false)) {
			    		return true;
			    	}	   
	    		}
	    	}
		}
		return false;
	}
	
	@Override
	public void squareSelected( int player, int x, int y ) {
		if(model.hasFinished() == false && player == model.getPlayer()) {
			boolean hasFlipped = false;
			if (model.getBoardContents(x, y) != 0) {
				view.feedbackToUser(player, "Invalid location to play a piece");
			}
			else {
				for (int[] dir : new int [][] {{1, 0}, {-1, 0}, {1, 1}, {0, 1},{0, -1}, {-1, -1}, {1, -1}, {-1, 1}}) {
			    	if(checkAndFlip(x, y, dir[0], dir[1], player, 0, true)) {
			    		hasFlipped = true;
			    	}	    	
				}
				if (hasFlipped) {
					model.setBoardContents(x, y, player);
					model.setPlayer(model.getPlayer() == 1 ? 2 : 1);
					update();
				}
				else {
					view.feedbackToUser(player, "Invalid location to play a piece");
				}
			}
			view.refreshView();	  
		}
		else if (!(player == model.getPlayer())){
			view.feedbackToUser(player, "It is not your turn!");
			view.refreshView();	
			return;
		}
		
	}
	
    public boolean checkAndFlip(int currentX, int currentY, int a, int b, int player, int steps, boolean shouldFlip) {
    	boolean canFlip;
    	currentX += a;
    	currentY += b;
    	
    	if (currentY >= model.getBoardHeight() || currentY < 0 || currentX >= model.getBoardWidth() || currentX < 0) {
			return false;
		}
    	else if (model.getBoardContents(currentX, currentY) == 0){
    		return false;
		}
    	else if (model.getBoardContents(currentX, currentY) != player){
    		steps++;
    		canFlip = checkAndFlip(currentX, currentY, a, b, player, steps, shouldFlip);
    		if (canFlip && shouldFlip) {
    			model.setBoardContents(currentX, currentY, player);
        	}
    		return canFlip;
    		
    	}
    	else {
    		if (steps > 0) {
    			return true;
    		}
    		else {
    			return false;
    		}
    	}
    	
    }
    

    private int checkAndFlipCount(int currentX, int currentY, int a, int b, int player, boolean shouldFlip) {
    	currentX += a;
    	currentY += b;
    	
    	if (currentY >= model.getBoardHeight() || currentY < 0 || currentX >= model.getBoardWidth() || currentX < 0) {
			return -1;
		}
    	else if (model.getBoardContents(currentX, currentY) == 0){
    		return -1;
		}
    	else if (model.getBoardContents(currentX, currentY) != player){
    		int count = checkAndFlipCount(currentX, currentY, a, b, player, shouldFlip);
    		if (count != -1){
    			return 1+count;
    		}
    		else {
    			return -1;
    		}
    	}
    	else {
    		return 0;
    	}
    }
 
	
    @Override
	public void doAutomatedMove( int player ) {
    	int mostSteps = 0;
    	int bestMoveX = 0;
    	int bestMoveY = 0;
    	for (int y = 0; y < model.getBoardHeight(); y++) {
	    	for (int x = 0; x < model.getBoardWidth(); x++) {
	    		steps = 0;
	    		if (model.getBoardContents(x, y) != 0) {
	    			continue;
	    		}
	    		for (int[] dir : new int [][] {{1, 0}, {-1, 0}, {1, 1}, {0, 1},{0, -1}, {-1, -1}, {1, -1}, {-1, 1}}) {
	    			int count = checkAndFlipCount(x, y, dir[0], dir[1], player, false);
			    	if (count != -1) {
			    		steps += count; 
			    	}
	    		}
	    		if (steps >= mostSteps) {
	    			mostSteps = steps;
	    			bestMoveX = x;
	    			bestMoveY = y;
	    		}
	    	}
		}
    	squareSelected(player,bestMoveX,bestMoveY);
	    		
    }

}