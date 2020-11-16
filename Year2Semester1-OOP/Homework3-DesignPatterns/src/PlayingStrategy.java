

/**
 * @author Chiru
 * Interfata pentru design patternul "Strategy"
 */
public interface PlayingStrategy {
	
	/**
	 * @param team echipa al carui scor dorim sa il calculam
	 * @return scorul echipei respective
	 */
	public double calculateTeamScore(Team team);
}
