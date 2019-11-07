import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameState
{
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
		for (int i = 0; i < numberOfPlayers; i++)
			{} // to do
		
		ArrayList<Card> hands = new ArrayList<Card>();
		//playerHands = new ArrayList<hands>();
	}
	
	public ArrayList<Integer> finalPoints()
	{
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
}
