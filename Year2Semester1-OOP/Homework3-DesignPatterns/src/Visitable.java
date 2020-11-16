

/**
 * @author Chiru
 * Interfata care stabileste metodele celui care este vizitat
 */
public interface Visitable {
	
	/**Vizitabilul permite accesul vizitatorului
	 * @param visitor
	 */
	public void accept(Visitor visitor);
}
