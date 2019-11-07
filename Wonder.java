import java.util.*;

public class Wonder {
	private String name;
	private HashMap<String, HashSet<Card>> cardsPlayed;
	private int playerWonders;
	private int money;
	private int victoryPoints;
	private ListIterator<Card> iter;
	private int hand;
	private int militaryPower;
	private Card selectedCard;
	private String action;
	private ArrayList<String> trades;
	
	
	public Wonder(String n, int i) {
		name = n;
		playerWonders = i;
		money = 3;
		victoryPoints = 0;	
		militaryPower = 0;
		action = "";
		trades = new ArrayList<String>();
	}
}
