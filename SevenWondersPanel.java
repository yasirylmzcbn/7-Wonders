import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SevenWondersPanel extends JPanel implements MouseListener
{
	public static final int HANDXPOS = 5, HANDYPOS = 5; // where the images of player's hand will start from
	public static final int CARDWIDTH = 180, CARDHEIGHT = 275;
	public static final int WONDERXPOS = 1120, WONDERYPOS = 720;
	public static final int WONDERWIDTH = 756, WONDERHEIGHT = 313; // multiplied by 1.25
	//public static final int WONDERWIDTH = 605, WONDERHEIGHT = 250; // original
	
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
			drawHand(g);
			drawWonder(g);
		}
		if (displayAllPlayed)
		{
			
		}
		if (displayHalic)
		{
			
		}
		if (displayGraveyard)
		{
			
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
	
	public void drawHand(Graphics g)
	{
		// hand of current player
		ArrayList<Card> currentHand = game.getPlayerHands().get(game.getWonders().get(game.getCurrentPlayer()).getHand());
		
		// Card names with no spaces and to lower case in order to match image file names
		ArrayList<String> playerHandNames = new ArrayList<String>();
		for (int i = 0; i < currentHand.size(); i++)
			playerHandNames.add(currentHand.get(i).getName().replace(" ", "").toLowerCase());
		
		// assign card images
		BufferedImage cards[] = new BufferedImage[playerHandNames.size()];
		for (int i = 0; i < cards.length; i++)
		{
			try {
				cards[i] = ImageIO.read(new File("src/images/cards/" + playerHandNames.get(i) + ".png"));
			} catch (IOException e) {
				System.out.println("Cannot find file " + playerHandNames.get(i));
			}
		}
		
		// draw card images
		for (int i = 0; i < cards.length; i++)
		{
			g.drawImage(cards[i], HANDXPOS + (CARDWIDTH + 10) * i, HANDYPOS, null);
		}
	}
	
	// draws player wonder along with the coins, military power, wins, and losses
	public void drawWonder(Graphics g)
	{
		// wonder of current player
		Wonder currentWonder = game.getWonders().get(game.getCurrentPlayer());
		
		// assigns image to wonder
		BufferedImage wonder = null;
		try
		{
			wonder = ImageIO.read(new File("src/images/wonders/" + currentWonder.getName() + ".png"));
		}
		catch (IOException e)
		{
			System.out.println("Cannot find " + currentWonder.getName());
		}
		
		g.drawImage(wonder, WONDERXPOS, WONDERYPOS, WONDERWIDTH, WONDERHEIGHT, null);
		
		// draws the stats
		
		
		int i = 1;
		int starting = WONDERYPOS + 70;
		g.setColor(new Color(0, 0, 0, 100));
		g.fillRect(WONDERXPOS, starting, 130, 120);
		
		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman", Font.BOLD, 16));
		g.drawString(currentWonder.getMoney() + " Coins", WONDERXPOS + 5, starting + (i++ * 25));
		g.drawString(currentWonder.getMilitaryPower() + " Military Power", WONDERXPOS + 5, starting + (i++ * 25));
		g.drawString(currentWonder.getWins() + " Wins", WONDERXPOS + 5, starting + (i++ * 25));
		g.drawString(currentWonder.getLosses() + " Losses", WONDERXPOS + 5, starting + (i++ * 25));
	}
	
	public void mousePressed(MouseEvent e)
	{
		System.out.println(e.getX() + ", " + e.getY());
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
