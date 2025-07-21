package reversi;


/**
 * Test program to test the GUI view.
 * You are going to do almost all of the work to check this yourself, and I give you a list of steps to check below.
 * This basically provides you with a dummy version of some methods, so you can see what is happening.
 * This is a very cut-down version of my test class, which will do a lot more checks, and do a lot of automatic checks.
 * 
 * When you are ready to test your view, find the line below that says TODO, in main(),
 * and change which of the following lines is commented out
 * Then run this class.
 * (I had to do this otherwise it would not compile until you create the GUIView)
 * 
 * In general the GUI is easier to test as there are not many things it does, however using this class will catch if you added any special code which relied upon your view and controller working together.
 * If this program cannot run your code, or your program crashes when you run this (e.g. class cast exception) then you tried to do something unusual - so please fix before submission.
 * 
 * Run up the program with this file as the main class.
 * 
 * Manual things to check before you start pressing anything:
 * Ensure that two frames appear.
 * Check that each frame title is correct.
 * Ensure that one is for player white and one for player black.
 * When you have played a few pieces, ensure that they are rotations of each other - i.e. top left for white is bottom right for black.
 * Ensure that pieces are oval with a different coloured outline on the outside.
 * Note: this program won't ensure you didn't use images from the file system, but my test harness will so please ensure that you do not access the file system even for standard images.
 * Ensure that there is a feedback label at the top. The steps below tell you what this will show so you can test it works.
 * Ensure that there are two buttons at the bottom, one above the other.
 * The top button should say Greedy AI (play black [or white])
 * The bottom button should say Restart
 *  
 * Now you can do more steps:
 * Manually use your GUI and check that the feedback messages come back to tell you that the features worked.
 * 1) Press greedy AI move for black and ensure that it puts between 6 and 10 pieces into the next blank squares. Starting 0,0.
 * You should get a feedback message to that player saying where the last piece was placed (i.e. check that your feedback messages appear)
 * The pieces should appear on the screen (i.e. check that your refreshView correctly showed the ovals on the buttons).
 * Repeat this a few times to ensure that they show.
 * 
 * 2) Now repeat this for player white. The pieces should carry on from where the others left off.
 * If you get to the end it should clear the board and start again.
 * Check that pieces appear and the feedback message appears saying where the move was played.
 * 
 * Ensure that if the AI player is used to fill the board:
 * You can still put pieces in manually.
 * Pressing the AI player again will clear the board. 
 * This is a test to ensure that your user interface is not checking for 'finished' - the controller should be the one implementing any logic.
 * i.e. even when the board is full, both the clicking in the board and the button to make an AI move still work.
 * 
 * 3) Ensure that you have a few pieces on the board, and press the Restart button on player black's frame.
 * Ensure that all pieces vanish from the board.
 * Ensure that the feedback message now says startup() was called successfully.
 * 
 * 4) Manually click a square as black.
 * Check that a black piece appears in that square.
 * Check that the feedback message says "You last played in " and the coordinates of the square which was clicked.
 * 
 * 5) Repeat this for player white.
 * Again check that the piece appears and that the feedback message is correct.
 * 
 * 6) Ensure that you have a few pieces on the board, and press the Restart button on player white's frame.
 * Ensure that all pieces vanish from the board.
 * Ensure that the feedback message now says startup() was called successfully.
 * 
 * If all of these tests were passed successfully then your GUIView should get all of the marks.
 * For testing, this is what how I intend to test it - by displaying it and checking these things.
 */
public class TestYourGUIView extends SimpleModel implements IController 
{
	public static void main(String[] args)
	{
		TestYourGUIView tester = new TestYourGUIView();
		
		//// Choose ONE of the models
		//IModel model = new SimpleModel();
		IModel model = tester;

		// TODO - Choose one of the following only:
		//IView view = new FakeTextView(); // Comment this out
		IView view = new GUIView(); // Uncomment this out, so it is executed
		
		IController controller = tester;
		
		// Don't change the lines below here, which connect things together
		
		// Initialise everything...
		model.initialise(8, 8, view, controller);
		controller.initialise(model, view);
		view.initialise(model, controller);
		
		// Now start the game - set up the board
		controller.startup();
	}

	
	IModel model;
	IView view;
	private int steps;
	private boolean turnMissed;
	
	java.util.Random rand = new java.util.Random();

	
	
	@Override
	public void initialise(IModel model, IView view)
	{
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
    		view.feedbackToUser(1, "White won. White"+ whiteToken +"to Black"+ blackToken +". Reset game to replay.");
    		view.feedbackToUser(2, "White won. White"+ whiteToken +"to Black"+ blackToken +". Reset game to replay.");
    	}
    	else if (blackToken > whiteToken) {
    		view.feedbackToUser(1, "Black won. Black"+ blackToken +"to White"+ whiteToken +". Reset game to replay.");
    		view.feedbackToUser(2, "Black won. Black"+ blackToken +"to White"+ whiteToken +". Reset game to replay.");
    	}
    	else {
    		view.feedbackToUser(1, "Draw. Both players ended with"+ blackToken +"pieces. Reset game to replay.");
    		view.feedbackToUser(2, "Draw. Both players ended with"+ blackToken +"pieces. Reset game to replay.");
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
			view.refreshView();	  
		}
		else {
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
