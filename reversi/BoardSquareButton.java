package reversi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;

public class BoardSquareButton extends JButton{
	Color drawColor; 
	Color borderColor;
	Color outline;
	int borderSize; 
    private int x,y;
    private IModel model;
	
	public BoardSquareButton( int width, int height,int borderWidth, Color borderCol, int xpos, int ypos, IModel model )
	{
		borderSize = borderWidth;
		borderColor = borderCol;
		setMinimumSize( new Dimension(width, height) );
		setPreferredSize( new Dimension(width, height) );
		x = xpos;
		y = ypos;
		this.model = model;
	}

	public BoardSquareButton( int width, int height, int xpos, int ypos, IModel model ){
		this(width, height, 0, null, xpos, ypos, model);
	}
	
	public int getXCoordinates() {
		return x;
	}
	
	public int getYCoordinates() {
		return y;
	}
	
	public Color getDrawColor()
	{
		return drawColor;
	}

	public void setDrawColor(Color drawColor)
	{
		this.drawColor = drawColor;
	}

	public Color getBorderColor()
	{
		return borderColor;
	}

	public void setBorderColor(Color borderColor)
	{
		this.borderColor = borderColor;
	}

	public int getBorderSize()
	{
		return borderSize;
	}

	public void setBorderSize(int borderSize)
	{
		this.borderSize = borderSize;
	}

	protected void paintComponent(Graphics g)
	{
		switch( model.getBoardContents(x, y) )
		{
			case 1:		
				drawColor = Color.WHITE;
				outline = Color.BLACK;
				break;
			case 2:	
				drawColor = Color.BLACK;
				outline = Color.WHITE;
				break;
			default:	
				drawColor = null;
				outline = null;
				break;
		}
		super.paintComponent(g);
		if ( borderColor != null )
		{
			g.setColor(borderColor);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if ( drawColor != null )
		{
			g.setColor(drawColor);
			g.fillOval(borderSize, borderSize, getWidth()-borderSize*2, getHeight()-borderSize*2);
		}
		if ( outline != null )
		{
			g.setColor(outline);
			g.drawOval(borderSize, borderSize, getWidth()-borderSize*2, getHeight()-borderSize*2);
		}
	}
	
	

}
