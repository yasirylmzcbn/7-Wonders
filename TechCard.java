import java.util.ArrayList;

public class TechCard extends Card
{
	private String techGiven;
	
	//constructor
	public TechCard(String name, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers,String techGiven) 
	{
		super(name, col, ch, co, numOfPlayers);
		this.techGiven=techGiven;
	}
	
	//getters and setters
	public String getTechGiven() 
	{
		return techGiven;
	}
	public void setTechGiven(String techGiven) 
	{
		this.techGiven = techGiven;
	}
	
	public String toString()
	{
		return super.toString() + String.format("Tech: %-5s", getTechGiven());
	}
}
