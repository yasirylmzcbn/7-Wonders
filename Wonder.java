import java.util.*;

public class Wonder {
	private String name;
	private HashMap<String, HashSet<Card>> cardsPlayed; //String = type/color of card, HashSet<Card> = cards
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
	private ArrayList<Card> stages=new ArrayList<Card>();
	private String wonderAbility;

	
	public Wonder(String n, int i) {
		name = n;
		numOfPlayers = i;
		playerWonders = 0;
		money = 3;
		victoryPoints = 0;	
		militaryPower = 0;
		action = "";
		trades = new ArrayList<String>();
		losses = wins = 0;
		cardsPlayed = new HashMap<String, HashSet<Card>>();
		cardsPlayed.put("Brown",new HashSet<Card>());
		cardsPlayed.put("Silver",new HashSet<Card>());
		cardsPlayed.put("Red",new HashSet<Card>());
		cardsPlayed.put("Blue",new HashSet<Card>());
		cardsPlayed.put("Green",new HashSet<Card>());
		cardsPlayed.put("Yellow",new HashSet<Card>());
		cardsPlayed.put("Purple",new HashSet<Card>());
		cardsPlayed.put("Wonder",new HashSet<Card>());
		//hard coded wonders
    if(name.equals("The Colossus of Rhodes")) {
			ArrayList<String> cor = new ArrayList<String>();
			cor.add("ore");

			cardsPlayed.get("Wonder").add(new ResourceCard("The Colossus of Rhodes","brown",null,null,3,cor));

			//wonder 1
			ArrayList<String> cor1Cost = new ArrayList<String>();
			cor1Cost.add("wood");
			cor1Cost.add("wood");
			stages.add(new CivicsCard("cor1","wonder",cor1Cost,null,3,3));
			//wonder 2
			ArrayList<String> cor2Cost = new ArrayList<String>();
			for(int k=0;k<3;k++)
				cor2Cost.add("clay");
			stages.add(new MilitaryCard("cor2","wonder",cor2Cost,null,3,2));
			//wonder 3
			ArrayList<String> cor3Cost = new ArrayList<String>();
			for(int k=0;k<4;k++)
				cor3Cost.add("ore");
			stages.add(new CivicsCard("cor3","wonder",cor3Cost,null,3,7));
		}
		else if(name.contentEquals("The Lighthouse of Alexandria")) {
			//wonder 1
			ArrayList<String> loa1Cost = new ArrayList<String>();
			loa1Cost.add("wood");
			loa1Cost.add("wood");
			stages.add(new CivicsCard("cor1","wonder",loa1Cost,null,3,3));
			//wonder 2
			ArrayList<String> loa2Cost = new ArrayList<String>();
			for(int k=0;k<3;k++)
				loa2Cost.add("clay");
			stages.add(new MilitaryCard("cor2","wonder",loa2Cost,null,3,2));
			//wonder 3
			ArrayList<String> loa3Cost = new ArrayList<String>();
			for(int k=0;k<4;k++)
				loa3Cost.add("ore");
			stages.add(new CivicsCard("cor3","wonder",loa3Cost,null,3,7));
		}
		else if(name.contentEquals("The Temple of Artemis in Ephesus")) {
			//wonder 1
			ArrayList<String> toaie1Cost = new ArrayList<String>();
			toaie1Cost.add("stone");
			toaie1Cost.add("stone");
			stages.add(new CivicsCard("cor1","wonder",toaie1Cost,null,3,3));
			//wonder 2
			ArrayList<String> toaie2Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				toaie2Cost.add("wood");
			ArrayList<String> toaie2Resource = new ArrayList<String>();
			for(int k=0;k<9;k++)
				toaie2Resource.add("coin");
			stages.add(new ResourceCard("cor2","wonder",toaie2Cost,null,3,toaie2Resource));
			//wonder 3
			ArrayList<String> toaie3Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				toaie3Cost.add("paper");
			stages.add(new CivicsCard("cor3","wonder",toaie3Cost,null,3,7));
		}
		else if(name.equals("The Hanging Gardens of Babylon")) {
		ArrayList<String> hgob = new ArrayList<String>();
		//cor.add("ore");
  
		HashSet<Card> temp = cardsPlayed.get("Wonder");
		
		cardsPlayed.get("Brown").add(new ResourceCard("The Colossus of Rhodes","brown",null,null,3,cor));

		//wonder 1
		ArrayList<String> cor1Cost = new ArrayList<String>();
		cor1Cost.add("wood");
		cor1Cost.add("wood");
		stages.add(new CivicsCard("cor1","wonder",cor1Cost,null,3,3));
		//wonder 2
		ArrayList<String> cor2Cost = new ArrayList<String>();
		for(int k=0;k<3;k++)
			cor2Cost.add("clay");
		stages.add(new MilitaryCard("cor2","wonder",cor2Cost,null,3,2));
		//wonder 3
		ArrayList<String> cor3Cost = new ArrayList<String>();
		for(int k=0;k<4;k++)
			cor3Cost.add("ore");
		stages.add(new CivicsCard("cor3","wonder",cor3Cost,null,3,7));
	}
		
		
  }

	
	public void changeHands(int i) {
		if((hand+i)<=numOfPlayers||(hand+i)>=0)

		hand=hand+i;
		else {
			if(i<0)
				hand=numOfPlayers;
			else
				hand=0;
		}

	}
	public ArrayList<Card> getAllPlayerResources() {
		
		ArrayList<Card> list = new ArrayList<Card>();
		return list;
	}
	public void playCard(Card c) {
		if(playable(c))
			System.out.println("You built "+ c.getName() + ", a " + c.getColor()+" card, by paying " + c.getCost());
		else System.out.println("You have already built this card");
	}
  
	public void burnCard() {
		money +=3;
	}
	public int buildWonder() {
		//not done
		++playerWonders;
		if(playerWonders == 1)
			victoryPoints += 3;
		else if(playerWonders == 2)
			victoryPoints += 7;
		
		return playerWonders;
	}
	public boolean playable(Card c) {
		if(cardsPlayed.get(c.getColor()).contains(c))
			return false;
		return true;
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
		//end of getter/setters

}
