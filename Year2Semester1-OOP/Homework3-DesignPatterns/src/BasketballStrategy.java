

/**
 * @author Chiru
 * Clasa ce implementeaza "Strategy" pentru competitiile de baschet masculine si feminine
 */
public class BasketballStrategy implements PlayingStrategy {
	
	//calculeaza scorul de baschet al echipei introduse
	@Override
	public double calculateTeamScore(Team team) {
		int sum=0;
		for (int i = 0; i < team.getPlayers().size(); i++) {
			sum+=team.getPlayers().get(i).getScore();
		}
		return (double)sum/team.getPlayers().size();
	}
}
