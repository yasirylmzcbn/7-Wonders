import java.util.*;

public class Wonder {
	// TODO should there be a resource ArrayList that holds every resource a wonder has?
	private String name;
	private HashMap<String, HashSet<Card>> cardsPlayed; //String = type/color of card, HashSet<Card> = cards 
	private int numOfPlayers;
	private int playerWonders;
	private int money;
	private int victoryPoints;
	private ListIterator<Card> iter;
	private int hand;
	private int militaryPower;
	private Card selectedCard;
	private String action;
	//private ArrayList<String> trades;
	private TreeMap<String, Integer> trades;
	private int losses;
	private int wins;
	private ArrayList<Card> stages=new ArrayList<Card>();
	private String wonderAbility;
	private HashMap<String, Integer> techCardPoints;

	
	public Wonder(String n, int i) {
		name = n;
		numOfPlayers = i;
		playerWonders = 0;
		money = 3;
		victoryPoints = 0;	
		militaryPower = 0;
		action = "";
		trades = new TreeMap<String, Integer>();
		losses = wins = 0;
		cardsPlayed = new HashMap<String, HashSet<Card>>();

		cardsPlayed.put("brown",new HashSet<Card>());
		cardsPlayed.put("silver",new HashSet<Card>());
		cardsPlayed.put("red",new HashSet<Card>());
		cardsPlayed.put("blue",new HashSet<Card>());
		cardsPlayed.put("green",new HashSet<Card>());
		cardsPlayed.put("yellow",new HashSet<Card>());
		cardsPlayed.put("purple",new HashSet<Card>());
		cardsPlayed.put("wonder",new HashSet<Card>());// initial resources, stages, construction markers

		techCardPoints = new HashMap<String, Integer>();
		techCardPoints.put("tablet", 0);
		techCardPoints.put("gear", 0);
		techCardPoints.put("compass", 0);

		//hard coded wonders
    if(name.equals("The Colossus of Rhodes")) {
			ArrayList<String> cor = new ArrayList<String>();
			cor.add("ore");
			cardsPlayed.get("wonder").add(new ResourceCard("The Colossus of Rhodes","brown",null,null,3,cor));

			//wonder 1
			ArrayList<String> cor1Cost = new ArrayList<String>();
			cor1Cost.add("wood");
			cor1Cost.add("wood");
			stages.add(new CivicsCard("cor1","wonder",cor1Cost,null,3,3));
			//wonder 2
			ArrayList<String> cor2Cost = new ArrayList<String>();
			for(int k=0;k<3;k++)
				cor2Cost.add("clay");
			stages.add(new MilitaryCard("cor2","wonder",cor2Cost,null,3,2));
			//wonder 3
			ArrayList<String> cor3Cost = new ArrayList<String>();
			for(int k=0;k<4;k++)
				cor3Cost.add("ore");
			stages.add(new CivicsCard("cor3","wonder",cor3Cost,null,3,7));
		}
		else if(name.contentEquals("The Lighthouse of Alexandria")) {
			ArrayList<String> loa = new ArrayList<String>();
			loa.add("glass");
			
			cardsPlayed.get("wonder").add(new ResourceCard("The Lighthouse of Alexandria","silver",null,null,3,loa));
      
			//wonder 1
			ArrayList<String> loa1Cost = new ArrayList<String>();
			loa1Cost.add("stone");
			loa1Cost.add("stone");
			stages.add(new CivicsCard("loa1","wonder",loa1Cost,null,3,3));
			//wonder 2
			ArrayList<String> loa2Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				loa2Cost.add("ore");
			ArrayList<String> loa2 = new ArrayList<String>();
			loa2.add("clay||ore||wood||stone");
			stages.add(new ResourceCard("loa2","wonder",loa2Cost,null,3,loa2));
			//wonder 3
			ArrayList<String> loa3Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				loa3Cost.add("glass");
			stages.add(new CivicsCard("loa3","wonder",loa3Cost,null,3,7));
		}
		else if(name.contentEquals("The Temple of Artemis in Ephesus")) {

			ArrayList<String> toaie = new ArrayList<String>();
			toaie.add("paper");
      
			cardsPlayed.get("wonder").add(new ResourceCard("The Temple of Artemis in Ephesus","brown",null,null,3,toaie));
      
			//wonder 1
			ArrayList<String> toaie1Cost = new ArrayList<String>();
			toaie1Cost.add("stone");
			toaie1Cost.add("stone");
			stages.add(new CivicsCard("toaie1","wonder",toaie1Cost,null,3,3));
			//wonder 2
			ArrayList<String> toaie2Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				toaie2Cost.add("wood");
			ArrayList<String> toaie2Resource = new ArrayList<String>();
			for(int k=0;k<9;k++)
				toaie2Resource.add("coin");
			stages.add(new ResourceCard("toaie2","wonder",toaie2Cost,null,3,toaie2Resource));
			//wonder 3
			ArrayList<String> toaie3Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				toaie3Cost.add("paper");
			stages.add(new CivicsCard("toaie3","wonder",toaie3Cost,null,3,7));
			}
		else if(name.equals("The Hanging Gardens of Babylon")) {
			ArrayList<String> hgob = new ArrayList<String>();
			hgob.add("clay");
			
			cardsPlayed.get("wonder").add(new ResourceCard("The Hanging Gardens of Babylon","brown",null,null,3,hgob));

			//wonder 1
			ArrayList<String> hgob1Cost = new ArrayList<String>();
			hgob1Cost.add("clay");
			hgob1Cost.add("clay");
			stages.add(new CivicsCard("hgob1","wonder",hgob1Cost,null,3,3));
			//wonder 2
			ArrayList<String> hgob2Cost = new ArrayList<String>();
			for(int k=0;k<3;k++)
				hgob2Cost.add("wood");
			stages.add(new TechCard("hgob2","wonder",hgob2Cost,null,3,"compass||gear||tablet"));
			//wonder 3
			ArrayList<String> hgob3Cost = new ArrayList<String>();
			for(int k=0;k<4;k++)
				hgob3Cost.add("clay");
			stages.add(new CivicsCard("hgob3","wonder",hgob3Cost,null,3,7));
	}
		else if(name.equals("The Statue of Zeus in Olympia")) {
			ArrayList<String> sofzio = new ArrayList<String>();
			sofzio.add("wood");
			
			cardsPlayed.get("wonder").add(new ResourceCard("The Statue of Zeus in Olympia","brown",null,null,3,sofzio));

			//wonder 1
			ArrayList<String> sofzio1Cost = new ArrayList<String>();
			sofzio1Cost.add("wood");
			sofzio1Cost.add("wood");
			stages.add(new CivicsCard("sofzio1","wonder",sofzio1Cost,null,3,3));
			//wonder 2
			ArrayList<String> sofzio2Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				sofzio2Cost.add("stone");
			stages.add(new GuildCard("sofzio2","wonder",sofzio2Cost,null,3,"free*Age"));
			//wonder 3
			ArrayList<String> sofzio3Cost = new ArrayList<String>();
			for(int k=0;k<4;k++)
				sofzio3Cost.add("clay");
			stages.add(new CivicsCard("sofzio3","wonder",sofzio3Cost,null,3,7));
	}
		else if(name.equals("The Mausoleum of Halicarnassus")) {
			ArrayList<String> moh = new ArrayList<String>();
			moh.add("cloth");
			
			cardsPlayed.get("wonder").add(new ResourceCard("The Mausoleum of Halicarnassus","brown",null,null,3,moh));

			//wonder 1
			ArrayList<String> moh1Cost = new ArrayList<String>();
			moh1Cost.add("clay");
			moh1Cost.add("clay");
			stages.add(new CivicsCard("moh1","wonder",moh1Cost,null,3,3));
			//wonder 2
			ArrayList<String> moh2Cost = new ArrayList<String>();
			for(int k=0;k<3;k++)
				moh2Cost.add("ore");
			stages.add(new GuildCard("moh2","wonder",moh2Cost,null,3,"graveyard"));
			//wonder 3
			ArrayList<String> moh3Cost = new ArrayList<String>();
			for(int k=0;k<2;k++)
				moh3Cost.add("cloth");
			stages.add(new CivicsCard("moh3","wonder",moh3Cost,null,3,7));
	}
		else if(name.equals("The Pyramids of Giza")) {
			ArrayList<String> pog = new ArrayList<String>();
			pog.add("stone");
			
			cardsPlayed.get("wonder").add(new ResourceCard("The Pyramids of Giza","brown",null,null,3,pog));

			//wonder 1
			ArrayList<String> pog1Cost = new ArrayList<String>();
			pog1Cost.add("stone");
			pog1Cost.add("stone");
			stages.add(new CivicsCard("pog1","wonder",pog1Cost,null,3,3));
			//wonder 2
			ArrayList<String> pog2Cost = new ArrayList<String>();
			for(int k=0;k<3;k++)
				pog2Cost.add("wood");
			stages.add(new CivicsCard("pog2","wonder",pog2Cost,null,3,5));
			//wonder 3
			ArrayList<String> pog3Cost = new ArrayList<String>();
			for(int k=0;k<4;k++)
				pog3Cost.add("stone");
			stages.add(new CivicsCard("pog3","wonder",pog3Cost,null,3,7));
	}
  }

	public void changeHands(int i) {
		if((hand+i)<numOfPlayers&&(hand+i)>=0)
			hand=hand+i;
		else {
			if(hand+i<0)
				hand = numOfPlayers-1;
			else if((hand+i)>=numOfPlayers)
				hand=0;
		}
	}

	/*
	 * TODO:
	 * -needs to remove coins if card requires coins to build
	 */
	public void playCard(Card c) {
		HashSet<Card> l = cardsPlayed.get(c.getColor());
		l.add(c);
		if(c.getColor().equals("green")) {
			TechCard x = (TechCard) c;
			int quantity = techCardPoints.get(x.getTechGiven());
			techCardPoints.put(x.getTechGiven(), quantity++);
		}

	}
	

	public ArrayList<String> getAllPlayerResources() {
		
		ArrayList<String> list = getCardResources();
		
		ArrayList<Card> crds = new ArrayList<Card>();
		crds.addAll(cardsPlayed.get("yellow"));
		for(int i = 0 ; i<crds.size(); i++)
		{
			if(crds.get(i).equals("Forum")||crds.get(i).equals("Caravansery"))
			{
				list.add(((CommercialCard)(crds.get(i))).getId());
			}
		}
		
		if(name.contentEquals("The Lighthouse of Alexandria")&&playerWonders == 2)
		{
			list.addAll(((ResourceCard)(stages.get(1))).getResource());
		}
		
		// adds coins as resources
		for (int i = 0; i < getMoney(); i++)
			list.add("coin");
		
		//
		//crds.clear();
		

		return list;
	}
	
	public ArrayList<String> getCardResources()
	{
		ArrayList<String> list = new ArrayList<String>();
		HashSet<Card> temp = cardsPlayed.get("brown");
		Iterator<Card> tempIter = temp.iterator();
		ArrayList<Card> crds = new ArrayList<Card>();
		
		crds.addAll(cardsPlayed.get("wonder"));
		for (int i = 0; i < crds.size(); i++)
		{
			if(crds.get(i).getName().equals(name))
			{
				list.addAll(((ResourceCard)(crds.get(i))).getResource());;
			}
		}
		
		while(tempIter.hasNext())
		{
			ArrayList<String> arr = ((ResourceCard)(tempIter.next())).getResource();
			list.addAll(arr);
		}
		
		temp = cardsPlayed.get("silver");
		tempIter = temp.iterator();
		
		while(tempIter.hasNext())
		{
			ArrayList<String> arr = ((ResourceCard)(tempIter.next())).getResource();
			list.addAll(arr);
		}
		return list;
	}
	
	
	public void burnCard() {
		money +=3;
	}
	public int buildWonder() 
	{
		++playerWonders;
		if(playerWonders == 1)
			victoryPoints += 3;
		else if(playerWonders == 2)
			victoryPoints += 7;
		
		playCard(stages.get(playerWonders-1));
		
		HashSet<Card> x = cardsPlayed.get("wonder");
		x.add(stages.get(playerWonders-1));
		
		
		if(playerWonders == 2 && name.contentEquals("The Temple of Artemis in Ephesus"))
		{
			money+=9;
		}
		

		return playerWonders;
	}
	public boolean canBuildWonder()
	{
		if(playerWonders >= 3)
		  return false;
		return true;
	}
	public Card nextWonder()
	{
		return stages.get(playerWonders);
	}
	public boolean playable(Card c) {
		if(cardsPlayed.get(c.getColor()).contains(c))
			return false;
		return true;
	}
	public boolean inChain(Card c)
	{
		ArrayList<String> temp = c.getChain();
		
		ArrayList<Card> crds = new ArrayList<Card>();
		crds.addAll(cardsPlayed.get("brown"));
		crds.addAll(cardsPlayed.get("silver"));
		crds.addAll(cardsPlayed.get("red"));
		crds.addAll(cardsPlayed.get("blue"));
		crds.addAll(cardsPlayed.get("green"));
		crds.addAll(cardsPlayed.get("yellow"));
		crds.addAll(cardsPlayed.get("purple"));
		for(int i = 0 ; i<crds.size(); i++)
		{
			if(temp.contains(crds.get(i).getName()))
			{
				return true;
			}
		}
		
		return false;
	}
	public void addMoney(int i) {
		money+=i;
	}
	
	//getters and setters
	public HashMap<String, Integer> getTechCardPoints() {
		return techCardPoints;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, HashSet<Card>> getCardsPlayed() {
		return cardsPlayed;
	}

	public void setCardsPlayed(HashMap<String, HashSet<Card>> cardsPlayed) {
		this.cardsPlayed = cardsPlayed;
	}

	public int getPlayerWonders() {
		return playerWonders;
	}

	public void setPlayerWonders(int playerWonders) {
		this.playerWonders = playerWonders;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public ListIterator<Card> getIter() {
		return iter;
	}

	public void setIter(ListIterator<Card> iter) {
		this.iter = iter;
	}

	public int getHand() {
		return hand;
	}

	public void setHand(int hand) {
		this.hand = hand;
	}

	public int getMilitaryPower() {
		return militaryPower;
	}

	public void setMilitaryPower(int militaryPower) {
		this.militaryPower = militaryPower;
	}

	public Card getSelectedCard() {
		return selectedCard;
	}

	public void setSelectedCard(Card selectedCard) {
		this.selectedCard = selectedCard;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public TreeMap<String, Integer> getTrades() {
		return trades;
	}

	public void setTrades(TreeMap<String, Integer> trades) {
		this.trades = trades;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public ArrayList<Card> getStages() {
		return stages;
	}

	public void setStages(ArrayList<Card> stages) {
		this.stages = stages;
	}

	public String getWonderAbility() {
		return wonderAbility;
	}

	public void setWonderAbility(String wonderAbility) {
		this.wonderAbility = wonderAbility;
	}
	//end of getter/setters

}
