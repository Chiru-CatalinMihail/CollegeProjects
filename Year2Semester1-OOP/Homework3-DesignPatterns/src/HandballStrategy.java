

/**
 * @author Chiru
 * Clasa ce implementeaza "Strategy" pentru competitiile de handbal masculine si feminine
 */
public class HandballStrategy implements PlayingStrategy {

	//calculeaza scorul de handbal al echipei introduse in functie de gen
	@Override
	public double calculateTeamScore(Team team) {
		if (team.getGender().equalsIgnoreCase("masculin")) {
			int sum=0;
			for (int i = 0; i < team.getPlayers().size(); i++) {
				sum+=team.getPlayers().get(i).getScore();
			}
			return sum;
		}else {
			int product=1;
			for (int i = 0; i < team.getPlayers().size(); i++) {
				product*=team.getPlayers().get(i).getScore();
			}
			return product;
		}
	}

}
