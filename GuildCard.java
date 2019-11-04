import java.util.ArrayList;

public class GuildCard extends Card
{
	private String id;
	
	public GuildCard(String name, String color, ArrayList<String> cost, ArrayList<String> chain, int num, String id)
	{
		super(name, color, cost, chain, num);
	}
}
