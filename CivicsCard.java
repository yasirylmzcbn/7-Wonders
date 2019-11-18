import java.util.ArrayList;

public class CivicsCard extends Card
{
	private int victoryPoints;
	
	//Constructor
	public CivicsCard(String na, String col, ArrayList<String> ch, ArrayList<String> co, int numOfPlayers,int victoryPoints) {
		super(na, col, ch, co, numOfPlayers);
		this.victoryPoints=victoryPoints;
	}
	
	//getters and setters
	public int getVictoryPoints() {
		return victoryPoints;
	}
	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public String toString()
	{
		//return super.toString() + String.format("VP: %-5s", getVictoryPoints());
		return super.toString() + "; VPoints: " + getVictoryPoints();
	}
}
