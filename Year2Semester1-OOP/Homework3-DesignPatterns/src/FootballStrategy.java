

/**
 * @author Chiru
 * Clasa ce implementeaza "Strategy" pentru competitiile de fotbal masculine si feminine
 */
public class FootballStrategy implements PlayingStrategy {

	//calculeaza scorul de fotbal al echipei introduse in functie de gen
	@Override
	public double calculateTeamScore(Team team) {
		if (team.getGender().equalsIgnoreCase("masculin")) {
			int sum=0, maxim=-1;
			for (int i = 0; i < team.getPlayers().size(); i++) {
				if (maxim<team.getPlayers().get(i).getScore()) {
					maxim=team.getPlayers().get(i).getScore();
				}
				sum+=team.getPlayers().get(i).getScore();
			}
			return sum+maxim;
		}else {
			int sum=0, minim=team.getPlayers().get(0).getScore();;
			for (int i = 0; i < team.getPlayers().size(); i++) {
				if (minim>team.getPlayers().get(i).getScore()) {
					minim=team.getPlayers().get(i).getScore();
				}
				sum+=team.getPlayers().get(i).getScore();
			}
			return sum+minim;
		}
	}
	
}
