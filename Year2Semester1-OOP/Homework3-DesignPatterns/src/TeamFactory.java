

/**
 * @author Chiru
 * Implementare a design patternului Factory pentru echipe de fotbal, baschet si handbal
 */
public class TeamFactory {
	
	/**Creeaza echipe cu ajutorul parametrilor
	 * @param typeOfTeam
	 * @param teamName
	 * @param gender
	 * @param numberOfPlayers
	 * @return intoarce echipa creata
	 */
	public Team createTeam(String typeOfTeam, String teamName, String gender, int numberOfPlayers) {
		if (typeOfTeam == null) {
			return null;
		}
			
		if (typeOfTeam.equalsIgnoreCase("football")) {
			return new FootballTeam(teamName, gender, numberOfPlayers);
		}
		if (typeOfTeam.equalsIgnoreCase("basketball")) {
			return new BasketballTeam(teamName, gender, numberOfPlayers);
		}
		if (typeOfTeam.equalsIgnoreCase("handball")) {
			return new HandballTeam(teamName, gender, numberOfPlayers);
		}
			
			return null;
	}
}
