import java.util.ArrayList;
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
	
	public static void main(String[] args)
	{
		input = new Scanner(System.in);
		state = new GameState();
		while (!state.isEndOfGame())
		{
			currentWonder = state.getWonders().get(state.getCurrentPlayer());
			currentPlayer = state.getCurrentPlayer();
			currentHand = state.getPlayerHands().get(currentPlayer);

			printNewRound();
			System.out.println("ROUND " + state.getRound());
			
			// for each player in one round
			while (!state.allDecisionsMade())
			{
				printDivider();
				currentWonder = state.getWonders().get(state.getCurrentPlayer());
				currentPlayer = state.getCurrentPlayer();
				currentHand = state.getPlayerHands().get(currentPlayer);
				
				printPlayerInfo(currentWonder);
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
			state.setRound(state.getRound() + 1);
		}
	}

	/*
	 * Separates start of round with a line
	 */
	public static void printNewRound()
	{
		for (int i = 0; i < 25; i++) System.out.print("~~~~~~");
		System.out.printf("\n%75s\n", "New Round");
		for (int i = 0; i < 25; i++) System.out.print("~~~~~~");
		System.out.println();
	}
	
	public static void printPlayerHand()
	{
		System.out.println("Your hand:");
		
		for (int i = 0; i < 25; i++) System.out.print("="); System.out.println();
		
		ArrayList<Card> playerHand = state.getPlayerHands().get(state.getCurrentPlayer());
		
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
		System.out.println("Type 'Play', 'Build' (wonder), 'Burn', 'Display' (cards and other info)");
		String input = keyboard.next();
		
		switch (input) {
		case "Play":
			currentWonder.setAction("Play");
			play();
			break;
		case "Build":
			currentWonder.setAction("Build");
			// doThing();
			break;
		case "Burn":
			currentWonder.setAction("Burn");
			// burn();
			break;
		case "display":
			// display();
			break;
		}
		
	}
	
	public static void resourceSelection()
	{
		
	}
	
	public static void play()
	public static void handSelection()
	{
		int playerInput = 0;
		HashSet<Card> cards;
		do {
			System.out.print("Choose index of card to play: ");
			
			try {
			playerInput = input.nextInt();
			cards = currentWonder.getCardsPlayed().get(currentHand.get(playerInput).getColor());
			cards.add(currentHand.get(playerInput));
			} 
			catch (InputMismatchException e) {
				System.out.println("Error: cannot convert from String to int");
			}
			catch(IndexOutOfBoundsException e) {
				if(currentHand.size() == 0)
				System.out.println("There are no cards left in the hand");
				else System.out.println("Woah! That number is too big! Please try again");
			}
			System.out.println();
			
			//System.out.println();
		}
		while (playerInput < 0 || playerInput > currentHand.size() - 1);
		
		// TODO should this operation be manual and inside TextRunner, or should it be a method in gameState?
		if (currentWonder.playable(currentHand.get(playerInput)));
			currentWonder.setSelectedCard(currentHand.remove(playerInput));
		System.out.println("Card \"" + currentWonder.getSelectedCard().getName() + "\" chosen");
		
		// selects resources in order to play card
		resourceSelection();
	}
	
	public static void build()
	{
		
	}
	
	public static void burn()
	{
		
	}
	
	public static void display()
  {
		// TODO should this operation be manual and inside TextRunner?
		currentWonder.setSelectedCard(currentHand.remove(playerInput));
		System.out.println("Chose card " + currentWonder.getSelectedCard().getName());
		
		if(currentHand.size() == 1) {
			endOfRound = true;
			currentWonder.setMoney(currentWonder.getMoney()+3);
		}
	}
}

