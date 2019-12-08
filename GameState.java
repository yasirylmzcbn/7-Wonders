import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/*
 * TODO
 * 
 */
public class GameState
{
	public static final String[] WONDERNAMES = {"The Colossus of Rhodes",
												"The Lighthouse of Alexandria",
												"The Temple of Artemis in Ephesus",
												"The Pyramids of Giza",
												"The Statue of Zeus in Olympia",
												"The Hanging Gardens of Babylon",
												"The Mausoleum of Halicarnassus"};
	private boolean endOfGame;
	private boolean warTime;

	private int currentPlayer;
	private int age;
	private int numberOfPlayers;
	private int order; // 1 if clockwise, -1 if anticlockwise
	private int round; // rounds 1 through 6 (since last card is burned)
	private int tradingCost; // cost of trading with other players PLUS coins you have selected

	private ArrayList<Boolean> decisionMade; // if all players have made decision for the turn
	private ArrayList<Card> graveyard; // stores all cards discarded
	private ArrayList<String> selectedResources; // the player's currently selected resources
	//^we dont really need this anymore
	private ArrayList<Wonder> wonders;
	private ArrayList<ArrayList<Card>> playerHands;
	private HashMap<Integer, ArrayList<Card>> deck; // Integer=age, ArrayList=Cards themselves
		//0,1,2 not 123
	private boolean halic;
	private boolean usedOlympia;
	
	// for resource selection
	private String[] ownSelected, leftSelected, rightSelected;
	
	

	
	private Scanner input; // parses through card files
		
	/*
	 * Initialises all variables
	 * Reads in and initialises cards with readCards() method
	 * Creates a random wonder for each player using the WONDERNAMES variable
	 */
	public GameState()
	{
		endOfGame = false;
		warTime = false;
		age = 1;
		numberOfPlayers = 3;
		order = 1;
		round = 1;
		currentPlayer = (int)(Math.random() * numberOfPlayers);
		tradingCost = 0;
		
		// sets all decisionsMade to false for the start of the game
		decisionMade = new ArrayList<Boolean>();
		for (int i = 0; i < numberOfPlayers; i++)
			decisionMade.add(false);
		
		graveyard = new ArrayList<Card>();
		
		selectedResources = new ArrayList<String>();
		
		halic = false;
		usedOlympia = false;
		
		// Creates a random wonder for each player using the WONDERNAMES variable
		wonders = new ArrayList<Wonder>();
		HashSet<Integer> randomWonder = new HashSet<Integer>();
		while (randomWonder.size() < numberOfPlayers)
			randomWonder.add((int)(Math.random() * 7) + 0);  
		Iterator<Integer> iter = randomWonder.iterator();
		for (int i = 0; i < numberOfPlayers; i++)
			wonders.add(new Wonder(WONDERNAMES[iter.next()], numberOfPlayers));
		
		//sets neighbors in trades
		for(int i = 0 ; i<wonders.size(); i++)
		{
			TreeMap<String, Integer> temp = wonders.get(i).getTrades();
			temp.put(getLeftWonder(i).getName(), 0);
			temp.put(getRightWonder(i).getName(), 0);
			
		}
		// sets the hand number of each player
		for (int i = 0; i < numberOfPlayers; i++)
			wonders.get(i).setHand(i);
		
		// initialises each player's hand but does NOT add any cards
		playerHands = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < numberOfPlayers; i++)
			playerHands.add(new ArrayList<Card>());
		
		// initialises every card in the game
		readCards();
		
		// passes out the card for each player
		passOutHands();
		
		initSelection();
		graveyard.addAll(deck.get(1));
	}
	
	public void initSelection()
	{
		tradingCost = 0;
		ownSelected = wonders.get(currentPlayer).getAllPlayerResources().toArray(new String[0]);
		leftSelected = wonders.get(leftPlayer(currentPlayer)).getCardResources().toArray(new String[0]);
		rightSelected = wonders.get(rightPlayer(currentPlayer)).getCardResources().toArray(new String[0]);
	}
	
	public int leftPlayer(int num)
	{
		int next = num - 1;
		if (next < 0)
			next = numberOfPlayers - 1;
		return next;
	}
	
	public int rightPlayer(int num)
	{
		int next = num + 1;
		if (next >= numberOfPlayers)
			next = 0;
		return next;
	}
	
	public Wonder getCurrentWonder()
	{
		return wonders.get(currentPlayer);
	}
	
	public ArrayList<Integer> finalPoints()
	{
		ArrayList<Integer> points = new ArrayList<Integer>(wonders.size());
		int totalPoints;
		// each player
		for (int i = 0; i < wonders.size(); i++)
		{
			totalPoints = 0;
			Wonder currentWonder = wonders.get(i);
			
			
			// military calculation
			totalPoints += currentWonder.getLosses() + currentWonder.getWins();
			
			// points from coins
			totalPoints += currentWonder.getMoney() / 3;
			
			// points from each wonder built
			totalPoints += currentWonder.getVictoryPoints();

			// points from civics cards
			ArrayList<Card> list = new ArrayList<Card>();
			list.addAll(currentWonder.getCardsPlayed().get("blue"));	
			for(int j = 0; j < list.size(); j++) {
				CivicsCard c = (CivicsCard) list.get(j);
				totalPoints += c.getVictoryPoints();
			}
			
			// points from scientific structures
			boolean hasBabylon = wonders.get(i).getName().equals("The Hanging Gardens of Babylon")&&wonders.get(i).getPlayerWonders()>=2;
			boolean hasScientist = false;
			ArrayList<Card> cds = new ArrayList<Card>();
			cds.addAll(wonders.get(i).getCardsPlayed().get("purple"));
			for(int c = 0; i<cds.size();c++)
			{
				if(cds.get(c).getName().equals("Scientists Guild"))
				{
					hasScientist = true;
				}
			}
			int sum = 0;
			int origTab = currentWonder.getTechCardPoints().get("tablet");
			int origGear = currentWonder.getTechCardPoints().get("gear");
			int origComp = currentWonder.getTechCardPoints().get("compass");
			
			if(hasBabylon&&hasScientist)
			{
				
				
				if(calcTech(origTab+2,origGear,origComp)>sum)//2 tabs
				{
					sum = calcTech(origTab+2,origGear,origComp);
				}
				if(calcTech(origTab,origGear+2,origComp)>sum)//2 gears
				{
					sum = calcTech(origTab,origGear+2,origComp);
				}
				if(calcTech(origTab,origGear,origComp+2)>sum)//2 compass
				{
					sum = calcTech(origTab,origGear,origComp+2);
				}
				if(calcTech(origTab+1,origGear+1,origComp)>sum)//1 tab + 1 gear
				{
					sum = calcTech(origTab+1,origGear+1,origComp);
				}
				if(calcTech(origTab+1,origGear,origComp+1)>sum)//1 tab + 1 compass
				{
					sum = calcTech(origTab+1,origGear,origComp+1);
				}
				if(calcTech(origTab,origGear+1,origComp+1)>sum)//1 compass + 1 gear
				{
					sum = calcTech(origTab,origGear+1,origComp+1);
				}
				totalPoints+=sum;
			}
			else if(hasBabylon||hasScientist)
			{
				if(calcTech(origTab+1,origGear,origComp)>sum)//1 tab
				{
					sum = calcTech(origTab+1,origGear,origComp);
				}
				if(calcTech(origTab,origGear+1,origComp)>sum)//1 gear
				{
					sum = calcTech(origTab,origGear+1,origComp);
				}
				if(calcTech(origTab,origGear,origComp+1)>sum)//1 compass
				{
					sum = calcTech(origTab,origGear,origComp+1);
				}
				totalPoints+=sum;
			}
			else
			{
				totalPoints += calcTech(origTab, origGear, origComp);
			}
				
			
				
			// points from commercial structures
			ArrayList<Card> crds = new ArrayList<Card>();
			crds.addAll(currentWonder.getCardsPlayed().get("yellow"));
			int numOfSilverCards = currentWonder.getCardsPlayed().get("silver").size();
			int numOfBrownCards = currentWonder.getCardsPlayed().get("brown").size();
			int numOfYellowCards = currentWonder.getCardsPlayed().get("yellow").size();
			for (int j = 0; j < crds.size(); j++) {
				if(crds.get(j).getName().equals("Chamber Of Commerce")) 
					totalPoints += numOfSilverCards*2;
				
				if(crds.get(j).getName().equals("Haven"))
					totalPoints += numOfBrownCards;
					
				if(crds.get(j).getName().equals("Lighthouse"))
					totalPoints += numOfYellowCards;
			}
			
			// points from guilds
			Wonder leftWonder = getLeftWonder(i);
			Wonder rightWonder = getRightWonder(i);
			ArrayList<Card> guilds = new ArrayList<Card>();
			guilds.addAll(currentWonder.getCardsPlayed().get("purple"));
			for (int j = 0; j < guilds.size(); j++) {
				if(guilds.get(i).getName().equals("Builders Guild")) 
					totalPoints += currentWonder.getPlayerWonders()
								+ leftWonder.getPlayerWonders()
								+ rightWonder.getPlayerWonders();
				
				if(guilds.get(i).getName().equals("Craftmens Guild"))
					totalPoints += 2 * (leftWonder.getCardsPlayed().get("silver").size()
								+ rightWonder.getCardsPlayed().get("silver").size());
				
				if(guilds.get(i).getName().equals("Magistrates Guild"))
					totalPoints += leftWonder.getCardsPlayed().get("blue").size()
								+ rightWonder.getCardsPlayed().get("blue").size();
			
				if(guilds.get(i).getName().equals("Philosophers Guild"))
					totalPoints += leftWonder.getCardsPlayed().get("green").size()
								+ rightWonder.getCardsPlayed().get("green").size();
				
				if(guilds.get(i).getName().equals("Shipowners Guild"))
					totalPoints += currentWonder.getCardsPlayed().get("brown").size()
								+ currentWonder.getCardsPlayed().get("silver").size()
								+ currentWonder.getCardsPlayed().get("purple").size();
				
				if(guilds.get(i).getName().equals("Strategists Guild"))
					totalPoints += leftWonder.getLosses()
								+ rightWonder.getLosses();
				
				if(guilds.get(i).getName().equals("Traders Guild"))
					totalPoints += leftWonder.getCardsPlayed().get("yellow").size()
								+ rightWonder.getCardsPlayed().get("yellow").size();
				
				if(guilds.get(i).getName().equals("Workers Guild"))
					totalPoints += leftWonder.getCardsPlayed().get("brown").size()
								+ rightWonder.getCardsPlayed().get("brown").size();
				
				if(guilds.get(i).getName().equals("Spies Guild"))
					totalPoints += leftWonder.getCardsPlayed().get("red").size()
								+ rightWonder.getCardsPlayed().get("red").size();
			}
			
			// add total points for each player to the list
			points.add(totalPoints);
		}
		return points;
	}
	
	public int calcTech(int tab, int gear, int compass)
	{
		int sum = 0;
		int each;
			//each = currentWonder.getTechCardPoints().get("tablet");
		sum += tab*tab;
			//each = currentWonder.getTechCardPoints().get("gear");
		sum += gear*gear;
			//each = currentWonder.getTechCardPoints().get("compass");
		sum += compass*compass;
		
		if(tab >= 1&& gear >= 1&& compass >= 1) 
		{ 
			int min = Math.min(tab, gear);
			min = Math.min(min, compass);
				sum += min*7;
		}	
		return sum;
	}
	
	/*
	 * Returns index of player who is in first place
	 */
	public String whoWon()
	{
		int winner = 0;
		ArrayList<Integer> finalPoints = finalPoints();
		for (int i = 0; i < finalPoints.size(); i++)
			if (finalPoints.get(i) > finalPoints.get(winner))
				winner = i;
		return winner+""+finalPoints.get(winner);
	}
	public TreeMap<String, Integer> standings()
	{
		TreeMap<String, Integer> stand = new TreeMap<String, Integer>();
		ArrayList<Integer> finalPoints = finalPoints();
		int size = finalPoints().size();
		for(int i = 0; i<size;i++)
		{
			int winner = 0;
			int winnerP = 0;
			for (int j = 0; j < finalPoints.size(); j++)
				if (finalPoints.get(i) > finalPoints.get(winner))
					winner = i;
			finalPoints.remove(winner);
			stand.put(wonders.get(winner).getName(),winnerP);
		}
		return stand;
	}
	public void rotateHands()
	{
		for(Wonder w: wonders)
		{
			w.changeHands(order);
		}
	}
	
	/*
	 * Plays the action of each wonder (burn, build, or play) using the action String
	 * Trades with the player if chosen to
	 */
	public void finishRound()
	{
		for(Wonder w: wonders)
		{
			if(w.getAction().contentEquals("Burn"))
			{
				w.burnCard();
			}
			else if(w.getAction().contentEquals("Build"))
			{
				w.buildWonder();
			}
			else if(w.getAction().contentEquals("Play"))
			{
				w.playCard(w.getSelectedCard());
			}
		}
		
		
		for(Wonder w: wonders)
		{
			if(w.getAction().contentEquals("Play"))
			{
				// points from commercial structures
				Card temp = w.getSelectedCard();
				int numOfSilverCards = w.getCardsPlayed().get("silver").size();
				int numOfBrownCards = w.getCardsPlayed().get("brown").size();
				int numOfYellowCards = w.getCardsPlayed().get("yellow").size();
				
				
				if(temp.getName().equals("Chamber Of Commerce")) 
					w.addMoney(numOfSilverCards*2);
				
				if(temp.getName().equals("Haven"))
					 w.addMoney(numOfBrownCards);
					
				if(temp.getName().equals("Lighthouse"))
					w.addMoney(numOfYellowCards);
				
				if(temp.getName().equals("Arena"))
				{
					w.addMoney(3*w.getPlayerWonders());
				}
					
				//For neighbors and themselves 
				if(temp.getName().equals("Vineyard"))
				{
					int rightBrown = getRightWonder(w).getCardsPlayed().get("brown").size();
					int leftBrown = getLeftWonder(w).getCardsPlayed().get("brown").size();
					w.addMoney(numOfBrownCards+rightBrown+leftBrown);
				}
				if(w.getSelectedCard().getCost().contains("coin"))
				{
					for(int i = 0; i<w.getSelectedCard().getCost().size();i++)
					{
						if(w.getSelectedCard().getCost().get(i).contentEquals("coin"))
						{
							w.addMoney(-1);
						}
					}
				}
				
			}
			if(w.getAction().contentEquals("Build"))
			{
				//add the card construction marker
				w.getCardsPlayed().get("wonder").add(w.getSelectedCard());
				
				if(w.getName().equals("The Mausoleum of Halicarnassus")&&w.getPlayerWonders()==2)
					halic = true;
			}
			w.setAction("");
			w.setSelectedCard(null);
			
		}
		/*
		for(int i = 0; i<wonders.size(); i++)
		{
			Wonder w = wonders.get(i);
			ArrayList<String> trades = w.getTrades();
			for(String str: trades)
			{
				String temp = "";
				for(String s: WONDERNAMES)
				{
					if(str.contains(s))
					{
						temp = s;
					}
				}
				int tradeV = Integer.parseInt(str.substring(str.length()));
				if(temp.equals(getLeftWonder(i).getName()))
				{
					getLeftWonder(i).addMoney(tradeV);
					w.addMoney(-tradeV);
				}
				if(temp.equals(getRightWonder(i).getName()))
				{
					getRightWonder(i).addMoney(tradeV);
					w.addMoney(-tradeV);
				}
			}
			w.getTrades().clear();
			
		}*/
		
		for(int i = 0; i<wonders.size(); i++)
		{
			Wonder w = wonders.get(i);
			TreeMap<String, Integer> trades = w.getTrades();
			int tradeV = trades.get(getLeftWonder(i).getName());
			getLeftWonder(i).addMoney(tradeV);
			w.addMoney(-tradeV);
			
			trades.put(getLeftWonder(i).getName(), 0);
			
			tradeV = trades.get(getRightWonder(i).getName());
			getRightWonder(i).addMoney(tradeV);
			w.addMoney(-tradeV);
			
			trades.put(getRightWonder(i).getName(), 0);
			
			
		}
		
		for (int i = 0; i < decisionMade.size(); i++)
			decisionMade.set(i, false);
		
	}


	public void nextRound()
	{
		round++;
		rotateHands();
		if(round == 7)
		{
			order = -1;
			
			for(int i = 0; i<playerHands.size();i++)
			{
				ArrayList<Card> temp = playerHands.get(i);
				graveyard.add(temp.remove(0));
			}
			//playerHands.clear();
			warTime();
			age++;
			
			// configures guild cards
			if (age == 3)
			{
				order = 1;
				
				ArrayList<Card> guilds = new ArrayList<Card>();
				ArrayList<Card> temp = deck.get(age-1);
				
				// puts all purple cards in guilds
				for (int i = 0; i < deck.get(age-1).size(); i++)
				{
					Card tempCard = temp.get(i);
					if (tempCard.getColor().equals("purple"))
					{
						guilds.add(temp.remove(i));
					}
				}
				
				// randomly selects guild cards
					Collections.shuffle(guilds);
					
					for (int i = 0; i < numberOfPlayers + 2; i++)
						temp.add(guilds.get(i));
			}
			
			round = 1; //Resets Round
			// ends game
			if(age == 4)
			{
				endOfGame = true;
			}
			else
			{
				passOutHands(); //NEED THIS
			}
			
			usedOlympia = false;
			
		}
		halic = false;
	}
	public void nextPlayer()
	{
		currentPlayer++;
		if(currentPlayer == numberOfPlayers)
		{
			currentPlayer = 0;
		}
		
		initSelection();
	}
	
	public boolean canUseOlympia()
	{
		boolean isOlympia = wonders.get(currentPlayer).getName().contentEquals("The Statue of Zeus in Olympia");
		boolean hasStage2 = wonders.get(currentPlayer).getPlayerWonders()>=2;
		
		return isOlympia&&hasStage2&&!usedOlympia;
	}
	
	public void passOutHands()
	{
		int ageIndex = age - 1;
		ArrayList<Card> ageDeck = deck.get(ageIndex);
		Collections.shuffle(ageDeck);
		
		for (int i = 0; i < numberOfPlayers; i++)
		{
			ArrayList<Card> playerCards = playerHands.get(i);
			
			
			for (int j = 0; j < 7; j++)
			{
				if (ageDeck.size() > 0)
					playerCards.add(ageDeck.remove(0));
			}
		}
	}


	/*
	 * warTime() gives each wonder either wins or losses. For each wonder, it only goes to war with the far right to prevent double conflicts
	 */
	public void warTime()
	{
		// for each wonder
		for (int i = 0; i < wonders.size(); i++)
		{
			Wonder rightWonder = getRightWonder(i);
			Wonder ownWonder = wonders.get(i);
			int rightPower = rightWonder.getMilitaryPower();
			int ownPower = ownWonder.getMilitaryPower();
			
			if (ownPower > rightPower)
			{
				if (age == 1)
					ownWonder.setWins(ownWonder.getWins() + 1);
				else if (age == 2)
					ownWonder.setWins(ownWonder.getWins() + 3);
				else if (age == 3)
					ownWonder.setWins(ownWonder.getWins() + 5);

				rightWonder.setLosses(rightWonder.getLosses() + 1);
			}
			if (ownPower < rightPower)
			{
				if (age == 1)
					rightWonder.setWins(rightWonder.getWins() + 1);
				else if (age == 2)
					rightWonder.setWins(rightWonder.getWins() + 3);
				else if (age == 3)
					rightWonder.setWins(rightWonder.getWins() + 5);
				
				ownWonder.setLosses(ownWonder.getLosses() + 1);
			}
		}
	}
	
	public Wonder getLeftWonder(Wonder wonder)
	{
		int leftWonderPos = -1;
		for (int i = 0; i < numberOfPlayers; i++)
		{
			Wonder temp = wonders.get(i);
			if (temp.getName().equals(wonder.getName()))
			{
				leftWonderPos = i - 1;
				if (leftWonderPos < 0)
					leftWonderPos = numberOfPlayers - 1;
				return wonders.get(leftWonderPos);
			}
				
		}
		return null;
	}
	public Wonder getLeftWonder(int wonder)
	{
		Wonder ownWonder = wonders.get(wonder);
		
		int leftWonder = wonder - 1;
		if (leftWonder < 0)
			leftWonder = numberOfPlayers-1;
		return wonders.get(leftWonder);
	}
	
	public Wonder getRightWonder(Wonder wonder)
	{
		int rightWonderPos = -1;
		for (int i = 0; i < numberOfPlayers; i++)
		{
			Wonder temp = wonders.get(i);
			if (temp.getName().equals(wonder.getName()))
			{
				rightWonderPos = i + 1;
				if (rightWonderPos > numberOfPlayers - 1)
					rightWonderPos = 0;
				return wonders.get(rightWonderPos);
			}
				
		}
		return null;
	}
	public Wonder getRightWonder(int wonder)
	{
		Wonder ownWonder = wonders.get(wonder);
		
		int rightWonder = wonder + 1;
		if (rightWonder > numberOfPlayers-1)
			rightWonder = 0;
		return wonders.get(rightWonder);
	}
	
	/*
	 * Creates every card through the text files for age 1, 2, and 3
	 * private HashMap<Integer, ArrayList<Card>> deck;
	 */
	public void readCards()
	{
		deck = new HashMap<Integer, ArrayList<Card>>();
		
		for (int i = 0; i < 3; i++)
		{
			try {
				input = new Scanner(new File(String.format("src/cardFiles/Age%d.txt", i + 1)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			ArrayList<Card> tempCards = new ArrayList<Card>();
			while (input.hasNext())
			{
				String vals[] = input.nextLine().split("/");
				String type = vals[0];
				String name = vals[1];
				String color = vals[2];
				ArrayList<String> chain = new ArrayList<String>(Arrays.asList(vals[3].split(",")));//holds what can make this card free
				ArrayList<String> cost = new ArrayList<String> (Arrays.asList(vals[4].split(",")));
				
				int players = Integer.parseInt(vals[5]);
				
				if (players >= numberOfPlayers) {
					if (type.equals("resourceCard")) {
						ArrayList<String> resource = new ArrayList<String>(Arrays.asList(vals[6].split(",")));
						ResourceCard card = new ResourceCard(name, color, chain, cost, players, resource);
						tempCards.add(card);
					}
					else if (type.equals("militaryCard")) {
						int militaryPower = Integer.parseInt(vals[6]);
						MilitaryCard card = new MilitaryCard(name, color, chain, cost, players, militaryPower);
						tempCards.add(card);
					}
					else if (type.equals("techCard")) {
						String techGiven = vals[6];
						TechCard card = new TechCard(name, color, chain, cost, players, techGiven);
						tempCards.add(card);
					}
					else if (type.equals("civicsCard")) {
						int vp = Integer.parseInt(vals[6]);
						CivicsCard card = new CivicsCard(name, color, chain, cost, players, vp);
						tempCards.add(card);
					}
					else if (type.equals("guildCard")) {
						String id = vals[6];
						GuildCard card = new GuildCard(name, color, chain, cost, players, id);
						tempCards.add(card);
					}
					else if (type.equals("commercialCard")) {
						String id = vals[6];
						CommercialCard card = new CommercialCard(name, color, chain, cost, players, id);
						tempCards.add(card);
					}
				}
			}
			
			//IF AGE == 3 then change the guild cards accordingly
			deck.put(i, tempCards);
		}
	
	}
	
	public boolean allDecisionsMade()
	{
		return !decisionMade.contains(false);
	}

	public boolean isEndOfGame() {
		return endOfGame;
	}


	public void setEndOfGame(boolean endOfGame) {
		this.endOfGame = endOfGame;
	}


	public boolean isWarTime() {
		return warTime;
	}


	public void setWarTime(boolean warTime) {
		this.warTime = warTime;
	}

	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}


	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public int getRound() {
		return round;
	}


	public void setRound(int round) {
		this.round = round;
	}


	public ArrayList<Boolean> getDecisionMade() {
		return decisionMade;
	}


	public void setDecisionMade(ArrayList<Boolean> decisionMade) {
		this.decisionMade = decisionMade;
	}


	public ArrayList<Card> getGraveyard() {
		return graveyard;
	}


	public void setGraveyard(ArrayList<Card> graveyard) {
		this.graveyard = graveyard;
	}


	public ArrayList<String> getSelectedResources() {
		return selectedResources;
	}


	public void setSelectedResources(ArrayList<String> selectedResources) {
		this.selectedResources = selectedResources;
	}


	public ArrayList<Wonder> getWonders() {
		return wonders;
	}


	public void setWonders(ArrayList<Wonder> wonders) {
		this.wonders = wonders;
	}


	public ArrayList<ArrayList<Card>> getPlayerHands() {
		return playerHands;
	}


	public void setPlayerHands(ArrayList<ArrayList<Card>> playerHands) {
		this.playerHands = playerHands;
	}


	public HashMap<Integer, ArrayList<Card>> getDeck() {
		return deck;
	}


	public void setDeck(HashMap<Integer, ArrayList<Card>> deck) {
		this.deck = deck;
	}


	public Scanner getInput() {
		return input;
	}


	public void setInput(Scanner input) {
		this.input = input;
	}

	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public boolean isHalic() {
		return halic;
	}

	public void setHalic(boolean halic) {
		this.halic = halic;
	}

	public boolean isUsedOlympia() {
		return usedOlympia;
	}

	public void setUsedOlympia(boolean usedOlympia) {
		this.usedOlympia = usedOlympia;
	}

	public static String[] getWondernames() {
		return WONDERNAMES;
	}

	public String[] getOwnSelected() {
		return ownSelected;
	}

	public void setOwnSelected(String[] ownSelected) {
		this.ownSelected = ownSelected;
	}

	public String[] getLeftSelected() {
		return leftSelected;
	}

	public void setLeftSelected(String[] leftSelected) {
		this.leftSelected = leftSelected;
	}

	public String[] getRightSelected() {
		return rightSelected;
	}

	public void setRightSelected(String[] rightSelected) {
		this.rightSelected = rightSelected;
	}

	public int getTradingCost() {
		return tradingCost;
	}

	public void setTradingCost(int tradingCost) {
		this.tradingCost = tradingCost;
	}
	
	public void plusCost(int cost)
	{
		this.tradingCost += cost;
	}
	
	public void minusCost(int cost)
	{
		this.tradingCost -= cost;
	}
}
