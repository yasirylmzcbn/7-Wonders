import java.util.*;

public class ResourceCard extends Card
{
	
	private boolean allAvailable;
	private ArrayList<String> resource;
	public ResourceCard(String name, String color, ArrayList<String> cost, ArrayList<String> chain, ArrayList<String> resources, int players, boolean allAvailable)
	{
		super(name, color, cost, chain, players);
		
	}
	public boolean isAllAvailable() 
	{
		return allAvailable;
	}
	public void setAllAvailable(boolean allAvailable) 
	{
		this.allAvailable = allAvailable;
	}
	public ArrayList<String> getResource() 
	{
		return resource;
	}
	public void setResource(ArrayList<String> resource) 
	{
		this.resource = resource;
	}
	
}
