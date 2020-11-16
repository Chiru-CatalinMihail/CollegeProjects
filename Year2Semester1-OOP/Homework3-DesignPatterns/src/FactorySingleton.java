

/**
 * @author Chiru
 * Implementare a design patternului Singleton ce contine un singur FactoryTeam
 */
public class FactorySingleton {
	private static FactorySingleton instance;
	public final TeamFactory factory;
	
	//instantiaza factoryul din Singleton
	private FactorySingleton() {
		this.factory= new TeamFactory();
	}
	
	/**Metoda care
	 * @return intoarce instanta unica a Singletonului
	 */
	public static FactorySingleton getinstance() {
		if (instance==null) {
			return new FactorySingleton();
		}
		return instance;
	}
}
