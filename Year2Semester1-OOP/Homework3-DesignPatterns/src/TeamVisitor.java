
/**
 * @author Chiru
 * Visitor al unei echipe, stocheaza scorul acesteia folosindu-se de Strategy
 */
public class TeamVisitor implements Visitor {
	private double calculated_score=0;
	
	//Metoda care retine scorul echipei vizitate, in campul privat al vizitatorului.
	@Override
	public void visit(Team team) {
		if(team.getType().equalsIgnoreCase("football")) {
			setCalculated_score(new FootballStrategy().calculateTeamScore(team));
		}
		
		if(team.getType().equalsIgnoreCase("basketball")) {
			setCalculated_score(new BasketballStrategy().calculateTeamScore(team));
		}
		
		if(team.getType().equalsIgnoreCase("handball")) {
			setCalculated_score(new HandballStrategy().calculateTeamScore(team));
		}
		
	}

	/**Getter pentru scorul calculat
	 * @return intoarce scorul retinut de Visitor
	 */
	public double getCalculated_score() {
		return calculated_score;
	}

	/**Setter pentru scorul calculat
	 * @param calculated_score
	 */
	public void setCalculated_score(double calculated_score) {
		this.calculated_score = calculated_score;
	}
}