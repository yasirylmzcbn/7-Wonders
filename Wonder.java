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
	private int losses;
	private int wins;
	private ArrayList<Card> stages;
	private String wonderAbility;
	private int numPlayers;
  private String WonderAbility;
	
	public Wonder(String n, int i) {
		name = n;
		playerWonders = i;
		money = 3;
		victoryPoints = 0;	
		militaryPower = 0;
		action = "";
		trades = new ArrayList<String>();
  }
  
	public void changeHands(int i) {
		
		
		
		
	}
	
	//getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, HashSet<Card>> getCardsPlayed() {
		return cardsPlayed;
	}
	public void setCardsPlayed(HashMap<String, HashSet<Card>> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}
	public int getPlayerWonders() {
		return playerWonders;
	}
	public void setPlayerWonders(int playerWonders) {
		this.playerWonders = playerWonders;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getVictoryPoints() {
		return victoryPoints;
	}
	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}
	public ListIterator<Card> getIter() {
		return iter;
	}
	public void setIter(ListIterator<Card> iter) {
		this.iter = iter;
	}
	public int getHand() {
		return hand;
	}
	public void setHand(int hand) {
		this.hand = hand;
	}
	public int getMilitaryPower() {
		return militaryPower;
	}
	public void setMilitaryPower(int militaryPower) {
		this.militaryPower = militaryPower;
	}
	public Card getSelectedCard() {
		return selectedCard;
	}
	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public ArrayList<String> getTrades() {
		return trades;
	}
	public void setTrades(ArrayList<String> trades) {
		this.trades = trades;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public ArrayList<Card> getStages() {
		return stages;
	}
	public void setStages(ArrayList<Card> stages) {
		this.stages = stages;
	}
	public String getWonderAbility() {
		return wonderAbility;
	}
	public void setWonderAbility(String wonderAbility) {
		this.wonderAbility = wonderAbility;
	}
	public int getNumPlayers() {
		return numPlayers;
	}
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
}
