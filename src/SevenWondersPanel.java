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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SevenWondersPanel extends JPanel implements MouseListener
{
	public static final int HANDXPOS = 5, HANDYPOS = 5; // where the images of player's hand will start from
	public static final int CARDWIDTH = 180, CARDHEIGHT = 275;
	public static final int WONDERXPOS = 1120, WONDERYPOS = 720;
	public static final int WONDERWIDTH = 756, WONDERHEIGHT = 313; // original image size is 605, 250
	
	public static final Color TRANSPARENTBLACK = new Color(0, 0, 0, 150); // used for shadows and to contrast against background for text
	public static final Color GREEN = new Color(198, 227, 203),
			BLUE = new Color(194, 220, 249),
			GREY = new Color(229, 229, 229), //54, 126, 209
			YELLOW = new Color(248, 224, 158),
			RED = new Color(248, 147, 144),
			//BROWN = new Color(207, 178, 172); 
			BROWN = new Color(101, 67, 33), // I found a better brown
			PURPLE = new Color(144, 120, 232);
	
	private boolean mainMenu; // shows the start button
	private boolean wonderDist; // Shows distribution of wonders graphically (What wonders are in the current game)
	private boolean optionSelection; // allows player to choose to play, build, burn, or use wonder ability
	private boolean defaultView; // Shows hand, wonder, and resources
	private String displayView;
	private String displayColor;
	//private boolean displayLeftPlayed;
	//private boolean displayRightPlayed;

	private boolean displayHalic; //might become a String later on
	private boolean displayGraveyard; // for Halicarnassus
	
	public static boolean OlympiaAbility = false;
	
	//private int[] posX;
	private int width;
	private int height;
	
	private GameState game;
	
	private boolean olympiaE, chainE, freeE, resE; // 'error' if choosing resources with olympia, chain, or free card, or wrong resources selected
	
	//private String displayOtherWonder;
	
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
		optionSelection = false;
		displayView = "";
		displayColor = "";
		OlympiaAbility = false;
		displayHalic = false;
		displayGraveyard = false;
		
		olympiaE = false;
		chainE = false;
		freeE = false;
		resE = false;

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
			if (optionSelection)
				drawOptionSelection(g);
			else
			{
				drawResourceSelection(g);
			}
			drawRoundInfo(g);
		}
		if (displayView!="")
		{
			drawBackground(g);
			drawOtherWonder(g);
		}
		if (displayHalic)
		{
			drawGraveyard(g);
		}
		if(game.isEndOfGame())
		{
			defaultView= false;
			displayView = "";
			wonderDist = false;
			drawFinalPoints(g);
		}
		drawError(g);
		
	}
	
	public void drawError(Graphics g)
	{
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 38));
		g.setColor(Color.red);
		if (olympiaE)
			g.drawString("Card free with Olympia", 1380, 150);
		else if (chainE)
			g.drawString("Card is in chain", 1380, 150);
		else if (freeE)
			g.drawString("Card is free", 1380, 150);
		else if(resE)
			g.drawString("Incorrect resources", 1380, 150);
	}
	
	// draws start and quit buttons
	public void drawMainMenu(Graphics g)
	{
		try
		{
			BufferedImage mainMenuBackground = ImageIO.read(new File("assets/images/newmainmenubackground.png"));
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
			BufferedImage background = ImageIO.read(new File("assets/images/background.png"));
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
				wonders[i] = ImageIO.read(new File("assets/images/wonders/" + chosenWonders[i].getName() + ".png"));
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
		ArrayList<Card> currentHand = game.getPlayerHands().get(game.getCurrentWonder().getHand());
		
		// selected card will be lowered graphically
		int selected = -1;
		
		// Card names with no spaces and to lower case in order to match image file names
		ArrayList<String> playerHandNames = new ArrayList<String>();
		for (int i = 0; i < currentHand.size(); i++)
		{
			if (currentHand.get(i).equals(game.getCurrentWonder().getSelectedCard()))
				selected = i;
			playerHandNames.add(currentHand.get(i).getName().replace(" ", "").toLowerCase());
		}
		
		// assign card images
		BufferedImage cards[] = new BufferedImage[playerHandNames.size()];
		for (int i = 0; i < cards.length; i++)
		{
			try {
				cards[i] = ImageIO.read(new File("assets/images/cards/" + playerHandNames.get(i) + ".png"));
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
		Wonder currentWonder = game.getCurrentWonder();
		
		// assigns image to wonder
		BufferedImage wonder = null, backCard = null;
		try
		{
			wonder = ImageIO.read(new File("assets/images/wonders/" + currentWonder.getName() + ".png"));
			backCard = ImageIO.read(new File("assets/images/cards/age1.png"));
		}
		catch (IOException e)
		{
			System.out.println("Cannot find " + currentWonder.getName());
		}
		
		// draws shadow
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(WONDERXPOS + 10, WONDERYPOS + 10, WONDERWIDTH, WONDERHEIGHT);
		
		// draws indicator that wonder has been built
		for (int w = 0; w < currentWonder.getPlayerWonders(); w++)
			g.drawImage(backCard, 1180 + (CARDWIDTH + 50) * w, 790, CARDWIDTH, CARDHEIGHT, null);
		// draws wonder
		g.drawImage(wonder, WONDERXPOS, WONDERYPOS, WONDERWIDTH, WONDERHEIGHT, null);
		
		//draws the check marks that show which wonder has been built
		try
		{
			BufferedImage check = ImageIO.read(new File("assets/images/selected.png"));
			int checkw = 50;
			int checkh = 50;
			if(currentWonder.getPlayerWonders()>=1)
				g.drawImage(check, 1314, 994, checkw, checkh, null);
			if(currentWonder.getPlayerWonders()>=2)
				g.drawImage(check, 1534, 994, checkw, checkh, null);
			if(currentWonder.getPlayerWonders()>=3)
				g.drawImage(check, 1763, 994, checkw, checkh, null);
		}
		catch (IOException e)
		{
			System.out.println("Cannot find check");
		}
		
		
		
		
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
		
		
		Color altgreen = new Color(50, 170, 140);
		
		g.setFont(new Font("Berlin Sans SB", Font.BOLD, 14));
		g.setColor(altgreen);
		g.fillRect(1375, 684, 238, 67); // displayAllCurrent
		g.setColor(Color.white);
		g.drawString("Display All of Your Played Cards", 1383 , 720);
		
		
		
		g.setColor(altgreen);
		g.fillRect(1738, 40, 149, 125); // displayLeftWonder 
		g.setColor(Color.white);
		//g.setFont(new Font("Berlin Sans SB", Font.BOLD, 14));
		g.drawString("Left Neighbor's", 1755 , 100);
		g.drawString("Situation", 1785 , 120);
		
		g.setColor(altgreen);
		g.fillRect(1738, 190, 149, 125); // displayRightWonder(40+125+25(space) = 190)
		g.setColor(Color.white);
		g.drawString("Right Neighbor's", 1755 , 250);
		g.drawString("Situation", 1785 , 270);
	}
	
	public void drawOptionSelection(Graphics g)
	{
		// Container that holds text
		
		// shadow of container
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(40, 460, 1025, 580);
		
		// sets colour of container based on the currently selected card's colour
		Card col = game.getCurrentWonder().getSelectedCard();
		if (col != null)
		{
			if (col.getColor().equals("green"))
				g.setColor(GREEN);
			else if (col.getColor().equals("blue"))
				g.setColor(BLUE);
			else if (col.getColor().equals("silver"))
				g.setColor(GREY);
			else if (col.getColor().equals("yellow"))
				g.setColor(YELLOW);
			else if (col.getColor().equals("red"))
				g.setColor(RED);
			else if (col.getColor().equals("brown"))
				g.setColor(BROWN);
			else if (col.getColor().equals("purple"))
				g.setColor(PURPLE);
				g.setColor(new Color(255, 245, 222));
		}
		else
			g.setColor(new Color(255, 245, 222));
		g.fillRect(30, 450, 1025, 580);
		
		
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
		g.setColor(Color.BLACK);
		g.drawString("CHOOSE YOUR ACTION", 280, 590);
		g.drawString("Play", 495, 660);
		g.drawString("Build Wonder", 395, 740); // I changed to Build Wonder for clarification also it was 485, 740
		g.drawString("Burn", 495, 820);
		if (game.canUseOlympia()) // should be Olympia only bc Halic is automatic
			g.drawString("Ability", 475, 900);
	}
	
	public void drawResourceSelection(Graphics g)
	{
		try
		{
			// Container that holds the resource images

			// shadow of container
			g.setColor(TRANSPARENTBLACK);
			g.fillRect(40, 460, 1025, 580);
			
			// sets colour of container based on the currently selected card's colour
			Card col = game.getCurrentWonder().getSelectedCard();
			if (col != null)
			{
				if (col.getColor().equals("green"))
					g.setColor(GREEN);
				else if (col.getColor().equals("blue"))
					g.setColor(BLUE);
				else if (col.getColor().equals("silver"))
					g.setColor(GREY);
				else if (col.getColor().equals("yellow"))
					g.setColor(YELLOW);
				else if (col.getColor().equals("red"))
					g.setColor(RED);
				else if (col.getColor().equals("brown"))
					g.setColor(BROWN);
				else if (col.getColor().equals("purple"))
					g.setColor(PURPLE);
				else
					g.setColor(new Color(255, 245, 222));
			}
			else
				g.setColor(new Color(255, 245, 222));
			g.fillRect(30, 450, 1025, 580);
			
			if(game.getCurrentWonder().getAction().contentEquals("Play")||game.getCurrentWonder().getAction().contentEquals("Build")&&!OlympiaAbility)//&&!game.isHalic()
			{
				g.setColor(Color.WHITE);
				g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
				g.drawString("Resource Selection", 350, 370);
				// resource name (including coin) and their images
				HashMap<String, BufferedImage> resources = new HashMap<String, BufferedImage>();
				File imageFiles[] = new File("assets/images/resources/").listFiles();
				BufferedImage selected = ImageIO.read(new File("assets/images/selected.png"));
				BufferedImage OR = ImageIO.read(new File("assets/images/OR.png"));
				
				// imports resource images
				for (int i = 0; i < imageFiles.length; i++)
				{
					//System.out.println(imageFiles[i].getName());
					if(imageFiles[i].getName().contains(".png"))
						resources.put(imageFiles[i].getName().substring(0, imageFiles[i].getName().indexOf(".png")), ImageIO.read(imageFiles[i]));
				}
				
				// drawing the coins
				int SilverCost = 2; // cost of trading for a silver card resource
				int RightBrownCost = 2;// cost for trading for a brown card resource to the right
				int LeftBrownCost = 2;//^*left
				
				String brownR = "wood-Selected stone-Selected clay-Selected ore-Selected";
	            String silverR = "paper-Selected cloth-Selected glass-Selected";
				
				ArrayList<Card> crds = new ArrayList<Card>();
				crds.addAll(game.getCurrentWonder().getCardsPlayed().get("yellow"));
				for(int i = 0; i<crds.size(); i++ )
				{
					if(crds.get(i).getName().equals("East Trading Post"))
					{
						RightBrownCost = 1;
					}
					if(crds.get(i).getName().equals("West Trading Post"))
					{
						LeftBrownCost = 1;
					}
					if(crds.get(i).getName().equals("Marketplace"))
					{
						SilverCost = 1;
					}
				}
				
				// left resources
				int startingX = 75, startingY = 470, space = 40, coinSize = 20;
				String[] leftResources = game.getLeftSelected();
				for (int i = 0; i < leftResources.length; i++)
				{
					// if || card
					if (leftResources[i].contains("||"))
					{
						String[] split = leftResources[i].split("\\|\\|");
						for (int s = 0; s < split.length; s++)
						{
							// System.out.print(split[s] + " and ");
							g.drawImage(resources.get(split[s].split("-Selected")[0].split("\\|\\|")[0]), startingX + (space + 5) * s, startingY + (space + 10) * i, space, space, null);
							if (split[s].split("\\|\\|")[0].contains("-Selected"))
								g.drawImage(selected, startingX + (space + 5) * s, startingY + (space + 10) * i, space, space, null);
							// draws the slash between resource
							if (s < split.length - 1)
								g.drawImage(OR, startingX + 35 + (space + 5) * s, startingY + 2 + (space + 10) * i, 16, 35, null);
						}
						System.out.println();
						// add a selected check mark
					}
					else
					{
						g.drawImage(resources.get(leftResources[i].split("-Selected")[0]), startingX, startingY + (space + 10) * i, space, space, null);
						if (leftResources[i].contains("-Selected"))
							g.drawImage(selected, startingX, startingY + (space + 10) * i, space, space, null);
						
					}
					if (brownR.contains(leftResources[i].split("\\|\\|")[0]))
						for (int j = 0; j < LeftBrownCost; j++)
							g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (space + 10) * i + (coinSize) * j, coinSize, coinSize, null);
					if (silverR.contains(leftResources[i].split("\\|\\|")[0]))
						for (int j = 0; j < SilverCost; j++)
							g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (space + 10) * i + (coinSize) * j, coinSize, coinSize, null);
				}
				// temporary test
				/*for (int i = 0; i < 15; i++)
				{
					g.drawImage(resources.get("clay"), startingX, startingY + (space + 10) * i, space, space, null);
					g.drawImage(resources.get("clay"), space + startingX, startingY + (space + 10) * i, space, space, null);
					g.drawImage(resources.get("clay"), 2*space +startingX, startingY + (space + 10) * i, space, space, null);
					g.drawImage(resources.get("clay"), 3*space +startingX, startingY + (space + 10) * i, space, space, null);
						
				}*/
				
				
				// own resources
				startingX = 415;
				String[] ownResources = game.getOwnSelected();
				for (int i = 0; i < ownResources.length; i++)
				{
					// if || card
					if (ownResources[i].contains("||"))
					{
						String[] split = ownResources[i].split("\\|\\|");
						for (int s = 0; s < split.length; s++)
						{
							// System.out.print(split[s] + " and ");
							g.drawImage(resources.get(split[s].split("-Selected")[0].split("\\|\\|")[0]), startingX + (space + 5) * s, startingY + (space + 10) * i, space, space, null);
							if (split[s].split("\\|\\|")[0].contains("-Selected"))
								g.drawImage(selected, startingX + (space + 5) * s, startingY + (space + 10) * i, space, space, null);
							if (s < split.length - 1)
								g.drawImage(OR, startingX + 35 + (space + 5) * s, startingY + 2 + (space + 10) * i, 16, 35, null);
						}
						System.out.println();
						// add a selected check mark
					}
					else if (!ownResources[i].contains("coin"))
					{
						g.drawImage(resources.get(ownResources[i].split("-Selected")[0]), startingX, startingY + (space + 10) * i, space, space, null);
						if (ownResources[i].contains("-Selected"))
							g.drawImage(selected, startingX, startingY + (space + 10) * i, space, space, null);
					}
				}
				// draws coins
				int pos = 0;
				for (int i = 0; i < ownResources.length; i++)
				{
					if (ownResources[i].contains("coin") && pos < 11)
					{
						g.drawImage(resources.get(ownResources[i].split("-Selected")[0]), startingX + 225, startingY + (space + 10) * pos, space, space, null);
						if (ownResources[i].contains("-Selected"))
							g.drawImage(selected, startingX + 225, startingY + (space + 10) * pos, space, space, null);
						pos++;
					}
				}		
				// right resources
				startingX = 750;
				String[] rightResources = game.getRightSelected();
				for (int i = 0; i < rightResources.length; i++)
				{
					// if || card
					if (rightResources[i].contains("||"))
					{
						String[] split = rightResources[i].split("\\|\\|");
						for (int s = 0; s < split.length; s++)
						{
							// System.out.print(split[s] + " and ");
							g.drawImage(resources.get(split[s].split("-Selected")[0].split("\\|\\|")[0]), startingX + (space + 5) * s, startingY + (space + 10) * i, space, space, null);
							if (split[s].split("\\|\\|")[0].contains("-Selected"))
								g.drawImage(selected, startingX + (space + 5) * s, startingY + (space + 10) * i, space, space, null);
							if (s < split.length - 1)
								g.drawImage(OR, startingX + 35 + (space + 5) * s, startingY + 2 + (space + 10) * i, 16, 35, null);
						}
						System.out.println();
						// add a selected check mark
					}
					else
					{
						g.drawImage(resources.get(rightResources[i].split("-Selected")[0]), startingX, startingY + (space + 10) * i, space, space, null);
						if (rightResources[i].contains("-Selected"))
							g.drawImage(selected, startingX, startingY + (space + 10) * i, space, space, null);
					}
					if (brownR.contains(rightResources[i].split("\\|\\|")[0]))
						for (int j = 0; j < RightBrownCost; j++)
							g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (space + 10) * i + (coinSize) * j, coinSize, coinSize, null);
					if (silverR.contains(rightResources[i].split("\\|\\|")[0]))
						for (int j = 0; j < SilverCost; j++)
							g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (space + 10) * i + (coinSize) * j, coinSize, coinSize, null);
				}
				
				// labels for left, own, right resources
				g.setColor(Color.WHITE);
				g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 36));
				g.drawString("Your resources", 425, 445);
				g.drawString("Left resources", 30, 445);
				g.drawString("Right resources", 780, 445);
				g.setColor(Color.WHITE);
				g.drawLine(711, 451, 711, 1030);
				g.drawLine(342, 451, 342, 1030);
			}
			else if(game.getCurrentWonder().getAction().contentEquals("Burn"))
			{
				g.setColor(new Color(250, 60, 0));
				g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
				g.drawString("BURN", 500, 390);
				BufferedImage flame = ImageIO.read(new File("assets/images/Flames.png"));
				g.drawImage(flame, 340, 566, 456, 458, null);
				
			}
			
			//if(!game.isHalic())
			//{
				// cancel button
				g.setColor(new Color(191, 57, 57));
				g.fillRect(30, 990, 210, 40);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 32));
				g.drawString("CANCEL", 75, 1020);
			
				// confirm button
				g.setColor(new Color(95, 184, 119));
				g.fillRect(845, 990, 210, 40);
				g.setColor(Color.WHITE);
				g.drawString("CONFIRM", 880, 1020);
			//}
			
				
				
			
		}
		catch (IOException e)
		{
			System.out.println("Cannot find icons!");
		}
	}
	
	public void drawRoundInfo(Graphics g)
	{
		try
		{
			BufferedImage logo = ImageIO.read(new File("assets/images/logo.png"));
			BufferedImage rotation = null;
			if (game.getOrder() == 1)
				rotation = ImageIO.read(new File("assets/images/rotationcc.png"));
			else
				rotation = ImageIO.read(new File("assets/images/rotationac.png"));
			BufferedImage card = ImageIO.read(new File("assets/images/cards/age" + game.getAge() + ".png"));
			
			g.drawImage(logo, 1270, 320, logo.getWidth() * 3 / 7, logo.getHeight() * 3 / 7, null);
			g.drawImage(rotation, 1370, 525, rotation.getWidth()/12, rotation.getHeight()/12, null); //y was 515
			g.setFont(new Font("Times New Roman", Font.BOLD, 56));
			g.setColor(Color.BLACK);// was white
			g.drawString("ROUND " + game.getRound(), 1285, 510);//was 1340, 495 but was being covered
			g.drawImage(card, 1550, 450, card.getWidth() * 3 / 4, card.getHeight() * 3 / 4, null);
			// g.drawString("age " + game.getAge(), 1360, 565);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		/*
		try
		{
			wonder = ImageIO.read(new File("assets/images/wonders/" + currentWonder.getName() + ".png"));
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
		*/
	}
	public void drawOtherWonder(Graphics g)
	{
		
			// wonder of current player
			Wonder displayWonder = null;
			for(Wonder w: game.getWonders())
			{
				if(w.getName().contentEquals(displayView))
				{
					displayWonder = w;
				}
			}
			
			// assigns image to wonder
			BufferedImage wonder = null;
			try
			{
				wonder = ImageIO.read(new File("assets/images/wonders/" + displayWonder.getName() + ".png"));
			}
			catch (IOException e)
			{
				System.out.println("Cannot find " + displayWonder.getName());
			}
			/*
			 * public static final int WONDERXPOS = 1120, WONDERYPOS = 720;
				public static final int WONDERWIDTH = 756, WONDERHEIGHT = 313; // original image size is 605, 250
			 */
			int WONDERXPOS2 = 615; //since I want it to be centered
			int WONDERYPOS2 = 720;
			//int WONDERWIDTH2 = 1800;
			//int WONDERHEIGHT2 = 1800;
			
			
			// draws shadow
			g.setColor(TRANSPARENTBLACK);
			g.fillRect(WONDERXPOS2 + 10, WONDERYPOS2 + 10, WONDERWIDTH, WONDERHEIGHT);
			// draws wonder
			g.drawImage(wonder, WONDERXPOS2, WONDERYPOS2, WONDERWIDTH, WONDERHEIGHT, null);
			
			try
			{
				BufferedImage check = ImageIO.read(new File("assets/images/selected.png"));
				int checkw = 50;
				int checkh = 50;
				if(displayWonder.getPlayerWonders()>=1)
					g.drawImage(check, 809, 994, checkw, checkh, null);
				if(displayWonder.getPlayerWonders()>=2)
					g.drawImage(check, 1017, 994, checkw, checkh, null);
				if(displayWonder.getPlayerWonders()>=3)
					g.drawImage(check, 1258, 994, checkw, checkh, null);
			}
			catch (IOException e)
			{
				System.out.println("Cannot find check");
			}
			
			// draws the stats
			int i = 1;
			int starting = WONDERYPOS2 + 70;
			g.setColor(TRANSPARENTBLACK);
			g.fillRect(WONDERXPOS2, starting, 130, 120);
			
			g.setColor(Color.white);
			g.setFont(new Font("TimesRoman", Font.BOLD, 16));
			g.drawString(displayWonder.getMoney() + " Coins", WONDERXPOS2 + 5, starting + (i++ * 25));
			g.drawString(displayWonder.getMilitaryPower() + " Military Power", WONDERXPOS2 + 5, starting + (i++ * 25));
			g.drawString(displayWonder.getWins() + " Wins", WONDERXPOS2 + 5, starting + (i++ * 25));
			g.drawString(displayWonder.getLosses() + " Losses", WONDERXPOS2 + 5, starting + (i++ * 25));
		//Berlin Sans SB
		
		
		int count = 0;
		int buttonWidth = 275;
		
		int textY = 45;
		int textX = 30;
		
		g.setColor(new Color(128, 56, 28));
		if (displayColor.contentEquals("brown"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setFont(new Font("Berlin Sans SB", Font.BOLD, 13));
		g.setColor(Color.WHITE);
		g.drawString("Raw Materials(Brown): " + displayWonder.getCardsPlayed().get("brown").size(), count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(108, 111, 128));
		if (displayColor.contentEquals("silver"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setColor(Color.WHITE);
		g.drawString("Manufactured Materials(Silver): " + displayWonder.getCardsPlayed().get("silver").size(), count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(120, 0, 212));
		if (displayColor.contentEquals("purple"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setColor(Color.WHITE);
		g.drawString("Guilds(Purple): " + displayWonder.getCardsPlayed().get("purple").size(),  count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(33, 109, 222));
		if (displayColor.contentEquals("blue"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setColor(Color.WHITE);
		g.drawString("Civilian Structures(Blue): " + displayWonder.getCardsPlayed().get("blue").size(),  count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(31, 184, 54));
		if (displayColor.contentEquals("green"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setColor(Color.WHITE);
		g.drawString("Scientific Structures(Green): " + displayWonder.getCardsPlayed().get("green").size(),  count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(255, 186, 0));
		if (displayColor.contentEquals("yellow"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setColor(Color.WHITE);
		g.drawString("Commercial Structures(Yellow): " + displayWonder.getCardsPlayed().get("yellow").size(),  count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(191, 0, 3));
		if (displayColor.contentEquals("red"))
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80 + 10);
		else
			g.fillRect(count*buttonWidth, 0, buttonWidth, 80);
		g.setColor(Color.WHITE);
		g.drawString("Military Structures(Red): " + displayWonder.getCardsPlayed().get("red").size(),  count*buttonWidth+textX , textY);
		count++;
		
		g.setColor(new Color(191, 0, 3));
		g.fillRect(1720, 980, 200, 100);
		g.setColor(Color.white);
		g.drawString("Close Window", 1775, 1035);
		

		ArrayList<Card> tempCards = new ArrayList<Card>();
		ArrayList<String> cardNames = new ArrayList<String>();
		if(displayColor!="")
		{
			//System.out.print(displayColor);
			if(displayColor.equals("brown"))
			{
				System.out.println("brown"+displayWonder.getCardsPlayed().get("brown"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("brown"));
			}
			else if(displayColor.equals("silver"))
			{
				System.out.println(displayWonder.getCardsPlayed().get("silver"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("silver"));
			}
			else if(displayColor.equals("purple"))
			{
				System.out.println(displayWonder.getCardsPlayed().get("silver"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("purple"));
			}
			else if(displayColor.equals("blue"))
			{
				System.out.println(displayWonder.getCardsPlayed().get("blue"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("blue"));
			}
			else if(displayColor.equals("green"))
			{
				System.out.println(displayWonder.getCardsPlayed().get("green"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("green"));
			}
			else if(displayColor.equals("yellow"))
			{
				System.out.println(displayWonder.getCardsPlayed().get("yellow"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("yellow"));
			}
			else if(displayColor.equals("red"))
			{
				System.out.println(displayWonder.getCardsPlayed().get("red"));
				tempCards.addAll(displayWonder.getCardsPlayed().get("red"));
			}
			System.out.println("tempCards:"+tempCards);
			for(int x = 0; x<tempCards.size();x++)
			{
				//playerHandNames.add(currentHand.get(i).getName().replace(" ", "").toLowerCase());
				cardNames.add(tempCards.get(x).getName().replace(" ", "").toLowerCase());
				//System.out.println(cardNames);
			}
			BufferedImage cards[] = new BufferedImage[cardNames.size()];
			for (int c = 0; c < cards.length; c++)
			{
				try {
					cards[c] = ImageIO.read(new File("assets/images/cards/" + cardNames.get(c) + ".png"));
				} catch (IOException e) {
					System.out.println("Cannot find file " + cardNames.get(i));
				}
			}
			int HANDXPOS2 = 200;
			int HANDYPOS2 = 200;
			for (int v = 0; v < cards.length; v++)
			{
					// draws shadow
					g.setColor(TRANSPARENTBLACK);
					g.fillRect(5 + HANDXPOS2 + (CARDWIDTH + 10) * v, 5 + HANDYPOS2 + 20, CARDWIDTH, CARDHEIGHT);
					
					
					g.drawImage(cards[v], HANDXPOS2 + (CARDWIDTH + 10) * v, HANDYPOS2 + 20, null);
				
			}
		}
			

		
	}
	public void drawGraveyard(Graphics g)
	{
		// cards that have been discarded
		ArrayList<Card> graveyard = game.getGraveyard();
		
		// images of discarded cards
		BufferedImage cardImages[] = new BufferedImage[graveyard.size()];
		
		// selected card will be lowered
		int selected = -1;
		
		int startingX = 5, startingY = 5;
		
		// Card names with no spaces and to lower case in order to match image file names
		ArrayList<String> graveyardNames = new ArrayList<String>();
		
		
		Wonder current = null;
		for (int s = 0; s < game.getWonders().size(); s++)
			if (game.getWonders().get(s).getName().equals("The Mausoleum of Halicarnassus"))
				current = game.getWonders().get(s);
		
		for (int i = 0; i < graveyard.size(); i++)
		{
			if (graveyard.get(i).equals(current.getSelectedCard()))
				selected = i;
			graveyardNames.add(graveyard.get(i).getName().replace(" ", "").toLowerCase());
		}
		try
		{
			for (int i = 0; i < cardImages.length; i++)
				cardImages[i] = ImageIO.read(new File("assets/images/cards/" + graveyardNames.get(i) + ".png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// draws background paper image
		drawBackground(g);
		
		// draws confirm button
		g.setColor(new Color(95, 184, 119));
		g.fillRect(1710, 1010, 210, 70);
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(0, 1010, 420, 70);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Times New Roman", Font.BOLD, 24));
		g.drawString("CONFIRM", 1745, 1050);
		g.drawString("Halicarnassus: Play A Card For Free", 10, 1050);
		
		// draws card images
		for (int i = 0; i < graveyard.size(); i++)
		{
			if (i == selected)
			{
				g.setColor(TRANSPARENTBLACK);
				g.fillRect(5 + startingX + (CARDWIDTH + 10) * (i % 9), 15 + startingY + (CARDHEIGHT + 30) * (i / 9), CARDWIDTH, CARDHEIGHT);
				g.drawImage(cardImages[i], startingX + (CARDWIDTH + 10) * (i % 9), 10 + startingY + (CARDHEIGHT + 30) * (i / 9), CARDWIDTH, CARDHEIGHT, null);
			}
			else
			{	g.setColor(TRANSPARENTBLACK);
				g.fillRect(5 + startingX + (CARDWIDTH + 10) * (i % 9), 5 + startingY + (CARDHEIGHT + 30) * (i / 9), CARDWIDTH, CARDHEIGHT);
				g.drawImage(cardImages[i], startingX + (CARDWIDTH + 10) * (i % 9), startingY + (CARDHEIGHT + 30) * (i / 9), CARDWIDTH, CARDHEIGHT, null);
			}
			
		}
		
		Color altgreen = new Color(50, 170, 140);
		
		g.setFont(new Font("Berlin Sans SB", Font.BOLD, 14));
		g.setColor(altgreen);
		g.fillRect(790, 1010, 250, 70); // displayAllCurrent
		g.setColor(Color.white);
		g.drawString("Display All of Your Played Cards", 800 , 1050);
		
		
		
		g.setColor(altgreen);
		g.fillRect(490, 1010, 250, 70); // displayAllCurrent
		g.setColor(Color.white);
		g.drawString("Display Left Wonder", 540 , 1050);
		
		g.setColor(altgreen);
		g.fillRect(1090, 1010, 250, 70); // displayAllCurrent
		g.setColor(Color.white);
		g.drawString("Display Right Wonder", 1140 , 1050);
	}
	public void drawFinalPoints(Graphics g)
	{
		drawBackground(g);
		int FP = 0;
		int SP = 0;
		int TP = 0;
		String[] stand = new String[3];
		ArrayList<Integer> finalPoints = game.finalPoints();
		int ind = 0;
		
		for (int i = 0; i < finalPoints.size(); i++)
			if (finalPoints.get(i) > FP)
			{
				FP = finalPoints.get(i);
				 stand[0] = game.getWonders().get(i).getName();
				ind = i;
			}
		finalPoints.set(ind, -1);
		ind = 0;
		
		for (int i = 0; i < finalPoints.size(); i++)
			if (finalPoints.get(i) > SP)
			{
				SP = finalPoints.get(i);
				stand[1] = game.getWonders().get(i).getName();
				ind = i;
			}
		finalPoints.set(ind, -1);
		ind = 0;
		
		
		for (int i = 0; i < finalPoints.size(); i++)
			if (finalPoints.get(i) > TP)
			{
				TP = finalPoints.get(i);
				stand[2] = game.getWonders().get(i).getName();
				ind = i;
			}
		finalPoints.set(ind, -1);
		ind = 0;
		
		/*
		finalPoints.clear();
		//DELETE
		stand.add("The Statue of Zeus in Olympia");
		finalPoints.add(48);
		stand.add("The Hanging Gardens of Babylon");  
		finalPoints.add(36);
		stand.add("The Mausoleum of Halicarnassus");
		finalPoints.add(24);
		*/
		
		
		// assigns image to wonder
		BufferedImage podium[] = new BufferedImage[3];
		BufferedImage crown = null;
		BufferedImage F = null;
		BufferedImage S = null;
		BufferedImage T = null;
		
		try
		{
			crown= ImageIO.read(new File("assets/images/winner.png"));
			for(int i = 0; i <stand.length;i++ )
			{
				System.out.println("wonder"+stand[i]);
				if(i == 0)
				{
					System.out.println("points"+FP);
				}
				else if(i == 1)
				{
					System.out.println("points"+SP);
				}
				else if(i == 2)
				{
					System.out.println("points"+TP);
				}
				
				podium[i] = ImageIO.read(new File("assets/images/wonders/"+stand[i]+".png"));
			}
			F = ImageIO.read(new File("assets/images/gold.png"));
			S = ImageIO.read(new File("assets/images/silver.png"));
			T = ImageIO.read(new File("assets/images/bronze.png"));
			
		}
		catch (IOException e)
		{
			System.out.println("Cannot find finalPoints images");
		}
		/*
		 * public static final int WONDERXPOS = 1120, WONDERYPOS = 720;
			public static final int WONDERWIDTH = 756, WONDERHEIGHT = 313; // original image size is 605, 250
		 */
		
		
		int WONDERXPOS1 = 562;
		int WONDERYPOS1 = 100;
		
		// draws shadow
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(WONDERXPOS1 + 10, WONDERYPOS1 + 10, WONDERWIDTH, WONDERHEIGHT);
		// draws wonder
		g.drawImage(podium[0], WONDERXPOS1, WONDERYPOS1, WONDERWIDTH, WONDERHEIGHT, null);
		g.drawImage(crown, 876, 22,126,100,null);
		g.setFont(new Font("Times New Roman", Font.BOLD, 100));
		g.setColor(Color.BLACK);
		g.drawString(""+FP, 890, 500);
		g.drawImage(F, 1195, 220, 150 ,225, null);
		
		int WONDERXPOS2 = 50;
		int WONDERYPOS2 = 550;
		
		int WONDERXPOS3 = 1064;
		
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(WONDERXPOS2 + 10, WONDERYPOS2 + 10, WONDERWIDTH, WONDERHEIGHT);
		// draws wonder
		g.drawImage(podium[1], WONDERXPOS2, WONDERYPOS2, WONDERWIDTH, WONDERHEIGHT, null);
		g.setFont(new Font("Times New Roman", Font.BOLD, 100));
		g.setColor(Color.BLACK);
		g.drawString(""+SP, 370, 950);
		g.drawImage(S, 676, 688, 150 ,225, null);
		
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(WONDERXPOS3 + 10, WONDERYPOS2 + 10, WONDERWIDTH, WONDERHEIGHT);
		// draws wonder
		g.drawImage(podium[2], WONDERXPOS3, WONDERYPOS2, WONDERWIDTH, WONDERHEIGHT, null);
		g.setFont(new Font("Times New Roman", Font.BOLD, 100));
		g.setColor(Color.BLACK);
		g.drawString(""+TP, 1387, 950);
		g.drawImage(T, 1702, 686, 150 ,225, null);
		
		
		
		
		
	}
	
	public void nextTurn()
	{
		ArrayList<Boolean> decision = game.getDecisionMade();
		decision.set(game.getCurrentPlayer(), true);
		game.setDecisionMade(decision);
		
		game.nextPlayer();
		game.getSelectedResources().clear();
		optionSelection = true;
		
		for (int i = 0; i < game.getDecisionMade().size(); i++)
			System.out.println("Decision " + i + ": " +  game.getDecisionMade().get(i));
		
		System.out.println("all made: " + game.allDecisionsMade());
		
		/*if (game.allDecisionsMade())
		{
			game.finishRound();
			if(game.isHalic())
			{
				int p = 0;
				for(int i = 0; i<game.getWonders().size(); i++)
				{
					if(game.getWonders().get(i).getName().equals("The Mausoleum of Halicarnassus"))
					{
						p = i;
					}
				}
				game.setCurrentPlayer(p);
				game.getCurrentWonder().setAction("Play");
				game.finishRound(game.getWonders().get(p));
				optionSelection = false;
			}
			game.nextRound();
			game.initSelection();
		}*/
		if (game.allDecisionsMade())
		{
			game.finishRound();
			if(game.isHalic())
			{
				defaultView = false;
				displayHalic = true;
				displayGraveyard = true;
			}
			else
			{
				game.nextRound();
				game.initSelection();
			}
		}
		if(game.isEndOfGame())
		{
			defaultView= false;
			displayView = "";
			wonderDist = false;
		}
		for(Wonder w: game.getWonders())
		{
			if(w.getMoney()<0)
			{
				w.setMoney(0);
			}
			
		}
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
		else if (wonderDist)
		{
			if(e.getX()>=320&&e.getX()<=560&&e.getY()>=550&&e.getY()<=630) //IF QUIT
			{
				System.exit(0);
			}
			if(e.getX()>=1320&&e.getX()<=1765&&e.getY()>=555&&e.getY()<=625) //IF CONTINUE
			{
				wonderDist = false;
				defaultView = true;
				optionSelection = true;
			}
		}
		
		else if (defaultView)
		{
			//1375, 684, 238, 67
			if(e.getX()>=1375&&e.getX()<=1613&&e.getY()>=684&&e.getY()<=751) // if displayAllPlayedCards
			{
				defaultView = false;
				displayView = game.getCurrentWonder().getName();
			}
			//g.fillRect(1738, 40, 149, 125); // displayLeftWonder 
			//g.fillRect(1738, 190, 149, 125); // displayRightWonder(40+125+25(space) = 190)
			else if(e.getX()>=1738&&e.getX()<=1887)
			{
				if(e.getY()>=40&&e.getY()<=165)
				{
					defaultView = false;
					displayView = game.getLeftWonder(game.getCurrentPlayer()).getName();
				}
				if(e.getY()>=190&&e.getY()<=315)
				{
					defaultView = false;
					displayView = game.getRightWonder(game.getCurrentPlayer()).getName();
				}
			}
			
			// choose option of play, build, burn, or ability
			if (optionSelection)
			{
				// play
				if (e.getX() <= 590 && e.getX() >= 495 && e.getY() <= 665 && e.getY() >= 620)
				{
					optionSelection = false;
					game.getCurrentWonder().setAction("Play");
				}
				// build
				else if (e.getX() <= 704 && e.getX() >= 397 && e.getY() <= 750 && e.getY() >= 700)
				{
					optionSelection = false;
					game.getCurrentWonder().setAction("Build");
				}
				// burn
				else if (e.getX() <= 595 && e.getX() >= 490 && e.getY() <= 820 && e.getY() >= 780)
				{
					optionSelection = false;
					game.getCurrentWonder().setAction("Burn");
				}
				else if (game.canUseOlympia()&&e.getX() <= 625 && e.getX() >= 466 && e.getY() <= 908 && e.getY() >= 864)
				{
					optionSelection = false;
					game.getCurrentWonder().setAction("Play");
					OlympiaAbility = true;
					//game.setUsingO(true);
				}
			}
			else
			{
				if (e.getX() <= 240 && e.getX() >= 30 && e.getY() <= 1030 && e.getY() >= 990)//&&!game.isHalic() &&!OlympiaAbility
				{
					OlympiaAbility = false;
					optionSelection = true;
					//game.setUsingO(false);
					game.getSelectedResources().clear();
					game.initSelection();
					
					olympiaE = false;
					chainE = false;
					freeE = false;
					resE = false;
				}
				
				// confirm button
				else if (e.getX() <= 1055 && e.getX() >= 855 && e.getY() <= 1030 && e.getY() >= 990)//i fixed the hitbox the height was 210 lol + &&!game.isHalic()
				{
					if (game.getCurrentWonder().getAction().equals("Play") &&
							game.getCurrentWonder().getSelectedCard() != null && game.getCurrentWonder().playable(game.getCurrentWonder().getSelectedCard()))
					{
						ArrayList<String> needed = new ArrayList<String>(game.getCurrentWonder().getSelectedCard().getCost());
						ArrayList<String> selected = game.getSelectedResources();
						
						for (int index = 0; index < selected.size(); index++)
						{
							if (selected.get(index).contains("||"))
							{
								String[] orResources = selected.get(index).split("\\|\\|");
								System.out.println("or: " + Arrays.toString(orResources));
								for (int h = 0; h < orResources.length; h++)
								{
									if (orResources[h].contains("-Selected"))
									{
										String theResource = orResources[h].split("-Selected")[0];
										selected.set(index, theResource);
										break;
									}
								}
							}
						}
						
						System.out.println(needed);
						System.out.println(selected);
						Collections.sort(needed);
						Collections.sort(selected);
						
						if(needed.equals(selected) || needed.contains("null") || OlympiaAbility || game.getCurrentWonder().inChain(game.getCurrentWonder().getSelectedCard()))
						{
							if ((needed.contains("null") || game.getCurrentWonder().inChain(game.getCurrentWonder().getSelectedCard()) || OlympiaAbility) && !selected.isEmpty())
							{
								if (OlympiaAbility)
									olympiaE = true;
								else if (needed.contains("null"))
									freeE = true;
								else if (game.getCurrentWonder().inChain(game.getCurrentWonder().getSelectedCard()))
									chainE = true;
							}
							else
							{
								ArrayList<Card> currentHand = game.getPlayerHands().get(game.getCurrentWonder().getHand());
								for (int i = 0; i < currentHand.size(); i++)
								{
									// System.out.println("[ ]" + currentHand.get(i).getName());
									// System.out.println(">>>>" + game.getCurrentWonder().getSelectedCard().getName());
									
									if (currentHand.get(i).equals(game.getCurrentWonder().getSelectedCard()))
									{
										System.out.println("Removing " + currentHand.get(i).getName());
										currentHand.remove(i);
										if(OlympiaAbility)
										{
											game.setUsedOlympia(true);
											//game.setUsingO(true);
											game.getCurrentWonder().getSelectedCard().getCost().clear();
											game.getCurrentWonder().getSelectedCard().getCost().add("null");
										}
										OlympiaAbility = false;
										break;
									}
								}
								nextTurn();
								
								olympiaE = false;
								chainE = false;
								freeE = false;
								resE = false;
							}
						}
						else
						{
							resE = true;
						}
					}
					else if(game.getCurrentWonder().getAction().equals("Burn") && game.getCurrentWonder().getSelectedCard() != null)
					{
						ArrayList<Card> currentHand = game.getPlayerHands().get(game.getCurrentWonder().getHand());
						for (int i = 0; i < currentHand.size(); i++)
						{
							System.out.println("[ ]" + currentHand.get(i).getName());
							System.out.println(">>>>" + game.getCurrentWonder().getSelectedCard().getName());
							
							if (currentHand.get(i).equals(game.getCurrentWonder().getSelectedCard()))
							{
								System.out.println("Removing " + currentHand.get(i).getName());
								currentHand.remove(i);
								break;
							}
						}
						nextTurn();
						
					}
					else if(game.getCurrentWonder().getAction().equals("Build") && game.getCurrentWonder().getSelectedCard() != null&&game.getCurrentWonder().canBuildWonder())
					{
						Card stage = game.getCurrentWonder().nextWonder();
						System.out.println("Building "+stage.getName());
						
						ArrayList<String> needed = new ArrayList<String>(stage.getCost());
						ArrayList<String> selected = game.getSelectedResources();
						
						for (int index = 0; index < selected.size(); index++)
						{
							if (selected.get(index).contains("||"))
							{
								String[] orResources = selected.get(index).split("\\|\\|");
								System.out.println("or: " + Arrays.toString(orResources));
								for (int h = 0; h < orResources.length; h++)
								{
									if (orResources[h].contains("-Selected"))
									{
										String theResource = orResources[h].split("-Selected")[0];
										selected.set(index, theResource);
										break;
									}
								}
							}
						}
						
						Collections.sort(needed);
						Collections.sort(selected);
						if(needed.equals(selected) || needed.contains("null"))
						{
							if ((needed.contains("null") && !selected.isEmpty()))
							{
								freeE = true;
							}
							ArrayList<Card> currentHand = game.getPlayerHands().get(game.getCurrentWonder().getHand());
							for (int i = 0; i < currentHand.size(); i++)
							{
								if (currentHand.get(i).equals(game.getCurrentWonder().getSelectedCard()))
								{
									System.out.println("Removing " + currentHand.get(i).getName() + " to build wonder");
									currentHand.remove(i);
									break;
								}
							}
							nextTurn();
							freeE = false;
							resE = false;
						}
						else
						{
							resE = true;
						}
						
					}
					
					// Wonder cu = game.getCurrentWonder();
					// System.out.println(cu.canBuildWonder());
					// System.out.println(game.getCurrentWonder().getAction().equalsIgnoreCase("build"));
					// System.out.println(game.getCurrentWonder().getSelectedCard() != null);
				}

				// resources selected
				else if (e.getX() <= 1055 && e.getX() >= 30 && e.getY() <= 990 && e.getY() >= 450&&!OlympiaAbility&&(game.getCurrentWonder().getAction().equals("Play")||game.getCurrentWonder().getAction().equals("Build")))//&&!game.isHalic()
				{
					int SilverCost = 2, RightBrownCost = 2, LeftBrownCost = 2;
					String brownR = "wood-Selected stone-Selected clay-Selected ore-Selected";
					String silverR = "paper-Selected cloth-Selected glass-Selected";
					
					ArrayList<Card> crds = new ArrayList<Card>();
					crds.addAll(game.getCurrentWonder().getCardsPlayed().get("yellow"));
					for(int i = 0; i<crds.size(); i++ )
					{
						if(crds.get(i).getName().equals("East Trading Post"))
							RightBrownCost = 1;
						if(crds.get(i).getName().equals("West Trading Post"))
							LeftBrownCost = 1;
						if(crds.get(i).getName().equals("Marketplace"))
							SilverCost = 1;
					}
		            
					// for left resources
					int startingX = 75, startingY = 470, space = 40;
					String[] leftResources = game.getLeftSelected();
					for (int i = 0; i < leftResources.length; i++)
					{
						int cardCost = 0;
						if (brownR.contains(leftResources[i].split("||")[0]))
							cardCost = LeftBrownCost;
						else if (silverR.contains(leftResources[i].split("||")[0]))
							cardCost = SilverCost;
						if (!leftResources[i].contains("||"))
						{
							if (e.getX() <= startingX + space && e.getX() >= startingX &&
									e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
							{
								System.out.println(leftResources[i]); // debugging
								
								if (leftResources[i].contains("-Selected"))
								{
									leftResources[i] = leftResources[i].substring(0, leftResources[i].indexOf("-Selected"));
									game.getSelectedResources().remove(leftResources[i]);
									
									TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
									String leftName = game.getLeftWonder(game.getCurrentWonder()).getName();
									int t = trades.get(leftName);
									if(brownR.contains(leftResources[i]))
									{
										t-=LeftBrownCost;
										game.minusCost(LeftBrownCost);
									}
									if(silverR.contains(leftResources[i]))
									{
										t-=SilverCost;
										game.minusCost(LeftBrownCost);
									}
									trades.put(leftName,t);
								}
								else if (game.getCurrentWonder().getMoney() >= game.getTradingCost() + cardCost)
								{
									game.getSelectedResources().add(leftResources[i]);
									leftResources[i] = leftResources[i] + "-Selected";
									
									TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
									String leftName = game.getLeftWonder(game.getCurrentWonder()).getName();
									int t = trades.get(leftName);
									if(brownR.contains(leftResources[i]))
									{
										t+=LeftBrownCost;
										game.plusCost(LeftBrownCost);
									}
									if(silverR.contains(leftResources[i]))
									{
										t+=SilverCost;
										game.plusCost(SilverCost);
									}
									trades.put(leftName, t);
								}
							}
						}
						else
						{
							String[] orResources = leftResources[i].split("\\|\\|");
							String merged;
							for (int r = 0; r < orResources.length; r++)
							{
								if (e.getX() <= startingX + (space) * (r + 1) && e.getX() >= startingX + (space + 5) * r &&
										e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
								{
									if (orResources[r].contains("-Selected"))
									{
										game.getSelectedResources().remove(leftResources[i]);
										orResources[r] = orResources[r].split("-Selected")[0];
										
										merged = "";
										for (int h = 0; h < orResources.length; h++) // h for heck
											merged += orResources[h] + "||";
										merged = merged.substring(0, merged.lastIndexOf("||"));
										leftResources[i] = merged;
										
										TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
										String leftName = game.getLeftWonder(game.getCurrentWonder()).getName();
										int t = trades.get(leftName);
										if(brownR.contains(leftResources[i].split("\\|\\|")[0]))
										{
											t-=LeftBrownCost;
											game.minusCost(LeftBrownCost);
										}
										if(silverR.contains(leftResources[i].split("\\|\\|")[0]))
										{
											t-=SilverCost;
											game.minusCost(SilverCost);
										}
										trades.put(leftName,t);
										
										System.out.println("All selected: " + game.getSelectedResources());
									}
									else if (game.getCurrentWonder().getMoney() >= game.getTradingCost() + cardCost)
									{
										boolean canSelect = true;
										for (int index = 0; index < orResources.length; index++)
											if (orResources[index].contains("-Selected"))
												canSelect = false;
										if (canSelect)
										{
											orResources[r] = orResources[r] + "-Selected";
											
											merged = "";
											for (int h = 0; h < orResources.length; h++) // h for heck
												merged += orResources[h] + "||";
											merged = merged.substring(0, merged.lastIndexOf("||"));
											leftResources[i] = merged;
											
											game.getSelectedResources().add(leftResources[i]);
											
											TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
											String leftName = game.getLeftWonder(game.getCurrentWonder()).getName();
											int t = trades.get(leftName);
											if(brownR.contains(leftResources[i].split("\\|\\|")[0]))
											{
												t+=LeftBrownCost;
												game.plusCost(LeftBrownCost);
											}
											if(silverR.contains(leftResources[i].split("\\|\\|")[0]))
											{
												t+=SilverCost;
												//game.minusCost(SilverCost);
												game.plusCost(SilverCost);
											}
											trades.put(leftName, t);
											
											System.out.println("All selected: " + game.getSelectedResources());
										}
									}
								}
							}
						}
					}
					
					// right resources
					startingX = 750;
					String[] rightResources = game.getRightSelected();
					for (int i = 0; i < rightResources.length; i++)
					{
						int cardCost = 0;
						if (brownR.contains(rightResources[i].split("||")[0]))
							cardCost = LeftBrownCost;
						else if (silverR.contains(rightResources[i].split("||")[0]))
							cardCost = SilverCost;
						if (!rightResources[i].contains("||"))
						{
							if (e.getX() <= startingX + space && e.getX() >= startingX &&
									e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
							{
								System.out.println(rightResources[i]); // debugging
								
								if (rightResources[i].contains("-Selected"))
								{
									rightResources[i] = rightResources[i].substring(0, rightResources[i].indexOf("-Selected"));
									game.getSelectedResources().remove(rightResources[i]);
									
									TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
									String rightName = game.getRightWonder(game.getCurrentWonder()).getName();
									int t = trades.get(rightName);
									if(brownR.contains(rightResources[i]))
									{
										t-=RightBrownCost;
										game.minusCost(RightBrownCost);
									}
									if(silverR.contains(rightResources[i]))
									{
										t-=SilverCost;
										game.minusCost(SilverCost);
									}
									trades.put(rightName,t);
								}
								else if (game.getCurrentWonder().getMoney() >= game.getTradingCost() + cardCost)
								{
									game.getSelectedResources().add(rightResources[i]);
									rightResources[i] = rightResources[i] + "-Selected";
									
									TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
									String rightName = game.getRightWonder(game.getCurrentWonder()).getName();
									int t = trades.get(rightName);
									if(brownR.contains(rightResources[i]))
									{
										t+=RightBrownCost;
										game.plusCost(RightBrownCost);
									}
									if(silverR.contains(rightResources[i]))
									{
										t+=SilverCost;
										game.plusCost(SilverCost);
									}
									trades.put(rightName, t);
								}
							}
						}
						else
						{
							String[] orResources = rightResources[i].split("\\|\\|");
							String merged;
							for (int r = 0; r < orResources.length; r++)
							{
								if (e.getX() <= startingX + (space) * (r + 1) && e.getX() >= startingX + (space + 5) * r &&
										e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
								{
									if (orResources[r].contains("-Selected"))
									{
										game.getSelectedResources().remove(rightResources[i]);
										orResources[r] = orResources[r].split("-Selected")[0];
										
										merged = "";
										for (int h = 0; h < orResources.length; h++) // h for heck
											merged += orResources[h] + "||";
										merged = merged.substring(0, merged.lastIndexOf("||"));
										rightResources[i] = merged;
										
										TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
										String rightName = game.getRightWonder(game.getCurrentWonder()).getName();
										int t = trades.get(rightName);
										if(brownR.contains(rightResources[i].split("\\|\\|")[0]))
										{
											t-=RightBrownCost;
											game.minusCost(RightBrownCost);
										}
										if(silverR.contains(rightResources[i].split("\\|\\|")[0]))
										{
											t-=SilverCost;
											game.minusCost(SilverCost);
										}
										trades.put(rightName,t);
										
										System.out.println("All selected: " + game.getSelectedResources());
									}
									else if (game.getCurrentWonder().getMoney() >= game.getTradingCost() + cardCost)
									{
										boolean canSelect = true;
										for (int index = 0; index < orResources.length; index++)
											if (orResources[index].contains("-Selected"))
												canSelect = false;
										if (canSelect)
										{
											orResources[r] = orResources[r] + "-Selected";
											
											merged = "";
											for (int h = 0; h < orResources.length; h++) // h for heck
												merged += orResources[h] + "||";
											merged = merged.substring(0, merged.lastIndexOf("||"));
											rightResources[i] = merged;
											
											game.getSelectedResources().add(rightResources[i]);
											
											TreeMap<String, Integer> trades = game.getCurrentWonder().getTrades();
											String rightName = game.getRightWonder(game.getCurrentWonder()).getName();
											int t = trades.get(rightName);
											if(brownR.contains(rightResources[i].split("\\|\\|")[0]))
											{
												t+=RightBrownCost;
												game.plusCost(RightBrownCost);
											}
											if(silverR.contains(rightResources[i].split("\\|\\|")[0]))
											{
												t+=SilverCost;
												game.plusCost(RightBrownCost);
											}
											trades.put(rightName, t);
											
											System.out.println("All selected: " + game.getSelectedResources());
										}
									}
								}
							}
						}
					}
					startingX = 415;
					String[] ownResources = game.getOwnSelected();
					for (int i = 0; i < ownResources.length; i++)
					{
						if (!ownResources[i].contains("coin"))
						{
							if (!ownResources[i].contains("||"))
							{
								if (e.getX() <= startingX + space && e.getX() >= startingX &&
										e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
								{
									System.out.println(ownResources[i]); // debugging
									
									if (ownResources[i].contains("-Selected"))
									{
										ownResources[i] = ownResources[i].substring(0, ownResources[i].indexOf("-Selected"));
										game.getSelectedResources().remove(ownResources[i]);
									}
									else
									{
										game.getSelectedResources().add(ownResources[i]);
										ownResources[i] =ownResources[i] + "-Selected";
									}
								}
							}
							else
							{
								String[] orResources = ownResources[i].split("\\|\\|");
								String merged;
								for (int r = 0; r < orResources.length; r++)
								{
									if (e.getX() <= startingX + (space) * (r + 1) && e.getX() >= startingX + (space + 5) * r &&
											e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
									{
										if (orResources[r].contains("-Selected"))
										{
											game.getSelectedResources().remove(ownResources[i]);
											orResources[r] = orResources[r].split("-Selected")[0];
											
											merged = "";
											for (int h = 0; h < orResources.length; h++) // h for heck
												merged += orResources[h] + "||";
											merged = merged.substring(0, merged.lastIndexOf("||"));
											ownResources[i] = merged;
											
											System.out.println("All selected: " + game.getSelectedResources());
										}
										else
										{
											boolean canSelect = true;
											for (int index = 0; index < orResources.length; index++)
												if (orResources[index].contains("-Selected"))
													canSelect = false;
											if (canSelect)
											{
												orResources[r] = orResources[r] + "-Selected";
												
												merged = "";
												for (int h = 0; h < orResources.length; h++) // h for heck
													merged += orResources[h] + "||";
												merged = merged.substring(0, merged.lastIndexOf("||"));
												ownResources[i] = merged;
												
												game.getSelectedResources().add(ownResources[i]);
												
												System.out.println("All selected: " + game.getSelectedResources());
											}
										}
									}
								}
							}
						}
					}
					int pos = 0;
					for (int i = 0; i < ownResources.length; i++)
					{
						if (ownResources[i].contains("coin"))
						{
							if (e.getX() <= startingX + space + 225 && e.getX() >= startingX + 225 &&
									e.getY() <= startingY + (space + 10) * pos + space && e.getY() >= startingY + (space + 10) * pos)
							{
								System.out.println(ownResources[i]); // debugging
								
								if (ownResources[i].contains("-Selected"))
								{
									ownResources[i] = ownResources[i].substring(0, ownResources[i].indexOf("-Selected"));
									game.getSelectedResources().remove(ownResources[i]);
									game.minusCost(1);
								}
								else
								{
									game.getSelectedResources().add(ownResources[i]);
									ownResources[i] = ownResources[i] + "-Selected";
									game.plusCost(1);
									
								}
							}
							pos++;
						}
					}
				}
		}
			// if selected a card in hand
			if (e.getX() >= 5 && e.getX() <= 1325 && e.getY() >= 5 && e.getY() <= 280)
			{
				// goes through each card in the hand to check for the click
				Wonder current = game.getCurrentWonder();
				ArrayList<Card> currentHand = game.getPlayerHands().get(current.getHand());
				for (int i = 0; i < currentHand.size(); i++)
				{
					// this is the bounds for each card of index 'i'
					// funcion is g(i) = 190i + 5, or g(i) = 5 (initial offset of card) + 10i (space between each card) + 180i (width of each card)
					if (e.getX() >= 190 * i + 5 && e.getX() <= 190 * i + 185 && e.getY() >= 5 && e.getY() <= 280)
					{
						// possible temporary code for debugging
						if (!currentHand.get(i).equals(current.getSelectedCard()))
						{
							System.out.println("Chosen card " + currentHand.get(i).getName());
							current.setSelectedCard(currentHand.get(i));
						}
						else
						{
							System.out.println("Deselected card " + currentHand.get(i).getName());
							current.setSelectedCard(null);
						}
					}
					
				}
			}
		}
		
		else if(displayView!="")
		{
			String[] temp = {"brown", "silver", "purple", "blue", "green", "yellow", "red"};
			int buttonWidth = 275;
			
			for(int i = 0; i<temp.length; i++)
			{
				if(e.getX()>=buttonWidth*i&&e.getX()<=(buttonWidth*i)+buttonWidth&&e.getY()>=0&&e.getY()<=80)
				{
					displayColor = temp[i];
				}
			}
			//g.fillRect(1720, 980, 200, 100);
			//closeWindow should set displayColor to "" and defaultView to true
			if(e.getX()>=1720&&e.getY()<=1920&&e.getY()>=980&&e.getY()<=1080)
			{
				displayView = "";
				displayColor = "";
				if (displayGraveyard)
					displayHalic = true;
				else
					defaultView = true;
			}
			
			
		}
		else if(displayHalic) // need confirm and cancel as well as use playable(Card c) method
		{
			Wonder current = null;
			for (int s = 0; s < game.getWonders().size(); s++)
				if (game.getWonders().get(s).getName().equals("The Mausoleum of Halicarnassus"))
					current = game.getWonders().get(s);
			
			if (displayView.equals(""))
			{
				// own wonder
				if(e.getX()>=790&&e.getX()<=1040&&e.getY()>=1010&&e.getY()<=1080)
				{
					displayHalic = false;
					displayView = current.getName();
				}
				// left wonder
				else if(e.getX() >= 490 && e.getX() <= 740 && e.getY() >= 1010 && e.getY() <= 1080)
				{
					displayHalic = false;
					displayView = game.getLeftWonder(current).getName();
				}
				// right wonder
				else if(e.getX() >= 1090 && e.getX() <= 1340 && e.getY() >= 1010 && e.getY() <= 1080)
				{
					displayHalic = false;
					displayView = game.getRightWonder(current).getName();
				}
				
				ArrayList<Card> graveyard = game.getGraveyard();
				
				int startingX = 5, startingY = 5;
				
				// goes through each card in the hand to check for the click
				for (int i = 0; i < graveyard.size(); i++)
				{
					if (e.getX() >= startingX + (CARDWIDTH + 10) * (i % 9) && e.getX() <= startingX + (CARDWIDTH + 10) * (i % 9) + CARDWIDTH
							&& e.getY() >= startingY + (CARDHEIGHT + 30) * (i / 9) && e.getY() <= startingY + (CARDHEIGHT + 30) * (i / 9) + CARDHEIGHT)
					{
						if (!graveyard.get(i).equals(current.getSelectedCard()))
						{
							current.setSelectedCard(graveyard.get(i));
							System.out.println("Chosen card " + current.getSelectedCard());
						}
						else
						{
							System.out.println("Deselected card " + graveyard.get(i).getName());
							current.setSelectedCard(null);
						}
						
					}
				}
				// confirm button
				if (e.getX() <= 1920 && e.getX() >= 1710 && e.getY() <= 1080 && e.getY() >= 1010 && current.getSelectedCard() != null && current.playable(current.getSelectedCard()))
				{
					int otherPlayer = game.getCurrentPlayer();
					int p = 0;
					for(int i = 0; i<game.getWonders().size(); i++)
					{
						if(game.getWonders().get(i).getName().equals("The Mausoleum of Halicarnassus"))
						{
							p = i;
						}
					}
					game.setCurrentPlayer(p);
					game.getCurrentWonder().setAction("Play");
					game.finishRound(game.getCurrentWonder());
					optionSelection = false;
					
					game.setCurrentPlayer(otherPlayer);
					
					displayHalic = false;
					displayGraveyard = false;
					defaultView = true;
					optionSelection = true;
					
					game.nextRound();
					game.initSelection();
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