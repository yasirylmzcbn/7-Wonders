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
			
			printWonderInformation();
			printOneLine();
			
			printPlayedCards();
			printOneLine();
			
			printPlayerHand();
			printOneLine();
			
			handSelection();
			printOneLine();
			
			break;
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
	 * TODO should wonder be chosen like a card?
	 */
	public static void optionSelection()
	{
		System.out.println("Type 'play' to play a card, 'build' to build a wonder, 'burn' to burn a card, 'display' to display your played cards, 'displayleft' for left wonder, or 'displayright for rightwonder");
		
	}
	
	public static void handSelection()
	{
		int playerInput = 0;
		
		do {
			System.out.print("Choose index of card to play: ");
			
			try {
			playerInput = input.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("Error: cannot convert from String to int");
				break;
			}
			System.out.println();
			
			//System.out.println();
		}
		while (playerInput < 0 || playerInput > currentHand.size() - 1);
		
		// TODO should this operation be manual and inside TextRunner, or should it be a method in gameState?
		currentWonder.setSelectedCard(currentHand.remove(playerInput));
		System.out.println("Card \"" + currentWonder.getSelectedCard().getName() + "\" chosen");
		// TODO setAction? formatting action
	}
}







