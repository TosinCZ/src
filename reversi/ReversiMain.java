package reversi;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import reversi.GUIView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;





public class ReversiMain
{
	IModel model;
	IView view;
	IController controller;

	ReversiMain()
	{
		
		model = new SimpleModel();
		//model = new SimpleTestModel();
		
		
		//view = new TextView();
		//view = new FakeTextView();
		view = new GUIView(); 
		
		
		//controller = new SimpleController();
		//controller = new TestYourGUIView();
		controller = new ReversiController(); 
		
		
		model.initialise(8, 8, view, controller);
		controller.initialise(model, view);
		view.initialise(model, controller);
	

		controller.startup();
	}
	
	public static void main(String[] args)
	{
		new ReversiMain();
	}
}
