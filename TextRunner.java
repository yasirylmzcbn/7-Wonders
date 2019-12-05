import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
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
	public static boolean OlympiaAbility = false;
	
	public static void main(String[] args)
	{
		//wonder distribution maybe
		input = new Scanner(System.in);
		state = new GameState();
		wonderDist();
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
	
				printNextPlayer();
				
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
			if(state.isHalic())
			{
				currentWonder = state.getWonders().get(state.getCurrentPlayer());
				currentPlayer = state.getCurrentPlayer();
				currentHand = state.getPlayerHands().get(currentWonder.getHand());
				optionSelection();	
				printOneLine();	
				state.finishRound();
			}
			state.nextRound();
		}
		System.out.println(state.whoWon());
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
	
	public static void printNextPlayer()
	{
		for (int i = 0; i < 25; i++)
			System.out.print("<>");
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
		System.out.printf("%s\n coin:%d, vp:%d, mp:%d, win:%d; loss:%d\n", currentWonder.getName(), currentWonder.getMoney(), currentWonder.getVictoryPoints(),
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
		
		while (true)
		{
			System.out.println("Type 'Play', 'Build' (wonder), 'Burn',"+s+" 'Display' (cards and other info)");
			String option = input.next().toLowerCase();
			
			if (option.equals("play"))
			{
				currentWonder.setAction("Play");
				handSelection();
				break;
			}
			else if (option.equals("build"))
			{
				currentWonder.setAction("Build");
				build();
				break;
			}
			else if (option.equals("burn"))
			{
				currentWonder.setAction("Burn");
				burn();
				break;
			}
			else if (option.equals("WonderAbility"))
			{
				currentWonder.setAction("Play");
				OlympiaAbility = true;
				handSelection();
				break;
			}
			else if (option.equals("display"))
			{
				display();
				break;
			}
		}
		
		/*
		 * If the player from the previous options has not made
		 * a valid choice, then they will be prompted to make a
		 * selection between those again in order to progress
		 */
		if (hasQuit)
			optionSelection();
		
	}
	
	/* IMPORTANT: card should only be added to selected cards if the selected resources are valid
	 * TODO:
	 * !!!! trading 'cards' does not work <- we dont trade cards and trading resources does
	 * method might be changed so that it passes in the parameter Card, which is the selected card so that the method can be used for build wonder as well
	 * ^dont
	 * checking for chain cards <----NEED THIS: i made a inChain() for this
	 * have it remove coins when using coins to play card <--NEED THIS: I did this in finishRound()
	 * 
	 * 
	 * Gets every resource that player has
	 * puts the resources in a hashmap<boolean, string>
	 * player can either select/deselect resource to use, cancel the operation, confirm the operation, or trade with others
	 * checks if resources are valid to play the card
	 * selects the card
	 */
	public static void resourceSelection()
	{
		int startingMoney = currentWonder.getMoney();
		int rightStartingMoney = state.getRightWonder(currentWonder).getMoney();
		int leftStartingMoney = state.getLeftWonder(currentWonder).getMoney();
		// Shows every resource needed to build the card, including the coins
		System.out.println("Cost to build " + currentWonder.getSelectedCard().getName() + ": " + currentWonder.getSelectedCard().getCost());
		
		// checks if the card actually costs anything to build
		if (!currentWonder.getSelectedCard().getCost().get(0).equals("null")&&!OlympiaAbility&&!currentWonder.inChain(currentWonder.getSelectedCard()))
		{
			int totalCost = 0;
			// every resource that a player has
			String[] allResources = currentWonder.getAllPlayerResources().toArray(new String[0]);
			String[] leftNR = state.getLeftWonder(currentWonder).getCardResources().toArray(new String[0]); //Left neighbor's resources
			String[] rightNR = state.getRightWonder(currentWonder).getCardResources().toArray(new String[0]);//right 
			// resources that the player will be selecting to play
			ArrayList<String> selected = new ArrayList<String>();
			
			int playerInput = -1;
			
			while (true)
			{	
				System.out.println("type 'quit' to quit");
				System.out.println("type 'confirm' to confirm selection");
				System.out.println("Choose index of resource to select/deselect: ");
				System.out.println("Total cost is: " + totalCost + " coins");
				// prints out the resources needed to play a card
				System.out.println("My Resources: ");
				for (int i = 0; i < allResources.length; i++)
					System.out.println(i + "-" + allResources[i]);
				
				int SilverCost = 2; // cost of trading for a silver card resource
				int RightBrownCost = 2;// cost for trading for a brown card resource to the right
				int LeftBrownCost = 2;//^*left
				

				String brownR = "wood-Selected stone-Selected clay-Selected ore-Selected";
                String silverR = "paper-Selected cloth-Selected glass-Selected";
				
				ArrayList<Card> crds = new ArrayList<Card>();
				crds.addAll(currentWonder.getCardsPlayed().get("yellow"));
				for(int i = 0; i<crds.size(); i++ )
				{
					if(crds.get(i).getName().equals("East Trading Post"))
					{
						RightBrownCost = 1;
					}
					if(crds.get(i).getName().equals("West Trading Post"))
					{
						LeftBrownCost = 1;
					}
					if(crds.get(i).getName().equals("Marketplace"))
					{
						SilverCost = 1;
					}
				}
				
				System.out.println(state.getLeftWonder(currentWonder).getName()+"'s Resources: ");
				
				for (int i = 0; i < leftNR.length; i++)
				{
					if(brownR.contains(leftNR[i].split("\\|\\|")[0]))
						System.out.println("L"+i + "-" + leftNR[i]+"\tCost: "+LeftBrownCost+" coin");
					if(silverR.contains(leftNR[i].split("\\|\\|")[0]))
						System.out.println("L"+i + "-" + leftNR[i]+"\tCost: "+SilverCost+" coin");
				}
				
				System.out.println(state.getRightWonder(currentWonder).getName()+"'s Resources: ");
				for (int i = 0; i < rightNR.length; i++)
				{
					if(brownR.contains(rightNR[i].split("\\|\\|")[0]))
						System.out.println("R"+i + "-" + rightNR[i]+"\tCost: "+RightBrownCost+" coin");
					if(silverR.contains(rightNR[i].split("\\|\\|")[0]))
						System.out.println("R"+i + "-" + rightNR[i]+"\tCost: "+SilverCost+" coin");
				}
				
				// quits out of the loop and goes back to option selection
				String temp = input.next();
				if (temp.equals("quit"))
				{
					currentWonder.setMoney(startingMoney);
					state.getRightWonder(currentWonder).setMoney(rightStartingMoney);
					state.getLeftWonder(currentWonder).setMoney(leftStartingMoney);
					hasQuit = true;
					return;
				}
				hasQuit = false;
				
				// if the player has confirmed their resource selection
				if (temp.equals("confirm"))
				{
					/*
					for (int i = 0; i < allResources.length; i++)
						if (allResources[i].contains("-Selected"))
							selected.add(allResources[i].substring(0, allResources[i].indexOf("-Selected")));
					
					for (int i = 0; i < leftNR.length; i++)
						if (leftNR[i].contains("-Selected"))
							selected.add(leftNR[i].substring(0, leftNR[i].indexOf("-Selected")));
					
					for (int i = 0; i < rightNR.length; i++)
						if (rightNR[i].contains("-Selected"))
							selected.add(rightNR[i].substring(0, rightNR[i].indexOf("-Selected")));
					*/
					
					
					// // selection for || cards is going here temporarily
					for (int i = 0; i < selected.size(); i++)
					{
						// need to do "\\|\\|" since | is a special char, or else it will not read
						if (selected.get(i).contains("||"))
						{
							String[] resources = selected.get(i).split("\\|\\|");
							System.out.print("Choose either one from :" + Arrays.toString(resources));
							selected.set(i, input.next());
						}
					}
					
					state.setSelectedResources(selected);
					ArrayList<String> needed = new ArrayList<String>(currentWonder.getSelectedCard().getCost());
					
					/*// checks if everything in selected and needed match
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
						System.out.println("Played card " + currentWonder.getSelectedCard().getName());
						//currentWonder.playCard(currentWonder.getSelectedCard());
						
						// when everything is valid
						ArrayList<Boolean> decision = state.getDecisionMade();
						decision.set(state.getCurrentPlayer(), true);
						state.setDecisionMade(decision);
						break;
					}
					else
						System.out.println("Invalid resources selected");
						*/
					//Raymond's version of the code above
					Collections.sort(needed);
					Collections.sort(selected);
					
					if(currentWonder.getMoney() < totalCost) {
						System.out.println("You don't have money");
						break;
					}
					if(currentWonder.getMoney() >= 1)
					//System.out.println(""+needed+selected);
					if(needed.equals(selected)) // might need to add &&currentWonder.playable(currentWonder.getSelectedCard())
						//but I think Mustafa's way of checking when they select the card also works
					{

						System.out.println("Played card " + currentWonder.getSelectedCard().getName());
						//currentWonder.playCard(currentWonder.getSelectedCard());
						//System.out.println(currentWonder.getAllPlayerResources());
						
						for (int i = 0; i < currentHand.size(); i++)
							if (currentHand.get(i).getName().equals(currentWonder.getSelectedCard().getName()))
								currentHand.remove(i);
						
						
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
					String who = "";
					// gets player input
					try
					{
						if(temp.contains("R")||temp.contains("L"))
						{
							who = temp.substring(0, 1);
							temp = temp.substring(1);
						}
						playerInput = Integer.parseInt(temp);
					}
					catch (NumberFormatException e)
					{
						System.out.println("Cannot convert from String to int");
						playerInput = -1;
					}
					
					// if choosing their own resource
					if(who.equals(""))
					{
						// checks if resources is in the array
						if (playerInput < 0 || playerInput > allResources.length - 1)
						{
							System.out.println("Index is out of bounds");
							playerInput = -1;
						}
						else
						{
							// if it is selected, then deselect it
							if (allResources[playerInput].contains("-Selected"))
							{
								allResources[playerInput] = allResources[playerInput].substring(0, allResources[playerInput].indexOf("-Selected"));
								selected.remove(allResources[playerInput]);
							}
							else
							{
								selected.add(allResources[playerInput]);
								allResources[playerInput] = allResources[playerInput] + "-Selected";
							}
						}
					}
					else if(who.equals("L"))
					{
						if (playerInput < 0 || playerInput > leftNR.length - 1)
						{
							System.out.println("Index is out of bounds");
							playerInput = -1;
						}
						else
						{
							// if it is selected, then deselect it
							if (leftNR[playerInput].contains("-Selected"))
							{
								leftNR[playerInput] = leftNR[playerInput].substring(0, leftNR[playerInput].indexOf("-Selected"));
								selected.remove(leftNR[playerInput]);
								
								if(brownR.contains(leftNR[playerInput]))
									totalCost -= LeftBrownCost;
								else totalCost -= SilverCost;
								/*
								ArrayList<String> trades = currentWonder.getTrades();
								String leftName = state.getLeftWonder(currentWonder).getName();
								for(int i = 0 ; i<trades.size(); i++)
								{
									if(trades.get(i).contains(leftName))
									{
										int t = Integer.parseInt(trades.get(i).substring(leftName.length()+1));
										if(brownR.contains(leftNR[playerInput]))
										{
											t-=LeftBrownCost;
											trades.set(i, trades.get(i).substring(0, leftName.length())+t);
										}
										if(silverR.contains(leftNR[playerInput]))
										{
											t-=SilverCost;
											trades.set(i, trades.get(i).substring(0, leftName.length())+t);
										}
									}
								}*/
								TreeMap<String, Integer> trades = currentWonder.getTrades();
								String leftName = state.getLeftWonder(currentWonder).getName();
								int t = trades.get(leftName);
								if(brownR.contains(leftNR[playerInput]))
								{
									t-=LeftBrownCost;
								}
								if(silverR.contains(leftNR[playerInput]))
								{
									t-=SilverCost;
								}
								trades.put(leftName,t);
							}
							else
							{
								selected.add(leftNR[playerInput]);
								if(brownR.contains(leftNR[playerInput]))
									totalCost += LeftBrownCost;
								else totalCost += SilverCost;
									
								leftNR[playerInput] = leftNR[playerInput] + "-Selected";
								/*
								ArrayList<String> trades = currentWonder.getTrades();
								String leftName = state.getLeftWonder(currentWonder).getName();
								
								for(int i = 0 ; i<trades.size(); i++)
								{
									if(trades.get(i).contains(leftName))
									{
										int t = Integer.parseInt(trades.get(i).substring(leftName.length()+1));
										if(brownR.contains(leftNR[playerInput]))
										{
											t+=LeftBrownCost;
											trades.set(i, trades.get(i).substring(0, leftName.length())+t);
										}
										if(silverR.contains(leftNR[playerInput]))
										{
											t+=SilverCost;
											trades.set(i, trades.get(i).substring(0, leftName.length())+t);
										}
									}
								}
								*/
								TreeMap<String, Integer> trades = currentWonder.getTrades();
								String leftName = state.getLeftWonder(currentWonder).getName();
								int t = trades.get(leftName);
								if(brownR.contains(leftNR[playerInput]))
								{
									t+=LeftBrownCost;
								}
								if(silverR.contains(leftNR[playerInput]))
								{
									t+=SilverCost;
								}
								trades.put(leftName, t);
							}
						}
					}
					else if(who.equals("R"))
					{
						if (playerInput < 0 || playerInput > rightNR.length - 1)
						{
							System.out.println("Index is out of bounds");
							playerInput = -1;
						}
						else
						{
							
							if (rightNR[playerInput].contains("-Selected"))
							{
								rightNR[playerInput] = rightNR[playerInput].substring(0, rightNR[playerInput].indexOf("-Selected"));
								selected.remove(rightNR[playerInput]);
								if(brownR.contains(rightNR[playerInput]))
									totalCost -= RightBrownCost;
								else totalCost -= SilverCost;
								TreeMap<String, Integer> trades = currentWonder.getTrades();
								String rightName = state.getRightWonder(currentWonder).getName();
								int t = trades.get(rightName);
								if(brownR.contains(rightNR[playerInput]))
								{
									t-=RightBrownCost;
								}
								if(silverR.contains(rightNR[playerInput]))
								{
									t-=SilverCost;
								}
								trades.put(rightName,t);
							}
							else
							{
								selected.add(rightNR[playerInput]);
								if(brownR.contains(rightNR[playerInput]))
									totalCost += RightBrownCost;
								else totalCost += SilverCost;
								
								rightNR[playerInput] = rightNR[playerInput] + "-Selected";
								TreeMap<String, Integer> trades = currentWonder.getTrades();
								String rightName = state.getRightWonder(currentWonder).getName();
								int t = trades.get(rightName);
								if(brownR.contains(rightNR[playerInput]))
								{
									t+=RightBrownCost;
								}
								if(silverR.contains(rightNR[playerInput]))
								{
									t+=SilverCost;
								}
								trades.put(rightName, t);
							}
						
					
						}
				
						System.out.println();
					}
				}
			}
		}
		else
		{
			System.out.println("Played card " + currentWonder.getSelectedCard().getName());

			for (int i = 0; i < currentHand.size(); i++)
				if (currentHand.get(i).getName().equals(currentWonder.getSelectedCard().getName()))
					currentHand.remove(i);
			//currentWonder.playCard(currentWonder.getSelectedCard());
			
			// when everything is valid
			ArrayList<Boolean> decision = state.getDecisionMade();
			decision.set(state.getCurrentPlayer(), true);
			state.setDecisionMade(decision);
		}
	}

	
	public static void handSelection()
	{
		int playerInput = -1;
		
		// checks if selection is in bounds and is playable (doesn't check for resources)
		do
		{
			printOneLine();
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
				if (!currentWonder.playable(currentHand.get(playerInput)))//
					playerInput = -1;
			
			System.out.println();
		}
		
		while (playerInput < 0 || playerInput > currentHand.size() - 1);
		
		// sets the selected card to player input, but doesn't add to cardsPlayed yet
		if (playerInput >= 0 && playerInput <= currentHand.size() - 1)
			currentWonder.setSelectedCard(currentHand.get(playerInput));
		System.out.println("Card '" + currentWonder.getSelectedCard().getName() + "' chosen");
		
		// goes to resource selection of card, will add to cardsPlayed <-- SHOULDNT ADD TO CARDSPLAYED - Raymond
		resourceSelection();
	}
	
	public static void build()
	{
		if(currentWonder.getPlayerWonders() < 3)
		{
			int playerInput = -1;
			do
			{
				System.out.println("Type 'quit' to quit");
				System.out.println("Choose card to build wonder with: ");
				
				// quits out of loop, goes back to option selection
				String temp = input.next();
				if (temp.equals("quit"))
				{
					hasQuit = true;
					return;
				}
				hasQuit = false;
				
				// checks if input is a number
				try
				{
					playerInput = Integer.parseInt(temp);
				}
				catch (NumberFormatException e)
				{
					System.out.println("Cannot convert from String to int");
					playerInput = -1;
				}
				
				// checks for out of bounds

				if (playerInput < 0 || playerInput > currentHand.size() - 1)

				{
					System.out.println("Index is out of bounds!");
					playerInput = -1;
				}
			}
			while (playerInput < 0 || playerInput > currentHand.size() - 1);
			
			// removes card since buildWonder() does not take in a card for input to burn
			Card c = currentHand.get(playerInput);
			currentWonder.setSelectedCard(c);
			// System.out.println("You used " + c.getName() + " to build Wonder #"+currentWonder.getPlayerWonders());
		}
		else
		{
			System.out.println("You already have all 3 wonders, please choose again");
			optionSelection();
		}
		
		// gets resource and sets decision made to true
		wonderResourceSelection();
	}
	public static void wonderResourceSelection() // TODO CHECK THIS, im doing this super late and my brain is fried rn
	{
			System.out.println("Cost to build wonder "+(currentWonder.getPlayerWonders()+1)+": " + currentWonder.nextWonder().getCost());
			//All Wonders cost something
				// every resource that a player has
				String[] allResources = currentWonder.getAllPlayerResources().toArray(new String[0]);
				String[] leftNR = state.getLeftWonder(currentWonder).getCardResources().toArray(new String[0]); //Left neighbor's resources
				String[] rightNR = state.getRightWonder(currentWonder).getCardResources().toArray(new String[0]);//right 
				// resources that the player will be selecting to play
				ArrayList<String> selected = new ArrayList<String>();
				
				int playerInput = -1;
				
				while (true)
				{	
					System.out.println("type 'quit' to quit");
					System.out.println("type 'confirm' to confirm selection");
					System.out.println("Choose index of resource to select/deselect: ");
					
					// prints out the resources needed to play a card
					System.out.println("My Resources: ");
					for (int i = 0; i < allResources.length; i++)
						System.out.println(i + "-" + allResources[i]);
					
					int SilverCost = 2; // cost of trading for a silver card resource
					int RightBrownCost = 2;// cost for trading for a brown card resource to the right
					int LeftBrownCost = 2;//^*left
					
					String brownR = "wood-Selected stone-Selected clay-Selected ore-Selected";
                    String silverR = "paper-Selected cloth-Selected glass-Selected";
					
					
					ArrayList<Card> crds = new ArrayList<Card>();
					crds.addAll(currentWonder.getCardsPlayed().get("yellow"));
					for(int i = 0; i<crds.size(); i++ )
					{
						if(crds.get(i).getName().equals("East Trading Post"))
						{
							RightBrownCost = 1;
						}
						if(crds.get(i).getName().equals("West Trading Post"))
						{
							LeftBrownCost = 1;
						}
						if(crds.get(i).getName().equals("Marketplace"))
						{
							SilverCost = 1;
						}
					}
					
					System.out.println(state.getLeftWonder(currentWonder).getName()+"'s Resources: ");
					
					for (int i = 0; i < leftNR.length; i++)
					{
						if(brownR.contains(leftNR[i].split("\\|\\|")[0]))
							System.out.println("L"+i + "-" + leftNR[i]+"\tCost: "+LeftBrownCost+" gold");
						if(silverR.contains(leftNR[i].split("\\|\\|")[0]))
							System.out.println("L"+i + "-" + leftNR[i]+"\tCost: "+SilverCost+" gold");
					}
					
					System.out.println(state.getRightWonder(currentWonder).getName()+"'s Resources: ");
					for (int i = 0; i < rightNR.length; i++)
					{
						if(brownR.contains(rightNR[i].split("\\|\\|")[0]))
							System.out.println("R"+i + "-" + rightNR[i]+"\tCost: "+RightBrownCost+" gold");
						if(silverR.contains(rightNR[i].split("\\|\\|")[0]))
							System.out.println("R"+i + "-" + leftNR[i]+"\tCost: "+SilverCost+" gold");
					}
					
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
						/*
						for (int i = 0; i < allResources.length; i++)
							if (allResources[i].contains("-Selected"))
								selected.add(allResources[i].substring(0, allResources[i].indexOf("-Selected")));
						
						for (int i = 0; i < leftNR.length; i++)
							if (leftNR[i].contains("-Selected"))
								selected.add(leftNR[i].substring(0, leftNR[i].indexOf("-Selected")));
						
						for (int i = 0; i < rightNR.length; i++)
							if (rightNR[i].contains("-Selected"))
								selected.add(rightNR[i].substring(0, rightNR[i].indexOf("-Selected")));
								*/
						
						// // selection for || cards is going here temporarily
						for (int i = 0; i < selected.size(); i++)
						{
							// need to do "\\|\\|" since | is a special char, or else it will not read
							if (selected.get(i).contains("||"))
							{
								String[] resources = selected.get(i).split("\\|\\|");
								System.out.print("Choose either one from :" + Arrays.toString(resources));
								selected.set(i, input.next());
							}
						}
						
						state.setSelectedResources(selected);
						ArrayList<String> needed = new ArrayList<String>(currentWonder.nextWonder().getCost());

						Collections.sort(needed);
						Collections.sort(selected);
						if(needed.equals(selected)) 
						{

							System.out.println("Used card " + currentWonder.getSelectedCard().getName() + " to build wonder " + (currentWonder.getPlayerWonders() + 1));
							//currentWonder.playCard(currentWonder.getSelectedCard());
							
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
						String who = "";
						// gets player input
						try {
						if(temp.contains("R")||temp.contains("L"))
						{
							who = temp.substring(0, 1);
							temp = temp.substring(1);
						}
						playerInput = Integer.parseInt(temp);
						} catch (NumberFormatException e) {
							System.out.println("Cannot convert from String to int");
							playerInput = -1;
						}
						

						if(who.equals(""))
						{
							// checks if resources is in the array
							if (playerInput < 0 || playerInput > allResources.length - 1)
							{
								System.out.println("Index is out of bounds");
								playerInput = -1;
							}
							else
							{
								// if it is selected, then deselect it
								if (allResources[playerInput].contains("-Selected"))
								{
									allResources[playerInput] = allResources[playerInput].substring(0, allResources[playerInput].indexOf("-Selected"));
									selected.remove(allResources[playerInput]);
								}
								else
								{
									selected.add(allResources[playerInput]);
									allResources[playerInput] = allResources[playerInput] + "-Selected";
								}
							}
						}
						else if(who.equals("L"))
						{
							if (playerInput < 0 || playerInput > leftNR.length - 1)
							{
								System.out.println("Index is out of bounds");
								playerInput = -1;
							}
							else
							{
								// if it is selected, then deselect it
								if (leftNR[playerInput].contains("-Selected"))
								{
									leftNR[playerInput] = leftNR[playerInput].substring(0, leftNR[playerInput].indexOf("-Selected"));
									selected.remove(leftNR[playerInput]);
									TreeMap<String, Integer> trades = currentWonder.getTrades();
									String leftName = state.getLeftWonder(currentWonder).getName();
									int t = trades.get(leftName);
									if(brownR.contains(leftNR[playerInput]))
									{
										t-=LeftBrownCost;
									}
									if(silverR.contains(leftNR[playerInput]))
									{
										t-=SilverCost;
									}
									trades.put(leftName,t);
								}
								else
								{
									selected.add(leftNR[playerInput]);
									leftNR[playerInput] = leftNR[playerInput] + "-Selected";
									TreeMap<String, Integer> trades = currentWonder.getTrades();
									String leftName = state.getLeftWonder(currentWonder).getName();
									int t = trades.get(leftName);
									if(brownR.contains(leftNR[playerInput]))
									{
										t+=LeftBrownCost;
									}
									if(silverR.contains(leftNR[playerInput]))
									{
										t+=SilverCost;
									}
									trades.put(leftName, t);
								}
							}
						}
						else if(who.equals("R"))
						{
							if (playerInput < 0 || playerInput > rightNR.length - 1)
							{
								System.out.println("Index is out of bounds");
								playerInput = -1;
							}
							else
							{
								
								if (rightNR[playerInput].contains("-Selected"))
								{
									rightNR[playerInput] = rightNR[playerInput].substring(0, rightNR[playerInput].indexOf("-Selected"));
									selected.remove(rightNR[playerInput]);
									TreeMap<String, Integer> trades = currentWonder.getTrades();
									String rightName = state.getRightWonder(currentWonder).getName();
									int t = trades.get(rightName);
									if(brownR.contains(rightNR[playerInput]))
									{
										t-=RightBrownCost;
									}
									if(silverR.contains(rightNR[playerInput]))
									{
										t-=SilverCost;
									}
									trades.put(rightName,t);
								}
								else
								{
									selected.add(rightNR[playerInput]);
									rightNR[playerInput] = rightNR[playerInput] + "-Selected";
									TreeMap<String, Integer> trades = currentWonder.getTrades();
									String rightName = state.getRightWonder(currentWonder).getName();
									int t = trades.get(rightName);
									if(brownR.contains(rightNR[playerInput]))
									{
										t+=RightBrownCost;
									}
									if(silverR.contains(rightNR[playerInput]))
									{
										t+=SilverCost;
									}
									trades.put(rightName, t);
								}
							
						
							}
					
							System.out.println();
						}
					}
		}
	}
	
	
	public static void burn()
	{
		int playerInput = -1;
		do
		{
			System.out.println("Type 'quit' to quit");
			System.out.println("Choose index of card to burn: ");
			
			// quits out of loop, goes back to option selection
			String temp = input.next();
			if (temp.equals("quit"))
			{
				hasQuit = true;
				return;
			}
			hasQuit = false;
			
			// checks if input is a number
			try
			{
				playerInput = Integer.parseInt(temp);
			}
			catch (NumberFormatException e)
			{
				System.out.println("Cannot convert from String to int");
				playerInput = -1;
			}
			
			// checks for out of bounds
			if (playerInput <0 || playerInput > currentHand.size() - 1) 
			{
				System.out.println("Index is out of bounds!");
				playerInput = -1;
			}
		}
		while (playerInput < 0 || playerInput > currentHand.size() - 1);
		
		Card c = currentHand.remove(playerInput);
		System.out.println("Chose card " + c.getName() + " to burn");
		state.getGraveyard().add(c);
		
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
	public static void wonderDist()
	{
		System.out.println("************************* Wonder Distribution ************************");
		for(Wonder w: state.getWonders())
		{
		System.out.printf("%s\n coin:%d, vp:%d, mp:%d, win:%d; loss:%d\n", w.getName(), w.getMoney(), w.getVictoryPoints(),
				w.getMilitaryPower(), w.getWins(), w.getLosses());
		}
		System.out.println("**********************************************************************");
		printOneLine();
	}
}
