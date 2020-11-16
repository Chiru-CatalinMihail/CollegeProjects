

import java.util.ArrayList;

/**
 * @author Chiru
 * Echipa oarecare, ea reprezinta Vizitabilul si Observatorul in rezolvarea temei
 */
public class Team implements Visitable, Observer {
	private String type;
	private String teamName;
	private String gender;
	private int numberOfPlayers;
	private ArrayList<Player> players;
	private int punctaj;
	private int pozitia_in_clasament;
	private Subject rankings;
	
	
	/**Constructor al echipei cu parametrii:
	 * @param type
	 * @param teamName
	 * @param gender
	 * @param numberOfPlayers
	 */
	public Team(String type, String teamName, String gender, int numberOfPlayers) {
		this.type=type;
		this.teamName=teamName;
		this.gender=gender;
		this.numberOfPlayers=numberOfPlayers;
		this.players= new ArrayList<Player>();
		this.punctaj=0;
	}
	
	/**Getter
	 * @return tipul echipei
	 */
	public String getType() {
		return type;
	}
	
	/**Getter
	 * @return numele echipei
	 */
	public String getTeamName() {
		return teamName;
	}
	
	/**Getter
	 * @return genul echipei
	 */
	public String getGender() {
		return gender;
	}
	
	
	/**Getter
	 * @return numarul de jucatori
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}
	
	/**Getter
	 * @return arrayul cu jucatorii
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	/**Getter
	 * @return punctajul echipei
	 */
	public int getPunctaj() {
		return punctaj;
	}
	
	/**Getter
	 * @return pozitia in clasament
	 */
	public int getPozitia_in_clasament() {
		return pozitia_in_clasament;
	}
	

	/**Setter
	 * @param punctaj punctajul pe care il seteaza echipei
	 */
	public void setPunctaj(int punctaj) {
		this.punctaj = punctaj;
	}

	/**Setter
	 * @param pozitia_in_clasament
	 */
	public void setPozitia_in_clasament(int pozitia_in_clasament) {
		this.pozitia_in_clasament = pozitia_in_clasament;
	}
	
	/**Setter
	 * @param rankings Subiectul la care se refera un Observer de tip Team
	 */
	public void setRankings(Subject rankings) {
		this.rankings= rankings;
	}
		
	/**Adauga un jucator in lista de jucatori a echipei
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
	}

	/**Metoda care scrie in storer informatiile unei echipe pe baza cerintei de la taskul 1
	 * @param storer
	 */
	public void showTeam(StringBuilder storer) {
		storer.append("{teamName: "+this.teamName+", gender: "+this.gender+", numberOfPlayers: "+
						this.numberOfPlayers+", players: [");
		for (int i = 0; i < this.players.size(); i++) {
			storer.append("{name: "+this.players.get(i).getName()+", score: "+
							this.players.get(i).getScore()+"}, ");
		}
		storer.deleteCharAt(storer.length()-1);
		storer.deleteCharAt(storer.length()-1);
		storer.append("]}\n");
	}
	
	//Metoda din Visitable specifica design patternului Visitor, Team accepta Visitorul
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	
	/**Metoda care compara punctajul echipei actuale (this), cu cel al unei echipe date ca parametru
	 * cu ajutorul visitorilor
	 * @param team echipa cu care are meci this
	 * @param visitor visitorul in care este calculat si retinut scorul echipei adverse
	 */
	public void playOtherTeam(Team team, TeamVisitor visitor) {
		TeamVisitor visitor_local=new TeamVisitor();
		this.accept(visitor_local);
		if (visitor_local.getCalculated_score()>visitor.getCalculated_score()) {
			this.punctaj+=3;
		}
		if (visitor_local.getCalculated_score()==visitor.getCalculated_score()) {
			this.punctaj+=1;
			team.setPunctaj(team.getPunctaj()+1);
		}
		if (visitor_local.getCalculated_score()<visitor.getCalculated_score()) {
			team.setPunctaj(team.getPunctaj()+3);
		}
		this.update(); //in urma jucarii unui meci trebuie sa actualizam clasamentul
	}	
	
	//Anunta Subiectul de actualizare si cu ajutorul sau recalculeaza pozitia echipelor in clasament
	@Override
	public void update() {
		this.rankings.notifyObservers();
	}
}
