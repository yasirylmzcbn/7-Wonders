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
			RED = new Color(255, 208, 208),
			BROWN = new Color(207, 178, 172);
	
	private boolean mainMenu; // shows the start button
	private boolean wonderDist; // Shows distribution of wonders graphically (What wonders are in the current game)
	private boolean optionSelection; // allows player to choose to play, build, burn, or use wonder ability
	private boolean defaultView; // Shows hand, wonder, and resources
	private String displayView;
	//private boolean displayLeftPlayed;
	//private boolean displayRightPlayed;
	private boolean displayHalic; //might become a String later on
	private boolean displayGraveyard; // for Halicarnassus
	
	private int[] posX;
	private int width;
	private int height;
	
	private GameState game;
	
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
			drawRoundInfo(g);
			if (optionSelection)
				drawOptionSelection(g);
			else
				drawResourceSelection(g);
		}
		if (displayView!="")
		{
			drawBackground(g);
		}/*
		if (displayLeftPlayed)
		{
			drawBackground(g);
		}
		if (displayRightPlayed)
		{
			drawBackground(g);
		}*/
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
		g.setColor(TRANSPARENTBLACK);
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
		
		g.setColor(GREEN);
		g.fillRect(1425, 684, 156, 50);
		
	}
	
	public void drawOptionSelection(Graphics g)
	{
		// Container that holds text
		
		// shadow of container
		g.setColor(TRANSPARENTBLACK);
		g.fillRect(40, 460, 1025, 580);
		
		// sets colour of container based on the currently selected card's colour
		Card col = game.getWonders().get(game.getCurrentPlayer()).getSelectedCard();
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
			else
				g.setColor(new Color(255, 245, 222));
		}
		else
			g.setColor(new Color(255, 245, 222));
		g.fillRect(30, 450, 1025, 580);
		
		
		g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
		g.setColor(Color.BLACK);
		g.drawString("CHOOSE YOUR ACTION", 280, 590);
		g.drawString("Play", 495, 660);
		g.drawString("Build", 485, 740);
		g.drawString("Burn", 495, 820);
		if (game.isHalic() || game.canUseOlympia())
			g.drawString("Ability", 475, 900);
	}
	
	/* TODO
	 * needs cancel and confirm button
	 * need to be able to select resource
	 */
	public void drawResourceSelection(Graphics g)
	{
		try
		{
			// Container that holds the resource images

			// shadow of container
			g.setColor(TRANSPARENTBLACK);
			g.fillRect(40, 460, 1025, 580);
			
			// sets colour of container based on the currently selected card's colour
			Card col = game.getWonders().get(game.getCurrentPlayer()).getSelectedCard();
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
				else
					g.setColor(new Color(255, 245, 222));
			}
			else
				g.setColor(new Color(255, 245, 222));
			g.fillRect(30, 450, 1025, 580);
			

			g.setColor(Color.WHITE);
			g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 48));
			g.drawString("Resource Selection", 350, 370);
			// resource name (including coin) and their images
			HashMap<String, BufferedImage> resources = new HashMap<String, BufferedImage>();
			File imageFiles[] = new File("src/images/resources/").listFiles();
			BufferedImage selected = ImageIO.read(new File("src/images/selected.png"));
			
			// imports resource images
			for (int i = 0; i < imageFiles.length; i++)
				resources.put(imageFiles[i].getName().substring(0, imageFiles[i].getName().indexOf(".png")), ImageIO.read(imageFiles[i]));
			
			// drawing the coins
			int SilverCost = 2; // cost of trading for a silver card resource
			int RightBrownCost = 2;// cost for trading for a brown card resource to the right
			int LeftBrownCost = 2;//^*left
			
			// TODO-needed?
			String brownR = "wood-Selected stone-Selected clay-Selected ore-Selected";
            String silverR = "paper-Selected cloth-Selected glass-Selected";
			
			ArrayList<Card> crds = new ArrayList<Card>();
			crds.addAll(game.getWonders().get(game.getCurrentPlayer()).getCardsPlayed().get("yellow"));
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
			ArrayList<String> leftResources = game.getLeftWonder(game.getCurrentPlayer()).getCardResources();
			for (int i = 0; i < leftResources.size(); i++)
			{
				g.drawImage(resources.get(leftResources.get(i)), startingX, startingY + (space + 10) * i, space, space, null);
				//if (leftResources.get(i).contains("-Selected"))
					//g.drawImage(selected, startingX, startingY + (space + 10) * i, space, space, null);
				if (brownR.contains(leftResources.get(i)))
					for (int j = 0; j < LeftBrownCost; j++)
						g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (coinSize) * j, coinSize, coinSize, null);
				if (silverR.contains(leftResources.get(i)))
					for (int j = 0; j < SilverCost; j++)
						g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (coinSize) * j, coinSize, coinSize, null);
					
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
			ArrayList<String> ownResources = game.getWonders().get(game.getCurrentPlayer()).getAllPlayerResources();
			for (int i = 0; i < ownResources.size(); i++)
			{
				if (!ownResources.get(i).equals("coin"))
				{
					g.drawImage(resources.get(ownResources.get(i)), startingX, startingY + (space + 10) * i, space, space, null);
					//if (ownResources.get(i).contains("-Selected"))
						//g.drawImage(selected, startingX, startingY + (space + 10) * i, space, space, null);
				}
			}
			// draws coins
			int pos = 0;
			for (int i = 0; i < ownResources.size(); i++)
			{
				if (ownResources.get(i).equals("coin"))
				{
					g.drawImage(resources.get(ownResources.get(i)), startingX + 225, startingY + (space + 2) * pos++, space, space, null);
					//if (ownResources.get(i).contains("-Selected"))
						//g.drawImage(selected, startingX + 225, startingY + (space + 2) * pos++, space, space, null);
				}
			}
				
			// right resources
			startingX = 750;
			ArrayList<String> rightResources = game.getRightWonder(game.getCurrentPlayer()).getCardResources();
			for (int i = 0; i < rightResources.size(); i++)
			{
				g.drawImage(resources.get(rightResources.get(i)), startingX, startingY + (space + 10) * i, space, space, null);
				//if (rightResources.get(i).contains("-Selected"))
					//g.drawImage(selected, startingX, startingY + (space + 10) * i, space, space, null);
				if (brownR.contains(rightResources.get(i)))
					for (int j = 0; j < LeftBrownCost; j++)
						g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (coinSize) * j, coinSize, coinSize, null);
				if (silverR.contains(rightResources.get(i)))
					for (int j = 0; j < SilverCost; j++)
						g.drawImage(resources.get("coin"), startingX - coinSize, startingY + (coinSize) * j, coinSize, coinSize, null);
					
			}
			
			// labels for left, own, right resources
			g.setColor(Color.WHITE);
			g.setFont(new Font("Berlin Sans FB", Font.PLAIN, 36));
			g.drawString("Your resources", 425, 445);
			g.drawString("Left resources", 30, 445);
			g.drawString("Right resources", 835, 445);
			
			
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
			
		}
		catch (IOException e)
		{
			System.out.println("Cannot find icons!");
		}
	}
	
	public void drawRoundInfo(Graphics g)
	{
		// 7wonders logo, card rotation, round num, age card
		BufferedImage logo = null, rotation = null, age = null;
		
		try
		{
			logo = ImageIO.read(new File("src/imges/logo.png"));
		}
		catch (IOException e)
		{
			
		}
		/*
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
		*/
	}
	public void drawAWonder(Graphics g)
	{
		//Berlin Sans SB
		
		if(displayView.contentEquals(game.getWonders().get(game.getCurrentPlayer()).getName()))
		{
		Wonder CurrentPlayer = game.getWonders().get(game.getCurrentPlayer());
		
		ArrayList<Card> yellow = new ArrayList<Card>();
		yellow.addAll(CurrentPlayer.getCardsPlayed().get("yellow"));
		
		ArrayList<Card> brown = new ArrayList<Card>();
		brown.addAll(CurrentPlayer.getCardsPlayed().get("brown"));
		
		ArrayList<Card> blue = new ArrayList<Card>();
		blue.addAll(CurrentPlayer.getCardsPlayed().get("blue"));
		
		ArrayList<Card> green = new ArrayList<Card>();
		green.addAll(CurrentPlayer.getCardsPlayed().get("green"));
		
		ArrayList<Card> purple = new ArrayList<Card>();
		purple.addAll(CurrentPlayer.getCardsPlayed().get("purple"));
		
		ArrayList<Card> silver = new ArrayList<Card>();
		silver.addAll(CurrentPlayer.getCardsPlayed().get("silver"));
		
		ArrayList<Card> red = new ArrayList<Card>();
		red.addAll(CurrentPlayer.getCardsPlayed().get("red"));
		
		int startX = 30;
		int startY = 50;
		int vertSpace = 5;
		int horizSpace= 5;
		
		g.setFont(new Font("Berlin Sans SB", Font.BOLD, 32));
		
		g.drawString("brown", startX, startY );
		}
		
	}
	
	public void nextTurn()
	{
		ArrayList<Boolean> decision = game.getDecisionMade();
		decision.set(game.getCurrentPlayer(), true);
		game.setDecisionMade(decision);
		
		game.nextPlayer();
		optionSelection = true;
		
		if (game.allDecisionsMade())
		{
			game.finishRound();
			game.nextRound();
		}
		
		/*
		 * while (!state.isEndOfGame())
		{
			printNewRound();
			while (!state.allDecisionsMade())
			{
				/* debugging
				for (int i = 0; i < state.getPlayerHands().size(); i++)
					System.out.println(i + " " + state.getPlayerHands().get(i)); // debugging
				System.out.println();
				
				currentWonder = state.getWonders().get(state.getCurrentPlayer());
				currentPlayer = state.getCurrentPlayer();
				currentHand = state.getPlayerHands().get(currentWonder.getHand());
	
				printNextPlayer();
				
				printWonderInformation();	
				printOneLine();	
					
				printPlayedCards();	
				printOneLine();	
					
				printPlayerHand();	
				printOneLine();	
					
				optionSelection();	
				printOneLine();	
				
				state.nextPlayer();
			}
			
			state.finishRound();
			if(state.isHalic())
			{
				currentWonder = state.getWonders().get(state.getCurrentPlayer());
				currentPlayer = state.getCurrentPlayer();
				currentHand = state.getPlayerHands().get(currentWonder.getHand());
				optionSelection();	
				printOneLine();	
				state.finishRound();
			}
			state.nextRound();
		}
		System.out.println(state.whoWon());
		 */
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
			//1425, 684, 156, 50 rect of displayPlayer
			if(e.getX()>=1425&&e.getX()<=1581&&e.getY()>=684&&e.getY()<=734)
			{
				defaultView = false;
				displayView = game.getWonders().get(game.getCurrentPlayer()).getName();
				
			}
			// choose option of play, build, burn, or ability
			if (optionSelection)
			{
				// play
				if (e.getX() <= 590 && e.getX() >= 495 && e.getY() <= 665 && e.getY() >= 620)
				{
					optionSelection = false;
					game.getWonders().get(game.getCurrentPlayer()).setAction("play");
				}
				// build
				else if (e.getX() <= 590 && e.getX() >= 485 && e.getY() <= 750 && e.getY() >= 700)
				{
					optionSelection = false;
					game.getWonders().get(game.getCurrentPlayer()).setAction("build");
				}
				// burn
				else if (e.getX() <= 595 && e.getX() >= 490 && e.getY() <= 820 && e.getY() >= 780)
				{
					optionSelection = false;
					game.getWonders().get(game.getCurrentPlayer()).setAction("burn");
				}
				// TODO action
				/*else if (e.getX() <= 610 && e.getX() >= 465 && e.getY() <= 910 && e.getY() >= 860)
				{
					optionSelection = false;
					game.getWonders().get(game.getCurrentPlayer()).setAction("play");
				}*/
			}
			// if resource selection
			/* TODO
			 * || cards
			 * taking away coins when trading? maybe not idk
			 */
			else
			{
				// cancel button // TODO what if they have selected a few resources? how would it reset that?
				if (e.getX() <= 240 && e.getX() >= 30 && e.getY() <= 1030 && e.getY() >= 990)
				{
					optionSelection = true;
				}
				// confirm button
				else if (e.getX() <= 1055 && e.getX() >= 855 && e.getY() <= 1030 && e.getY() >= 210)
				{
					if (game.getWonders().get(game.getCurrentPlayer()).getAction().equals("play") &&
							game.getWonders().get(game.getCurrentPlayer()).getSelectedCard() != null)
					{
						ArrayList<String> needed = new ArrayList<String>(game.getWonders().get(game.getCurrentPlayer()).getSelectedCard().getCost());
						ArrayList<String> selected = game.getSelectedResources();
						
						System.out.println(needed);
						System.out.println(selected);
						Collections.sort(needed);
						Collections.sort(selected);
						
						if(needed.equals(selected) || needed.contains("null"))
						{
							ArrayList<Card> currentHand = game.getPlayerHands().get(game.getCurrentPlayer());
							for (int i = 0; i < currentHand.size(); i++)
								if (currentHand.get(i).getName().equals(game.getWonders().get(game.getCurrentPlayer()).getSelectedCard().getName()))
									currentHand.remove(i);
							nextTurn();
						}
						
						
					}
				}
				// resourses selected
				else if (e.getX() <= 1055 && e.getX() >= 30 && e.getY() <= 990 && e.getY() >= 450)
				{
					// for left resources
					int startingX = 75, startingY = 470, space = 40, coinSize = 20;
					ArrayList<String> leftResources = game.getLeftWonder(game.getCurrentPlayer()).getCardResources();
					for (int i = 0; i < leftResources.size(); i++)
					{
						if (e.getX() <= startingX + space && e.getX() >= startingX &&
								e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
						{
							System.out.println(leftResources.get(i)); // debugging
							
							if (leftResources.get(i).contains("-Selected"))
							{
								leftResources.set(i, leftResources.get(i).substring(0, leftResources.indexOf("-Selected")));
								game.getSelectedResources().remove(leftResources.get(i));
							}
							else
							{
								game.getSelectedResources().add(leftResources.get(i));
								leftResources.set(i, leftResources.get(i) + "-Selected");
							}
						}
					}
					
					// right resources
					startingX = 750;
					ArrayList<String> rightResources = game.getRightWonder(game.getCurrentPlayer()).getCardResources();
					for (int i = 0; i < rightResources.size(); i++)
					{
						if (e.getX() <= startingX + space && e.getX() >= startingX &&
								e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
						{
							System.out.println(rightResources.get(i)); // debugging
							
							if (rightResources.get(i).contains("-Selected"))
							{
								rightResources.set(i, rightResources.get(i).substring(0, rightResources.indexOf("-Selected")));
								game.getSelectedResources().remove(leftResources.get(i));
							}
							else
							{
								game.getSelectedResources().add(rightResources.get(i));
								rightResources.set(i, rightResources.get(i) + "-Selected");
							}
						}
					}
					startingX = 415;
					ArrayList<String> ownResources = game.getWonders().get(game.getCurrentPlayer()).getAllPlayerResources();
					for (int i = 0; i < ownResources.size(); i++)
					{
						if (!ownResources.get(i).equals("coin"))
						{
							if (e.getX() <= startingX + space && e.getX() >= startingX &&
									e.getY() <= startingY + (space + 10) * i + space && e.getY() >= startingY + (space + 10) * i)
							{
								System.out.println(ownResources.get(i)); // debugging
								
								if (ownResources.get(i).contains("-Selected"))
								{
									ownResources.set(i, ownResources.get(i).substring(0, ownResources.indexOf("-Selected")));
									game.getSelectedResources().remove(leftResources.get(i));
								}
								else
								{
									game.getSelectedResources().add(ownResources.get(i));
									ownResources.set(i, ownResources.get(i) + "-Selected");
								}
							}
						}
					}
					// the coins
					// TODO
					// can't deselect coins lol
					// TODO
					// TODO
					// TODO
					// TODO
					int pos = 0;
					for (int i = 0; i < ownResources.size(); i++)
					{
						if (ownResources.get(i).equals("coin"))
						{
							if (e.getX() <= startingX + space + 225 && e.getX() >= startingX + 225 &&
									e.getY() <= startingY + (space + 10) * pos + space && e.getY() >= startingY + (space + 10) * pos)
							{
								System.out.println(ownResources.get(i)); // debugging
								
								if (ownResources.get(i).contains("-Selected"))
								{
									ownResources.set(i, ownResources.get(i).substring(0, ownResources.indexOf("-Selected")));
									game.getSelectedResources().remove(leftResources.get(i));
								}
								else
								{
									game.getSelectedResources().add(ownResources.get(i));
									ownResources.set(i, ownResources.get(i) + "-Selected");
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
				Wonder current = game.getWonders().get(game.getCurrentPlayer());
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
		
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}