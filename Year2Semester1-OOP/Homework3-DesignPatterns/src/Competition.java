

import java.util.ArrayList;

/**
 * @author Chiru
 * Competitia la care participa echipele
 */
public class Competition {
	private String type;
	private String gender;
	private ArrayList<Team> teams;
	private Clasament clasament;
	
	/**Constructor al competitiei cu parametrii:
	 * @param type
	 * @param gender
	 */
	public Competition(String type, String gender) {
		this.type=type;
		this.gender=gender;
		this.teams= new ArrayList<Team>();
		this.clasament= new Clasament();
	}
	
	/**Metoda care adauga echipa in competitie daca respecta conditiile de intrare
	 * @param team
	 */
	public void addInCompetition(Team team) {
		if (this.gender.equalsIgnoreCase(team.getGender())&&this.type.equalsIgnoreCase(team.getType())) {
			this.teams.add(team);
			this.clasament.register(team);
			team.setRankings(this.clasament);
		}
	}
	
	/**Metoda care calculeaza parcursul intregii competitii
	 * Cu ajutorul Visitorilor, este calculat si comparat scorul intre oricare doua echipe din liga
	 * 				o singura data si in functie de acesta este actualizat punctajul acestora.
	 * O data actualizat punctajul, Observerul trimite comanda de update asupra Clasamentului, care
	 * 				se modifica corespunzator, ordonand echipele in ordinea descrecatoare a punctajelor
	 * 				si notificandu-le de pozitia lor curenta dupa jucarea fiecarui meci.
	 */
	public void playTheCompetition() {
		for (int i = 0; i < this.teams.size()-1; i++) {
			for (int j= i+1; j < this.teams.size(); j++) {
				TeamVisitor visitor =new TeamVisitor();
				this.teams.get(j).accept(visitor);
				this.teams.get(i).playOtherTeam(this.teams.get(j), visitor);
			}
		}
	}
	
	/**Metoda care scrie in storer podiumul clasamentului si pozitia fiecarei echipe la finalul competitiei
	 * @param storer
	 */
	public void afisareClasament(StringBuilder storer) {
		this.playTheCompetition();
		storer.append(this.clasament.getPositions().get(0).getTeamName()+"\n"
					 +clasament.getPositions().get(1).getTeamName()+"\n"
					 +clasament.getPositions().get(2).getTeamName()+"\n");
		for (int i = 0; i < this.teams.size(); i++) {
			storer.append("Echipa "+this.teams.get(i).getTeamName()+" a ocupat locul "
						  +this.teams.get(i).getPozitia_in_clasament()+"\n");
		}
		storer.deleteCharAt(storer.length()-1);
	}
}
