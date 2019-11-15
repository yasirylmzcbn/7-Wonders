import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
			
			printPlayedCards();
			
			printPlayerHand();
			
			handSelection();
			
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
		System.out.println("Your hand:\n{");
		ArrayList<Card> playerHand = state.getPlayerHands().get(state.getCurrentPlayer());
		for (int i = 0; i < playerHand.size(); i++)
			System.out.println(i + " " + playerHand.get(i));
		System.out.println("}");
	}
	
	public static void printPlayedCards()
	{
		System.out.println("Your Played Cards:\n{");
		HashMap<String, HashSet<Card>> tempMap = currentWonder.getCardsPlayed();
		for (String s: tempMap.keySet())
			for (Card c: tempMap.get(s))
				System.out.println(s + ": " + c);
		System.out.println("}");
	}
	
	/*
	 * Scanner takes in input for player choice
	 */
	private static void handSelection()
	{
		System.out.print("Choose index of card to play: ");
		int playerInput;
		do
		{
			playerInput = input.nextInt();
		}
		while (playerInput >= 0 && playerInput <= currentHand.size());
			
		System.out.println("help this isn\'t working");
		System.out.println("chose " + state.getPlayerHands().get(currentPlayer).get(playerInput));
	}
}
