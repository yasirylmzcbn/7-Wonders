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
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	}
	public static void main(String [ ]args) //throws IOException
	{
		SevenWondersFrame frame = new SevenWondersFrame("SevenWondersPanel");
	}
}
