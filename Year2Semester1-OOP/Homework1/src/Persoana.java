
/**
 * @author marmy
 *	Persoana contine datele fiecarui om care participa la intreg procesul de asteptat la coada de prioritate si imbarcat in avion
 *	Persoana este caracterizata prin apartenenta la un grup origine, nume, varsta, tipul biletului, existenta sau nu a nevoilor speciale si a imbarcarii prioritare
 */
public class Persoana {
	String grup_origine;
	String nume;
	int varsta;
	Bilet bilet;
	boolean imbarcare_prioritara;
	boolean nevoi_speciale;
	
	public Persoana(){}
	
	/** Constructor care seteaza
	 * @param grup_origine: grupul de origine al persoanei
	 * @param nume: numele persoanei
	 * @param varsta: varsta persoanei
	 * @param tip_bilet: tipul biletului pe care il detine persoana respectiva
	 * @param imbarcare_prioritara: valoarea de adevar a imbarcarii prioritare
	 * @param nevoi_speciale: existenta nevoilor speciale
	 */
	public Persoana(String grup_origine, String nume, int varsta, char tip_bilet, boolean imbarcare_prioritara, boolean nevoi_speciale){
		this.grup_origine=grup_origine;
		this.nume=nume;
		this.varsta=varsta;
		this.bilet= new Bilet(tip_bilet);
		this.imbarcare_prioritara=imbarcare_prioritara;
		this.nevoi_speciale=nevoi_speciale;
	}
	
	/** Calculeaza punctajul fiecarei persoane conform cerintei
	 * Observatie: imbarcarea prioritara si tipul de bilet nu sunt luate in calcul deoarece acestea se calculeaza la nivel de structura (GrupGeneral: Familie, Grup, Singur)
	 * @return intoarce punctajul obtinut in urma calculului 
	 */
	public int getPunctajPersoana(){
		int punctaj=0;
		if(this.varsta<2) {
			punctaj+=20;
		}
		if(2<=this.varsta && this.varsta<5) {
			punctaj+=10;
		}
		if(5<=this.varsta && this.varsta<10) {
			punctaj+=5;
		}
		if(10<=this.varsta && this.varsta<60) {
			punctaj+=0;
		}
		if(60<=this.varsta) {
			punctaj+=15;
		}
		
		if(this.nevoi_speciale)
			punctaj+=100;
		
		return punctaj;
	}
}
