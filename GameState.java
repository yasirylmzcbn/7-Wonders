import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

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
	private int round; // rounds 1 through 7

	private ArrayList<Boolean> decisionMade; // if all players have made decision for the turn
	private ArrayList<Card> graveyard; // stores all cards discarded
	private ArrayList<String> selectedResources; // the player's currently selected resources
	private ArrayList<Wonder> wonders;
	private ArrayList<ArrayList<Card>> playerHands;
	private HashMap<Integer, ArrayList<Card>> deck; // Integer=age, ArrayList=Cards themselves
	
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
		
		// sets all decisionsMade to false for the start of the game
		decisionMade = new ArrayList<Boolean>();
		for (int i = 0; i < numberOfPlayers; i++)
			decisionMade.add(false);
		
		graveyard = new ArrayList<Card>();
		
		selectedResources = new ArrayList<String>();
		
		// Creates a random wonder for each player using the WONDERNAMES variable
		wonders = new ArrayList<Wonder>();
		HashSet<Integer> randomWonder = new HashSet<Integer>();
		while (randomWonder.size() < numberOfPlayers)
			randomWonder.add((int)(Math.random() * 7));
		Iterator<Integer> iter = randomWonder.iterator();
		for (int i = 0; i < numberOfPlayers; i++)
			wonders.add(new Wonder(WONDERNAMES[iter.next()], numberOfPlayers));
		
		// initialises each player's hand but does NOT add any cards
		playerHands = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < numberOfPlayers; i++)
			playerHands.add(new ArrayList<Card>());
		
		// initialises every card in the game
		readCards();
	}
	
	public ArrayList<Integer> finalPoints()
	{
		ArrayList<Integer> points = new ArrayList<Integer>(this.numberOfPlayers);
		int totalPoints;
		// each player
		for (int i = 0; i < this.numberOfPlayers; i++)
		{
			totalPoints = 0;
			Wonder currentWonder = wonders.get(i);
			
			// military calculation
			totalPoints += currentWonder.getLosses() + currentWonder.getWins();
			
			// points from coins
			totalPoints += currentWonder.getMoney() / 3;
			
			// points from each wonder built, blue cards, green cards
			//
			totalPoints += currentWonder.victoryPoints;

			// points from each wonder built and blue cards
			totalPoints += currentWonder.getVictoryPoints();
			
			// points from each wonder built, blue cards, green cards
			totalPoints += currentWonder.getVictoryPoints();

			
			// points from scientific structures
			

			// points from commercial structures
			
			
			// points from guilds
			
			
			
			// add total points for each player to the list
			points.add(totalPoints);
		}
		return points;
	}
	
	/*
	 * Returns index of player who is in first place
	 */
	public int whoWon()
	{
		int winner = 0;
		ArrayList<Integer> finalPoints = finalPoints();
		for (int i = 0; i < finalPoints.size(); i++)
			if (finalPoints.get(i) > finalPoints.get(winner))
				winner = i;
		return winner;
	}
	public void rotateHands()
	{
		for(Wonder w: wonders)
		{
			w.changeHands(order);
		}
	}
	public void finishRound()
	{
		for(Wonder w: wonders)
		{
			
		}
		
	}
	public void nextRound()
	{
		round++;
		if(round == 6)
		{
			warTime();
			age++;
			if(age == 4)
			{
				endOfGame = true;
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
	
	public Wonder getLeftWonder(int wonder)
	{
		Wonder ownWonder = wonders.get(wonder);
		
		int leftWonder = wonder - 1;
		if (leftWonder < 0)
			leftWonder = 3;
		return wonders.get(leftWonder);
	}
	public Wonder getRightWonder(int wonder)
	{
		Wonder ownWonder = wonders.get(wonder);
		
		int rightWonder = wonder + 1;
		if (rightWonder > 3)
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
				ArrayList<String> chain = new ArrayList<String>(Arrays.asList(vals[3].split(",")));
				ArrayList<String> cost = new ArrayList<String> (Arrays.asList(vals[4].split(",")));
				
				int players = Integer.parseInt(vals[5]);
				
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
			//IF AGE == 3 then change the guild cards accordingly
			deck.put(i, tempCards);
		}
	
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
}
