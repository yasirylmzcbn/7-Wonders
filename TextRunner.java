import java.util.HashMap;
import java.util.HashSet;

public class TextRunner
{
	public static GameState state;
	
	public static void main(String[] args)
	{
		state = new GameState();
		
		while (!state.isEndOfGame())
		{
			printNewRound();
			
			printPlayedCards();
			
			printPlayerHand();
			
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
		System.out.println(state.getPlayerHands().get(state.getCurrentPlayer()));
		System.out.println("}");
	}
	
	public static void printPlayedCards()
	{
		System.out.println("Your Played Cards:\n{");
		HashMap<String, HashSet<Card>> tempMap = state.getWonders().get(state.getCurrentPlayer()).getCardsPlayed();
		for (String s: tempMap.keySet())
			for (Card c: tempMap.get(s))
				System.out.println(s + ": " + c);
		System.out.println("}");
	}
}
