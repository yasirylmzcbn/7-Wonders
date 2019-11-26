import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TextRunner
{
	public static GameState state;
	public static Scanner input;
	public static Wonder currentWonder;
	public static int currentPlayer;
	public static ArrayList<Card> currentHand;
	public static boolean endOfRound = false;
	public static boolean hasQuit = false; // if the player has chosen to click cancel out of play, build, or burn
	
	public static void main(String[] args)
	{
		input = new Scanner(System.in);
		state = new GameState();
		while (!state.isEndOfGame())
		{
			printNewRound();
			while (!state.allDecisionsMade())
			{
				/* debugging
				for (int i = 0; i < state.getPlayerHands().size(); i++)
					System.out.println(i + " " + state.getPlayerHands().get(i)); // debugging
				System.out.println();*/
				
				currentWonder = state.getWonders().get(state.getCurrentPlayer());
				currentPlayer = state.getCurrentPlayer();
				currentHand = state.getPlayerHands().get(currentWonder.getHand());
	
				printWonderInformation();	
				printOneLine();	
					
				printPlayedCards();	
				printOneLine();	
					
				printPlayerHand();	
				printOneLine();	
					
				optionSelection();	
				printOneLine();	
				
				state.nextPlayer();
			}
			
			state.finishRound();
			state.nextRound();
		}
	}

	/*
	 * Separates start of round with a line
	 */
	public static void printNewRound()
	{
		for (int i = 0; i < 25; i++) System.out.print("~~~~~~");
		System.out.printf("\n%75s %d, %s %d\n", "Age", state.getAge(), "Round", state.getRound());
		for (int i = 0; i < 25; i++) System.out.print("~~~~~~");
		System.out.println();
	}
	
	public static void printPlayerHand()
	{
		System.out.println("Your hand:");
		
		for (int i = 0; i < 25; i++) System.out.print("="); System.out.println();
		
		ArrayList<Card> playerHand = state.getPlayerHands().get(currentWonder.getHand());
		
		for (int i = 0; i < playerHand.size(); i++)
			System.out.println(i + " " + playerHand.get(i));
		
		for (int i = 0; i < 25; i++) System.out.print("="); System.out.println();
	}
	
	public static void printPlayedCards()
	{
		System.out.println("Your Played Cards:");
		
		for (int i = 0; i < 25; i++) System.out.print("="); System.out.println();//
		
		HashMap<String, HashSet<Card>> tempMap = currentWonder.getCardsPlayed();
		for (String s: tempMap.keySet())
			for (Card c: tempMap.get(s))
				if(!s.equals("wonder"))
				System.out.println(s + ": " + c);
		
		for (int i = 0; i < 25; i++) System.out.print("="); System.out.println();//
	}
	
	public static void printWonderInformation()
	{
		System.out.printf("%s; coin:%d, vp:%d, mp:%d, win:%d; loss:%d\n", currentWonder.getName(), currentWonder.getMoney(), currentWonder.getVictoryPoints(),
				currentWonder.getMilitaryPower(), currentWonder.getWins(), currentWonder.getLosses());
	}
	
	public static void printOneLine()
	{
		System.out.println("\n");
	}
	
	/*
	 * Scanner takes in input for player choice
	 */
	public static void optionSelection()
	{
		String s = "";
		if(state.canUseOlympia())
		{
			s = " 'WonderAbility'(build a structure of your choice or free), ";
		}
		System.out.println("Type 'Play', 'Build' (wonder), 'Burn',"+s+" 'Display' (cards and other info)");
		String option = input.next().toLowerCase();
		switch (option) {
		case "play":
			currentWonder.setAction("Play");
			handSelection();
			break;
		case "build":
			currentWonder.setAction("Build");
			build();
			break;
		case "burn":
			currentWonder.setAction("Burn");
			 burn();
			break;
		case "WonderAbility":
			
			
		case "display":
			 display();
			break;
		}
		
		/*
		 * If the player from the previous options has not made
		 * a valid choice, then they will be prompted to make a
		 * selection between those again in order to progress
		 */
		if (hasQuit)
			optionSelection();
		
	}
	
	/* IMPORTANT: selected card should only be added to played cards if the selected resources are valid
	 * TODO:
	 * trading cards
	 * method might be changed so that it passes in the paramater Card, which is the selected card so that the method can be used for build wonder as well
	 * checking for chain cards
	 * have it remove coins when using coins to play card
	 * 
	 * 
	 * Gets every resource that player has
	 * puts the resources in a hashmap<boolean, string>
	 * player can either select/deselect resource to use, cancel the operation, confirm the operation, or trade with others
	 * checks if resources are valid to play the card
	 * plays the card
	 */
	public static void resourceSelection()
	{
		// Shows every resource needed to build the card, including the coins
		System.out.println("Cost to build " + currentWonder.getSelectedCard().getName() + ": " + currentWonder.getSelectedCard().getCost());
		
		// checks if the card actually costs anything to build
		if (!currentWonder.getSelectedCard().getCost().get(0).equals("null"))
		{
			// every resource that a player has
			String[] allResources = currentWonder.getAllPlayerResources().toArray(new String[0]);
			// resources that the player will be selecting to play
			ArrayList<String> selected = new ArrayList<String>();
			
			int playerInput = -1;
			
			while (true)
			{	
				System.out.println("type 'quit' to quit");
				System.out.println("type 'confirm' to confirm selection");
				System.out.println("Choose index of resource to select/deselect: ");
				
				// prints out the resources needed to play a card
				for (int i = 0; i < allResources.length; i++)
					System.out.println(i + "-" + allResources[i]);
				
				// quits out of the loop and goes back to option selection
				String temp = input.next();
				if (temp.equals("quit"))
				{
					hasQuit = true;
					return;
				}
				hasQuit = false;
				
				// if the player has confirmed their resource selection
				if (temp.equals("confirm"))
				{
					for (int i = 0; i < allResources.length; i++)
						if (allResources[i].contains("-Selected"))
							selected.add(allResources[i].substring(0, allResources[i].indexOf("-Selected")));
					state.setSelectedResources(selected);
					ArrayList<String> needed = new ArrayList<String>(currentWonder.getSelectedCard().getCost());
					
					
					// checks if everything in selected and needed match
					for (int i = 0; i < needed.size(); i++)
					{
						String str = needed.get(i);
						
						for (int j = 0; j < selected.size(); j++)
						{
							if (str.contains(selected.get(j)) || selected.get(j).contains(str))
							{
								needed.remove(i);
								selected.remove(i);
							}
						}
					}
					
					// if it all matches (resources all valid)
					if (needed.size() == 0)
					{
						System.out.println("Played card " + currentWonder.getSelectedCard());
						currentWonder.playCard(currentWonder.getSelectedCard());
						
						// when everything is valid
						ArrayList<Boolean> decision = state.getDecisionMade();
						decision.set(state.getCurrentPlayer(), true);
						state.setDecisionMade(decision);
						break;
					}
					else
						System.out.println("Invalid resources selected");
				}
				else
				{
					// gets player input
					try {
					playerInput = Integer.parseInt(temp);
					} catch (NumberFormatException e) {
						System.out.println("Cannot convert from String to int");
						playerInput = -1;
					}
					
					// checks if card is playable, else it continues the loop
					if (playerInput < 0 && playerInput > allResources.length - 1)
					{
						System.out.println("Index is out of bounds");
						playerInput = -1;
					}
					else
					{
						// if it is selected, then deselect it
						if (allResources[playerInput].contains("-Selected"))
							allResources[playerInput] = allResources[playerInput].substring(0, allResources[playerInput].indexOf("-Selected"));
						else
							allResources[playerInput] = allResources[playerInput] + "-Selected";
					}
				}
				System.out.println();
			}
		}
	}
	
	public static void trade()
	{
		
	}
	
	public static void handSelection()
	{
		int playerInput = -1;
		
		// checks if selection is in bounds and is playable (doesn't check for resources)
		do
		{
			System.out.println("type 'quit' to quit");
			System.out.print("Choose index of card to play: ");
			
			// quits out of the loop and goes back to option selection
			String temp = input.next();
			if (temp.equals("quit"))
			{
				hasQuit = true;
				return;
			}
			hasQuit = false;
			
			try {
			playerInput = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				System.out.println("Cannot convert from String to int");
				playerInput = -1;
			}
			
			// checks if card is playable, else it continues the loop
			if (playerInput >= 0 && playerInput <= currentHand.size() - 1)
				if (!currentWonder.playable(currentHand.get(playerInput)))
					playerInput = -1;
			
			System.out.println();
		}
		
		while (playerInput < 0 || playerInput > currentHand.size() - 1);
		
		// sets the selected card to player input, but doesn't add to cardsPlayed yet
		if (playerInput >= 0 && playerInput <= currentHand.size() - 1)
			currentWonder.setSelectedCard(currentHand.remove(playerInput));
		System.out.println("Card '" + currentWonder.getSelectedCard().getName() + "' chosen");
		
		// goes to resource selection of card, will add to cardsPlayed
		resourceSelection();
	}
	
	/*
	 * TODO
	 * Need a resource selection screen to build wonder
	 */
	public static void build()
	{
		// not done
		
		if(currentWonder.getPlayerWonders() < 3) {
			System.out.println("Choose index of the card you want to build a wonder with");
			Card c = currentHand.remove(input.nextInt());
			//currentWonder.buildWonder();
			System.out.println("You used " + c.getName() + " to build Wonder #"+currentWonder.getPlayerWonders());
			 }
			else {
				System.out.println("You already have all 3 wonders, please choose again");
				optionSelection();
			}
		
		// gets resource and sets decision made to true
		resourceSelection();
	}
	
	public static void burn()
	{
		System.out.println("Choose index of a card to burn");
		Card c = currentHand.remove(input.nextInt());
		//currentWonder.burnCard();
		System.out.println("You burnt " + c.getName() + " and gained 3 coins.\nYour new coin balance: " + currentWonder.getMoney());
		
		// sets decision to true to progress game
		ArrayList<Boolean> decision = state.getDecisionMade();
		decision.set(state.getCurrentPlayer(), true);
		state.setDecisionMade(decision);
	}
	
	public static void display()
	{
		System.out.println("All available resources: " + currentWonder.getAllPlayerResources());
		System.out.println("Number of Wonders built: " + currentWonder.getPlayerWonders());
		System.out.println();
		/* Raymond: I dont think this should go here
		System.out.println("Your neighbor's resources (right): " + state.getRightWonder(currentPlayer).getCardResources());
		System.out.println("Your neighbor's number of Wonders (right): " + state.getRightWonder(currentPlayer).getPlayerWonders());
		System.out.println("Your neighbor's resources (left): " + state.getLeftWonder(currentPlayer).getCardResources());
		System.out.println("Your neighbor's number of Wonders (right): " + state.getLeftWonder(currentPlayer).getPlayerWonders());*/
		optionSelection();
	}
}
