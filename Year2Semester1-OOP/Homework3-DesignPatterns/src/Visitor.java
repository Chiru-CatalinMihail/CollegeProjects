

/**
 * @author Chiru
 * Interfata care stabileste metodele celui ce viziteaza (Vizitatorului)
 */
public interface Visitor {
	
	/**Vizitorul are acces la echipa data ca parametru
	 * @param team
	 */
	public void visit(Team team);
}
