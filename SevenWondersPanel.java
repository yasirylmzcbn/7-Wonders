import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	public static final int WONDERWIDTH = 756, WONDERHEIGHT = 313; // original image size is 605, 250
	public static final Color TRANSPARENTBLACK = new Color(0, 0, 0, 150); // used for shadows and to contrast against background for text
	
	private boolean mainMenu; // shows the start button
	private boolean wonderDist; // Shows distribution of wonders graphically (What wonders are in the current game)
	private boolean defaultView; // Shows hand, wonder, and resources
	private boolean displayAllPlayed;
	private boolean displayHalic; //might become a String later on
	private boolean displayGraveyard; // for Halicarnassus
	
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
		mainMenu = true;
		wonderDist = false;
		defaultView = false;
		
	}
	
	public void paint(Graphics g) 
	{
		// Anti-aliases text so that it is smooth
		((Graphics2D) g).setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		if(mainMenu)
		{
			drawMainMenu(g);
		}
		if(wonderDist)
		{
			drawWonderDist(g);
		}
		if (defaultView)
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
	
	// draws the image in the background
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
	
	// Draws which wonders will be in the current game, graphically
	public void drawWonderDist(Graphics g)
	{
		drawBackground(g);

		// all chosen wonders
		Wonder[] chosenWonders = new Wonder[game.getWonders().size()];
		for (int i = 0; i < chosenWonders.length; i++)
			chosenWonders[i] = game.getWonders().get(i);
		
		BufferedImage wonders[] = new BufferedImage[chosenWonders.length];

		// gets images for all wonders
		for (int i = 0; i < wonders.length; i++)
		{
			try
			{
				wonders[i] = ImageIO.read(new File("src/images/wonders/" + chosenWonders[i].getName() + ".png"));
			}
			catch (IOException e)
			{
				System.out.println("Cannot find " + chosenWonders[i].getName());
			}
		}
		
		// position of where the wonders will be draws
		int xpos = 677;
		int ypos = 180;
		
		g.setColor(TRANSPARENTBLACK);
		
		// draws the wonders
		for (int i = 0; i < wonders.length; i++)
		{
			// draws shadows for wonder boards
			g.fillRect( xpos + 10, 10 + ypos + (WONDERHEIGHT - 20) * i, (int)(0.75 * WONDERWIDTH), (int)(0.75 * WONDERHEIGHT));
			// draws wonders themselves
			g.drawImage(wonders[i], xpos, ypos + (WONDERHEIGHT - 20) * i, (int)(0.75 * WONDERWIDTH), (int)(0.75 * WONDERHEIGHT), null);
		}
		
		// draws text "wonder distribution"
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 64));
		g.setColor(TRANSPARENTBLACK);
		g.drawString("WONDER DISTRIBUTION", 633, 123);
		g.setColor(Color.white);
		g.drawString("WONDER DISTRIBUTION", 630, 120);
		
		// draws text "continue ->"
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 72));
		g.setColor(TRANSPARENTBLACK);
		g.drawString("CONTINUE ->", 1333, 618);
		g.setColor(Color.white);
		g.drawString("CONTINUE ->", 1330, 615);
		
		// draws text "<- quit"
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 72));
		g.setColor(TRANSPARENTBLACK);
		g.drawString("<- QUIT", 323, 618);
		g.setColor(Color.white);
		g.drawString("<- QUIT", 320, 615);
		
		
	}
	
	public void drawHand(Graphics g)
	{
		// hand of current player
		ArrayList<Card> currentHand = game.getPlayerHands().get(game.getWonders().get(game.getCurrentPlayer()).getHand());
		
		// selected card will be lowered graphically
		int selected = -1;
		
		// Card names with no spaces and to lower case in order to match image file names
		ArrayList<String> playerHandNames = new ArrayList<String>();
		for (int i = 0; i < currentHand.size(); i++)
		{
			if (currentHand.get(i).equals(game.getWonders().get(game.getCurrentPlayer()).getSelectedCard()))
				selected = i;
			playerHandNames.add(currentHand.get(i).getName().replace(" ", "").toLowerCase());
		}
		
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
			if (i != selected)
			{
				// draws shadow
				g.setColor(TRANSPARENTBLACK);
				g.fillRect(5 + HANDXPOS + (CARDWIDTH + 10) * i, 5 + HANDYPOS, CARDWIDTH, CARDHEIGHT);
				
				
				g.drawImage(cards[i], HANDXPOS + (CARDWIDTH + 10) * i, HANDYPOS, null);
			}
			else
			{
				// draws shadow
				g.setColor(TRANSPARENTBLACK);
				g.fillRect(5 + HANDXPOS + (CARDWIDTH + 10) * i, 5 + HANDYPOS + 20, CARDWIDTH, CARDHEIGHT);
				
				
				g.drawImage(cards[i], HANDXPOS + (CARDWIDTH + 10) * i, HANDYPOS + 20, null);
			}
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
		
		// draws shadow
		g.fillRect(WONDERXPOS + 10, WONDERYPOS + 10, WONDERWIDTH, WONDERHEIGHT);
		// draws wonder
		g.drawImage(wonder, WONDERXPOS, WONDERYPOS, WONDERWIDTH, WONDERHEIGHT, null);
		
		// draws the stats
		int i = 1;
		int starting = WONDERYPOS + 70;
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(WONDERXPOS, starting, 130, 120);
		
		g.setColor(Color.white);
		g.setFont(new Font("TimesRoman", Font.BOLD, 16));
		g.drawString(currentWonder.getMoney() + " Coins", WONDERXPOS + 5, starting + (i++ * 25));
		g.drawString(currentWonder.getMilitaryPower() + " Military Power", WONDERXPOS + 5, starting + (i++ * 25));
		g.drawString(currentWonder.getWins() + " Wins", WONDERXPOS + 5, starting + (i++ * 25));
		g.drawString(currentWonder.getLosses() + " Losses", WONDERXPOS + 5, starting + (i++ * 25));
	}
	
	//draws resource selection
	//Add check to selected resources
	public void drawResourceSelection(Graphics g)
	{
		
	}
	
	public void mousePressed(MouseEvent e)
	{
		System.out.println(e.getX() + ", " + e.getY()); // for debugging and testing
		
		if(mainMenu)
		{
			if(e.getX()>=805&&e.getX()<=1060&&e.getY()>=750&&e.getY()<=835) //IF QUIT
			{
				System.exit(0);
			}
			if(e.getX()>=785&&e.getX()<=1090&&e.getY()>=595&&e.getY()<=685) //IF PLAY
			{
				mainMenu = false;
				wonderDist = true;
			}
		}
		if (wonderDist)
		{
			if(e.getX()>=320&&e.getX()<=560&&e.getY()>=550&&e.getY()<=630) //IF QUIT
			{
				System.exit(0);
			}
			if(e.getX()>=1320&&e.getX()<=1765&&e.getY()>=555&&e.getY()<=625) //IF CONTINUE
			{
				wonderDist = true;
				defaultView = true;
			}
		}
		
		if (defaultView)
		{
			// if selected a card in hand
			if (e.getX() >= 5 && e.getX() <= 1325 && e.getY() >= 5 && e.getY() <= 280)
			{
				// goes through each card in the hand to check for the click
				Wonder current = game.getWonders().get(game.getCurrentPlayer());
				ArrayList<Card> currentHand = game.getPlayerHands().get(current.getHand());
				for (int i = 0; i < currentHand.size(); i++)
				{
					// this is the bounds for each card of index 'i'
					// funcion is g(i) = 195x + 5, or g(i) = 5 (initial offset of card) + 10i (space between each card) + 185i (width of each card)
					if (e.getX() >= 195 * i + 5 && e.getX() <= 195 * i + 185 && e.getY() >= 5 && e.getY() <= 280)
					{
						// temporary code for debugging
						System.out.println("Chosen card " + currentHand.get(i).getName());
						current.setSelectedCard(currentHand.get(i));
					}
					
				}
			}
		}
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
