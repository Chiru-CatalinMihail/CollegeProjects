

/**
 * @author Chiru
 * Jucatorul unei echipe
 */
public class Player {
	private String name;
	private int score;
	
	/**Constructor al jucatorului cu parametrii
	 * @param name
	 * @param score
	 */
	public Player(String name, int score) {
		this.name=name;
		this.score=score;
	}
	
	/**Getter
	 * @return numele jucatorului
	 */
	public String getName() {
		return name;
	}
	
	/**Getter
	 * @return scorul jucatorului
	 */
	public int getScore() {
		return score;
	}
}
