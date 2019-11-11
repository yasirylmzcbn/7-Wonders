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

	private int age;
	private int numberOfPlayers;
	private int order; // 1 if clockwise, -1 if anticlockwise
	private int round;

	private ArrayList<Boolean> decisionMade; // if all players have made decision for the turn
	private ArrayList<Card> graveyard; // stores all cards discarded
	private ArrayList<String> selectedResources; // the player's currently selected resources
	private ArrayList<Wonder> wonders;
	private ArrayList<ArrayList<Card>> playerHands;
	private HashMap<Integer, ArrayList<Card>> deck; // Integer=age, ArrayList=Cards themselves
	
	private Scanner input; // parses through card files
		
	public GameState()
	{
		endOfGame = false;
		warTime = false;
		age = 1;
		numberOfPlayers = 3;
		order = 1;
		round = 1;
		
		decisionMade = new ArrayList<Boolean>();
		for (int i = 0; i < numberOfPlayers; i++)
			decisionMade.add(false);
		
		graveyard = new ArrayList<Card>();
		
		selectedResources = new ArrayList<String>();
		
		wonders = new ArrayList<Wonder>();
		HashSet<Integer> randomWonder = new HashSet<Integer>();
		while (randomWonder.size() < numberOfPlayers)
			randomWonder.add((int)(Math.random() * 7));
		Iterator<Integer> iter = randomWonder.iterator();
		
		for (int i = 0; i < numberOfPlayers; i++)
			wonders.add(new Wonder(WONDERNAMES[iter.next()], numberOfPlayers));
		
		playerHands = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < numberOfPlayers; i++)
			playerHands.add(new ArrayList<Card>());
		
		readCards();
	}
	
	/*
	 * should this method be moved to wonder.java and this method be used to just call the other ones?
	 */
	// calculated the victory points of each player at the end of the game
	public ArrayList<Integer> finalPoints()
	{
		ArrayList<Integer> points = new ArrayList<Integer>(this.numberOfPlayers);
		
		// each player
		for (int i = 0; i < this.numberOfPlayers; i++)
		{
			int totalPoints = 0;
			Wonder currentWonder = wonders.get(i);
			
			// military calculation
			totalPoints += currentWonder.getLosses() + currentWonder.getWins();
			
			// points from coins
			totalPoints += currentWonder.getMoney() / 3;
			
			// point for each wonder built
			
		}
		return null;
	}
	public int whoWon()
	{
		return 0;
	}
	public void rotateHands()
	{
		
	}
	public void finishRound()
	{
		
	}
	public void nextRound()
	{
		
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
		
		/* temporary method
		for (ArrayList<Card> list: deck.values())
		{
			for (Card c: list)
				System.out.println(c);
			System.out.println();
		}*/
	
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
}
