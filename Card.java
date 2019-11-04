import java.util.*;


public abstract class Card
{
	private String name;
	private ArrayList<String> chain = new ArrayList<String>();
	private ArrayList<String> cost = new ArrayList<String>();
	private int numberOfPlayers;
	private String color;
	
	
	public Card(String na, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers) {
		name = na;
		color = col;
		chain = ch;
		cost = co;
		numberOfPlayers = numOfPlayers;
	}
	
	//getters
	public String getName() {
		return name;
	}
	public ArrayList<String> getChain() {
		return chain;
	}
	public ArrayList<String> getCost() {
		return cost;
	}
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	public String getColor() {
		return color;
	}
	
	//setters
	public void setName(String na) {
		name = na;
	}
	public void setChain(ArrayList<String> chain) {
		this.chain = chain;
	}
	public void setCost(ArrayList<String> cost) {
		this.cost = cost;
	}
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	public String toString() {
		return name + " - " + color +" Card | Cost: " + cost;
		// Format:Arsenal - Red Card | Cost: 2 wood, 1 ore, 1 loom 
		// idk if we have to display the numberOfPlayers required to play the card
	}
	
}