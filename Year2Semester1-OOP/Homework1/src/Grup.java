/**
 * @author Chiru
 * Clasa Grup mosteneste ideea mai abstracta a GrupuluiGeneral, suprascrie felul in care se calculeaza punctajul (specific calculului pentru grup),
 * dar si metoda de obtinere punctaj pentru a returna punctajul specific grupului (aceeasi abordare ca Familia)
 */
public class Grup extends GrupGeneral {

	/** constructor care aloca un grup pe baza primului membru: Persoana p
	 * @param n nr maxim de persoane dintr-un grup (egal cu numarul total de pasageri anuntati la inceputul inputului)
	 * @param p prima Persoana care este asociata Grupului
	 */
	public Grup(int n, Persoana p) {
		super(n, p);
	}
	
	//calculeaza punctajul adaugand bonusul de grup
	public void calculeazaPunctajGrup() {
		this.punctaj=0; //ne asiguram ca initializam punctajul cu 0 inainte de a-l calcula
		for (int i = 0; i < this.index; i++) {
			this.punctaj+=this.membru[i].getPunctajPersoana();
		}
		this.punctaj+=this.index*this.bilet.getPunctajBilet();
		if(this.imbarcare_prioritara) {
			this.punctaj+=this.index*30;
		}
		
		this.punctaj+=5;
	}
	
	//intoarce punctajul grupului
	public int getPunctajGrup() {
		return this.punctaj;
	}
}