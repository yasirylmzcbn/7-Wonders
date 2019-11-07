import java.util.*;

public class Wonder extends GameState {
	private String name;
	private HashMap<String, HashSet<Card>> cardsPlayed;
	private int numOfPlayers;
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
	private String WonderAbility;
	
	
	public Wonder(String n, int i) {
		name = n;
		numOfPlayers = i;
		playerWonders = 0;
		money = 3;
		victoryPoints = 0;	
		militaryPower = 0;
		action = "";
		trades = new ArrayList<String>();
<<<<<<< Updated upstream
  }

	private String getName() {
=======
		losses = wins = 0;
		
  }

	public String getName() {
>>>>>>> Stashed changes
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	private HashMap<String, HashSet<Card>> getCardsPlayed() {
		return cardsPlayed;
	}

	private void setCardsPlayed(HashMap<String, HashSet<Card>> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}

	private int getPlayerWonders() {
		return playerWonders;
	}

	private void setPlayerWonders(int playerWonders) {
		this.playerWonders = playerWonders;
	}

	private int getMoney() {
		return money;
	}

	private void setMoney(int money) {
		this.money = money;
	}

	private int getVictoryPoints() {
		return victoryPoints;
	}

	private void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	private ListIterator<Card> getIter() {
		return iter;
	}

	private void setIter(ListIterator<Card> iter) {
		this.iter = iter;
	}

	private int getHand() {
		return hand;
	}

	private void setHand(int hand) {
		this.hand = hand;
	}

	private int getMilitaryPower() {
		return militaryPower;
	}

	private void setMilitaryPower(int militaryPower) {
		this.militaryPower = militaryPower;
	}

	private Card getSelectedCard() {
		return selectedCard;
	}

	private void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	private String getAction() {
		return action;
	}

	private void setAction(String action) {
		this.action = action;
	}

	private ArrayList<String> getTrades() {
		return trades;
	}

	private void setTrades(ArrayList<String> trades) {
		this.trades = trades;
	}

	private int getLosses() {
		return losses;
	}

	private void setLosses(int losses) {
		this.losses = losses;
	}

	private int getWins() {
		return wins;
	}

	private void setWins(int wins) {
		this.wins = wins;
	}

	private ArrayList<Card> getStages() {
		return stages;
	}

	private void setStages(ArrayList<Card> stages) {
		this.stages = stages;
	}

	private String getWonderAbility() {
		return WonderAbility;
	}

	private void setWonderAbility(String wonderAbility) {
		WonderAbility = wonderAbility;
	}
	
	
	
	
	public void burnCard() {
		money +=3;
	}

	
	public boolean playable(Card c) {
		if(c.getNumberOfPlayers() > numOfPlayers)
			return false;
		
		return true;
	}
}
