import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class SevenWondersFrame extends JFrame
{

	private static final int WIDTH = 1200;
	private static final int HEIGHT = 800;

	public SevenWondersFrame(String frameName)
	{
	
	super(frameName);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(WIDTH,HEIGHT);
	add(new SevenWondersPanel());
	setVisible(true);
	
		
	}
	public static void main(String [ ]args) //throws IOException
	{
		SevenWondersFrame frame = new SevenWondersFrame("SevenWondersPanel");
	}


}
