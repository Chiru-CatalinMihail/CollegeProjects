/**
 * @author Chiru
 * Clasa Familie mosteneste ideea mai abstracta a GrupuluiGeneral, suprascrie felul in care se calculeaza punctajul (specific calculului pentru familie),
 * dar si metoda de obtinere punctaj pentru a returna punctajul specific familiei
 */
public class Familie extends GrupGeneral {
	
	/** constructor care aloca o familie pe baza primului membru: Persoana p
	 * @param n numarul maxim de persoane dintr-o familie (egal cu numarul total de pasageri anuntati la inceputul inputului)
	 * @param p prima Persoana care este asociata unei Familii
	 */
	public Familie(int n, Persoana p) {
		super(n, p);
	}
	
	//calculeaza punctajul adaugand bonusul de familie 
	public void calculeazaPunctajGrup() {
		this.punctaj=0; //ne asiguram ca initializam punctajul cu 0 inainte de a-l calcula
		for (int i = 0; i < this.index; i++) {
			this.punctaj+=this.membru[i].getPunctajPersoana();
		}
		this.punctaj+=this.index*this.bilet.getPunctajBilet();
		if(this.imbarcare_prioritara) {
			this.punctaj+=this.index*30;
		}
		
		this.punctaj+=10;
	}
	
	 //intoarce punctajul familiei
	public int getPunctajGrup() {
		return this.punctaj;
	}
}