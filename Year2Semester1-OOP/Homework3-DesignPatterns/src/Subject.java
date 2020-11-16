

/**
 * @author Chiru
 * Interfata care stabileste metodele Subiectului de observat
 */
public interface Subject {
		//Metoda de inregistrare a observerului
		public void register(Observer obj);
		
		//Metoda de notificare a tuturor observerilor
		public void notifyObservers();
}
