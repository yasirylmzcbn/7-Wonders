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
}
