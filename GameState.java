import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GameState
{
	private Scanner input; // parses through card files
	private ArrayList<Card> graveyard; // stores all cards discarded
	HashMap<Integer, ArrayList<Card>> deck; // Integer=age, ArrayList=Cards themselves
	private int age; // current age
	
}
