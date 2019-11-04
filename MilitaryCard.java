import java.util.*;

public class MilitaryCard extends Card
{
	// military points
	private int militaryPower;
	
	public MilitaryCard(String na, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers, int militaryPoints)
	{
		super(na, col, ch, co, numOfPlayers);
		militaryPower = militaryPoints;
	}
	
	//getter
	public int getMilitaryPower() {
		return militaryPower;
	}
	
	//setter
	public void setMilitaryPower(int militaryPower) {
		this.militaryPower = militaryPower;
	}
}
