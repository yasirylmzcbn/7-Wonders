import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SevenWondersPanel extends JPanel implements MouseListener
{
	private boolean GameLobby;
	private boolean wonderDist;
	private boolean displayAllPlayed;
	private boolean displayHalic; //might become a String later on
	private boolean displayGraveyard;
	
	private int[] posX;
	private int width;
	private int height;
	
	private GameState game;
	private String displayOtherWonder;
	
	public SevenWondersPanel(int width, int height)
	{
		super();
		addMouseListener(this);
		setLayout(null);
		setSize(width, height);
		
		game = new GameState();
		this.width = width;
		this.height = height;
		GameLobby = true;
		wonderDist = false;
		
	}
	
	public void paint(Graphics g) 
	{
		if(GameLobby)
		{
			drawMainMenu(g);
		}
		if(wonderDist)
		{
			drawBackground(g);
		}
	}
	
	// draws start and quit buttons
	public void drawMainMenu(Graphics g)
	{
		try
		{
			BufferedImage mainMenuBackground = ImageIO.read(new File("src/images/newmainmenubackground.png"));
			g.drawImage(mainMenuBackground, 0, 0, width, height, null);
		}
		catch(IOException e)
		{
			System.out.println("Cannot find background image!");
		}
	}
	
	public void drawBackground(Graphics g)
	{
		try
		{
			BufferedImage background = ImageIO.read(new File("src/images/background.png"));
			g.drawImage(background, 0, 0, width, height, null);
		}
		catch(IOException e)
		{
			System.out.println("Cannot find background image!");
		}
	}
	
	public void mousePressed(MouseEvent e)
	{
		if(GameLobby)
		{
			if(e.getX()>=805&&e.getX()<=1060&&e.getY()>=750&&e.getY()<=835) //IF QUIT
			{
				/*JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
				frame.dispose();*/
				System.exit(0);
			}
			if(e.getX()>=785&&e.getX()<=1090&&e.getY()>=595&&e.getY()<=685) //IF PLAY
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
