import java.util.*;


public abstract class Card
{
	private String name;
	private ArrayList<String> chain = new ArrayList<String>();
	private ArrayList<String> cost = new ArrayList<String>();
	private int numberOfPlayers;
	private String color;
	
	
	public Card(String na, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers) {
		this.name = na;
		this.color = col;
		this.chain = ch;
		this.cost = co;
		this.numberOfPlayers = numOfPlayers;
	}
	
	//getters
	public String getName() 
	{
		return name;
	}
	public ArrayList<String> getChain() 
	{
		return chain;
	}
	public ArrayList<String> getCost() 
	{
		return cost;
	}
	public int getNumberOfPlayers() 
	{
		return numberOfPlayers;
	}
	public String getColor() 
	{
		return color;
	}
	
	//setters
	public void setName(String na) 
	{
		name = na;
	}
	public void setChain(ArrayList<String> chain) 
	{
		this.chain = chain;
	}
	public void setCost(ArrayList<String> cost) 
	{
		this.cost = cost;
	}
	
	public String toString()
	{
		//return String.format("%-7s %-15s Chain: %-13s Cost: %-25s Players: %-3s", getColor(), getName(), getChain(), getCost(), getNumberOfPlayers());
		return getColor() + " " + getName() + "; Chain:" + getChain() + "; Cost:" + getCost() + "; Players:" + getNumberOfPlayers();
	}
	//im just testing my github
}