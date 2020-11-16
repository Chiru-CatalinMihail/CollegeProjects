/**
 * @author Chiru
 * Clasa Singur constrange ideea mai abstracta a GrupuluiGeneral la un vector de o singura persoana, suprascrie felul in care se calculeaza punctajul (redus la o persoana), dar si metoda de obtinere punctaj
 * pentru a o returna punctajul specific grupului (aceeasi abordare ca Familia)
 */
public class Singur extends GrupGeneral {
	
	/** constructor care aloca Singur ca vector GrupGeneral de o singura Persoana
	 * @param p unica Persoana continuta de singur
	 */
	public Singur(Persoana p) {
		super(1,p);
	}
	
	//calculeaza punctajul pentru o singura persoana 
	public void calculeazaPunctajGrup() {
		this.punctaj=0; //ne asiguram ca initializam punctajul cu 0 inainte de a-l calcula
		this.punctaj+=this.membru[0].getPunctajPersoana();
		this.punctaj+=this.bilet.getPunctajBilet();
		this.punctaj+=1*30*((this.imbarcare_prioritara)?1:0);
	}
	
	//intoarce punctajul raportat la instanta Singur
	public int getPunctajGrup() {
		return this.punctaj;
	}
}