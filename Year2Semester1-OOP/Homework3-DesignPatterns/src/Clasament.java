

import java.util.ArrayList;

/**
 * @author Chiru
 * Clasamentul contine un ArrayList cu echipele ordonate descrescator in functie de punctaj
 * Reprezinta Subiectul din implementarea design patternului Observer
 */
public class Clasament implements Subject {
	private ArrayList<Team> positions;
	
	//constructor
	public Clasament() {
		this.positions= new ArrayList<Team>();
	}
	
	/**Getter
	 * @return lista cu echipe ordonate
	 */
	public ArrayList<Team> getPositions() {
		return positions;
	}	
	
	/*Metoda specifica Subiectului, in urma updatarii punctajelor echipelor
	 * 		recalculeaza clasamentul acestora si le notifica pe fiecare referitor la pozitia
	 * 		pe care o ocupa dupa actualizare
	 */
	@Override
	public void notifyObservers() {
		//BubbleSort with a twist XD
		for (int i = 0; i < this.positions.size()-1; i++) {
			for (int j = 0; j < this.positions.size()-i-1; j++) {
				if (this.positions.get(j).getPunctaj()<this.positions.get(j+1).getPunctaj()) {
					Team aux=this.positions.get(j);
					this.positions.remove(j);
					this.positions.add(j+1, aux);
				}
			}
		}
		
		//actualizeaza pozitia fiecarei echipe in clasament
		for (int i = 0; i < this.positions.size(); i++) {
			this.positions.get(i).setPozitia_in_clasament(i+1);
		}
	}
	
	//Metoda de adaugare a Observatorilor in lista
	@Override
	public void register(Observer obj) {
		this.positions.add((Team) obj);	
	}
}
