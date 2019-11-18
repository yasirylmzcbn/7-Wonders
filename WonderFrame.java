import java.awt.Dimension;

import javax.swing.JFrame;

public class WonderFrame extends JFrame
{
	// dimensions for the frame and panel
	public static final int WIDTH = 1920, HEIGHT = 1080;
	
	public static void main(String[] args)
	{
		WonderFrame frame = new WonderFrame("Seven Wonders!");
	}
	
	public WonderFrame(String name)
	{
		super(name);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.add(new WonderPanel());
		setVisible(true);
	}
}
