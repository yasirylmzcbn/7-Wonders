import java.util.*;

public class ResourceCard extends Card
{
	
	private ArrayList<String> resource;
	public ResourceCard(String name, String color, ArrayList<String> cost, ArrayList<String> chain, int players, ArrayList<String> resources)
	{
		super(name, color, cost, chain, players);
		resource = resources;
		
	}
	public ArrayList<String> getResource() 
	{
		return resource;
	}
	public void setResource(ArrayList<String> resource) 
	{
		this.resource = resource;
	}
	
	public String toString()
	{
		//return super.toString() + String.format("Re: %-20s", getResource());
		return super.toString() + "; Resource: " + getResource();
	}
	
}
