import java.util.*;


public abstract class Card
{
	private String ID;
	private ArrayList<String> chain = new ArrayList<String>();
	private ArrayList<String> cost = new ArrayList<String>();
	private int numberOfPlayers;
	private String color;
	
	
	public Card(String name, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers) {
		
	}
	
	
	public String getID() {
		return ID;
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
	
	
}