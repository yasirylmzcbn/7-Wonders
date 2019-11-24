import java.awt.*;
import java.io.File; 
import java.io.*;
import java.io.IOException; 
import java.awt.image.BufferedImage; 
import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.event.MouseListener;
public class SevenWondersPanel extends JPanel implements MouseListener
{
	private int[] posX;
	private GameState game;
	private int width;
	private int height;
	
	private boolean GameLobby;
	private boolean wonderDist;
	private boolean displayAllPlayed;
	private String displayOtherWonder;
	private boolean displayHalic;//might become a String later on
	private boolean displayGraveyard;
	
	public SevenWondersPanel(int width, int height)
	{
		super();
		game = new GameState();
		addMouseListener(this);
		//setLayout(null);
		this.width = width;
		this.height = height;
		
		GameLobby = true;
		wonderDist = false;
		
	}
	
	public void paint(Graphics g) 
	{
		if(GameLobby)
		{
			try 
			{
				BufferedImage back = ImageIO.read(new File("src/images/mainmenubackground.png"));
				g.drawImage(back, 0, 0,1200, 800, null);
				
				//g.fillRect(525, 330, 125, 70);//where PLAY is
				//g.fillRect(525, 420, 140, 70);//where QUIT is
				
			}catch(IOException e) {
				
			}
		}
		if(wonderDist)
		{
			try 
			{
				BufferedImage back = ImageIO.read(new File("src/images/background.png"));
				g.drawImage(back, 0, 0,1200, 800, null);
				
			}catch(IOException e) {
				
			}
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(GameLobby)
		{
			if(e.getX()>=525&&e.getX()<=665&&e.getY()>=420&&e.getY()<=490) //IF QUIT
			{
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
				frame.dispose();
			}
			if(e.getX()>=525&&e.getX()<=650&&e.getY()>=330&&e.getY()<=400) //IF PLAY
			{
				GameLobby = false;
				wonderDist = true;
				repaint();
			}
		}
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
