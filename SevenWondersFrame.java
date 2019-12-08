import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class SevenWondersFrame extends JFrame
{
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1080;

	public SevenWondersFrame(String frameName)
	{
		super(frameName);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH,HEIGHT);
		add(new SevenWondersPanel(WIDTH, HEIGHT));
		//setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		
		try {
			setIconImage(ImageIO.read(new File("src/images/icon.png")));
		} catch (IOException e) {
			System.out.println("Could not load icon!");
		}
		
		setVisible(true);
	}
	public static void main(String [ ]args) //throws IOException
	{
		SevenWondersFrame frame = new SevenWondersFrame("SevenWondersPanel");
	}
}