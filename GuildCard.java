import java.util.ArrayList;

public class GuildCard extends Card
{
	// 'id': what function the guild card will perform 
	private String id;
	
	public GuildCard(String name, String color, ArrayList<String> cost, ArrayList<String> chain, int num, String id)
	{
		super(name, color, cost, chain, num);
		this.setId(id);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	public String toString()
	{
		return super.toString() + String.format("ID: %-5s", getId());
	}
}
