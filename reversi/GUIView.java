package reversi;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class GUIView implements IView, ActionListener {

    // References you'll store in initialise()
    private IModel model;
    private IController controller;
    private JLabel message1 = new JLabel();
    private JLabel message2 = new JLabel();
	private JFrame frame1 = new JFrame();
	private JFrame frame2 = new JFrame();
	private JPanel board1;
	private JPanel board2;
	private BoardSquareButton [][] board1Buttons;
	private BoardSquareButton [][] board2Buttons;

    @Override
    public void initialise(IModel model, IController controller ) {
        this.model = model;
        this.controller = controller;
        int  height = model.getBoardHeight();
        int width = model.getBoardWidth();
        board1Buttons = new BoardSquareButton[height][width];
        board2Buttons = new BoardSquareButton[height][width];
        
        // Frame setup
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setTitle("Reversi White Interface");
        frame1.setLocationRelativeTo(null);
        frame1.getContentPane().setLayout(new GridLayout(1, 2));
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setTitle("Reversi Black Interface");
        frame2.setLocationRelativeTo(null);
        frame2.getContentPane().setLayout(new GridLayout(1, 2));

        // Panel for player 1
        board1 = new JPanel(new GridLayout(height, width));
        board1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel p1Panel = new JPanel(new BorderLayout());
        p1Panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 3));
        frame1.getContentPane().add(p1Panel);

        // Panel for player 2
        board2 = new JPanel(new GridLayout(height, width));
        board2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel p2Panel = new JPanel(new BorderLayout());
        p2Panel.setBorder( BorderFactory.createLineBorder(Color.BLACK,3) );
        frame2.getContentPane().add(p2Panel);
        
        board1.setName("Board 1");
        board2.setName("Board 2");
        
        // Fonts and board sizes
        message1.setFont(new Font("Arial", Font.BOLD, 20));
        message2.setFont(new Font("Arial", Font.BOLD, 20));
        board1.setPreferredSize(new Dimension(400, 330));
        board2.setPreferredSize(new Dimension(400, 330));
        
        
        for (int y = 0; y < height; y++) {
        	for (int x = 0; x < width; x++) {
	        	BoardSquareButton tile = new BoardSquareButton(50,50,2,Color.GREEN,x,y,model);
	        	tile.addActionListener(this);
	        	board1Buttons[y][x] = tile;
	        	board1.add(tile);
        	}
        }
        
        for (int y = height-1 ; y >= 0 ; y--) {
        	for (int x = width-1 ; x >= 0 ; x-- ) {
	        	BoardSquareButton tile = new BoardSquareButton(50,50,2,Color.GREEN,x,y,model);
	        	tile.addActionListener(this);
	        	board2Buttons[y][x] = tile;
	        	board2.add(tile);
        	}
        }
        
        // Player 1 UI
        message1.setText("Player 1 (White)");
        p1Panel.add(message1, BorderLayout.NORTH);
        p1Panel.add(board1, BorderLayout.CENTER);

        // Player 2 UI
        message2.setText("Player 2 (Black)");
        p2Panel.add(message2, BorderLayout.NORTH);
        p2Panel.add(board2, BorderLayout.CENTER);

        // Create button panel for AI buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));

        JButton butAI1 = new JButton("Greedy AI (play white)");
        butAI1.addActionListener(e -> controller.doAutomatedMove(1));
        buttonPanel.add(butAI1);

        JButton butRestart1 = new JButton("Restart");
        butRestart1.addActionListener(e -> {
        	controller.startup();
        });
        buttonPanel.add(butRestart1);

        // Add button panel to bottom of p2Panel
        p1Panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Button panel for frame2 (Black)
        JPanel buttonPanel2 = new JPanel(new GridLayout(2, 1));
        JButton butAI2 = new JButton("Greedy AI (play black)");
        butAI2.addActionListener(e -> controller.doAutomatedMove(2));
        buttonPanel2.add(butAI2);

        JButton butRestart2 = new JButton("Restart");
        butRestart2.addActionListener(e -> {
        	controller.startup();
        });
        buttonPanel2.add(butRestart2);
        p2Panel.add(buttonPanel2, BorderLayout.SOUTH);

        // Finalize frame
        frame1.pack();
        frame1.setVisible(true);
        frame2.pack();
        frame2.setVisible(true);

    }
    
    @Override
	public void actionPerformed(ActionEvent e)
	{
    	Object source = e.getSource();

        if (source instanceof BoardSquareButton) {
        	BoardSquareButton button = (BoardSquareButton) source;
        	JPanel parentPanel = (JPanel)button.getParent();
        	int x = button.getXCoordinates();
        	int y = button.getYCoordinates();
        	switch(parentPanel.getName()) {
        		case "Board 1":
        			controller.squareSelected(1,x,y);
        			break;
        		case "Board 2":
        			controller.squareSelected(2,x,y);
        			break;
        	}
        }
	}
 
    @Override
    public void refreshView() {
    	for (BoardSquareButton[] row : board1Buttons) {
    		for (BoardSquareButton tile : row) {
    			tile.repaint();
    		}
    	}
    	for (BoardSquareButton[] row : board2Buttons) {
    		for (BoardSquareButton tile : row) {
    			tile.repaint();
    		}
    	}
    }

    @Override
    public void feedbackToUser(int player, String message) {
    	if ( player == 1 )
			message1.setText(message);
		else if ( player == 2 )
			message2.setText(message);
    }
    
}
