import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class WonderPanel extends JPanel implements MouseListener
{
	private GameState game;
	private boolean showMainMenu;
	
	public WonderPanel(Dimension size)
	{
		super();
		this.setSize(size);
		this.setLayout(null);
		showMainMenu = true;
		game = new GameState();
		addMouseListener(this);
	}
	
	public void paint(Graphics g)
	{
		System.out.println("REPAINTED"); // for debugging
		
		if (showMainMenu)
			drawMainMenu(g);
	}
	
	public void drawHand()
	{
		
	}
	
	public void drawCardsPlayed()
	{
		
	}
	
	/*
	 * Draws the main menu, where players will either have the choice to start the game, or to exit
	 */
	public void drawMainMenu(Graphics g)
	{
		BufferedImage mainMenuBackground = null;
		BufferedImage mainMenuLogo = null;
		try {
		mainMenuBackground = ImageIO.read(new File("src/images/mainmenubackground.png"));
		} catch (Exception e) {
			System.out.println("Could not load image: " + e);
		}
		
		// draws the background image
		g.drawImage(mainMenuBackground, 0, 0, 1920, 1080, null);
	}

	public void mouseClicked(MouseEvent arg0) {}

	public void mouseEntered(MouseEvent arg0) {}

	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0)
	{
		int x = arg0.getX();
		int y = arg0.getY();
		if (showMainMenu)
		{
			// pressed play button
			if (x <= 1040 && x >= 850 && y <= 525 && y >= 450)
			{
				System.out.println("Game started");
				showMainMenu = false; // moves to actual game screen
			}
			// pressed quit
			if (x <= 1050 && x >= 850 && y <= 650 && y >= 575)
			{
				System.out.println("Game quit");
				System.exit(0); // exits the game
			}
		}
		
		System.out.println(arg0.getX() + ", " + arg0.getY()); // mouse click coordinates, for debugging
		revalidate();
		repaint();
		
	}

	public void mouseReleased(MouseEvent arg0) {}
}
