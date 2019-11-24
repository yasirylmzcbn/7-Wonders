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
	public SevenWondersPanel()
	{
		super();
		game = new GameState();
		addMouseListener(this);
		//setLayout(null);
	}
	
	public void paint(Graphics g) 
	{
	}
	
	public void mousePressed(MouseEvent e)
	{
	}
	
	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

}
