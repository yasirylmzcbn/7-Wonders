import java.util.ArrayList;

public class CommercialCard extends Card
{
	// 'id' is the function that the commercial card will perform
	private String id;
	
	public CommercialCard(String name, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers, String id)
	{
		super(name, col, ch, co, numOfPlayers);
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
	
}
