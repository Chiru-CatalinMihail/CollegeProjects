
/**
 * @author Chiru
 * Echivalentul entitatii Pasager mentionata in cerinta, GrupulGeneral este o generalizare care cuprinde tipurile Familie, Singur sau Grup.
 * GrupulGeneral contine numele grupului, punctajul dupa identificarea tuturor membrilor, biletul, un vector de persoane (care reprezinta membrii GrupuluiGeneral), tipul de imbarcare prioritara
 * si indexul curent la care poate fi adaugata o persoana in vector.
 */
public class GrupGeneral {
	String nume_grup;
	int punctaj;
	Bilet bilet;
	Persoana[] membru;
	boolean imbarcare_prioritara;
	int index; //contorizeaza pe a cata pozitie va fi adaugata o persoana noua. Scazand 1 se obtine numarul curent de persoane din GrupulGeneral
	
	public GrupGeneral() {};
	
	/** Constructor care aloca un GrupGeneral pe baza aparitiei primei persoane
	 * @param n: numarul n de persoane anuntate la inceputul fisierului de intrare ca vor participa la CoadaDePrioritate
	 * (alegerea lui n ca numar maxim de persoane este un worst-case scenario, care ia in calcul posibilitatea limita ca toate persoanele care se imbarca sa faca parte din aceeasi entitate (Grup, Familie)
	 * @param p: prima persoana p, pe baza caruia se stabileste numele grupului, tipul de imbarcare si de bilet (toti membrii aceleiasi structuri au acelasi tip de imbarcare si bilet)
	 */
	public GrupGeneral(int n, Persoana p) {
		this.nume_grup=p.grup_origine;
		this.membru= new Persoana[n];
		this.membru[0]=p;
		this.punctaj=0;
		this.index=1;
		this.imbarcare_prioritara=p.imbarcare_prioritara;
		this.bilet= p.bilet;
	}
	
	//functie care calculeaza si seteaza campul punctaj al grupului general adunand punctajul fiecarei persoane in parte, valoarea biletelor si a imbarcarii speciale raportate la numarul membrilor
	public void calculeazaPunctajGrup() {
		this.punctaj=0;
		for (int i = 0; i < this.index; i++) {
			this.punctaj+=this.membru[i].getPunctajPersoana();
		}
		this.punctaj+=this.index*this.bilet.getPunctajBilet();
		if(this.imbarcare_prioritara) {
			this.punctaj+=this.index*30;
		}
	}
	
	/** Foloseste functia de calculare a punctajului
	 * @return intoarce punctajul calculat
	 */
	public int getPunctajGrup() {
		this.calculeazaPunctajGrup();
		return this.punctaj;
	}
	
	//similar metodei "calculeazaPunctajGrup" mai verifica daca GrupulGeneral poate fi particularizat la una din structurile mostenitoare si daca se poate adauga bonusurile de punctaj specifice
	public void calculeazaPunctajInstantiat() {
		if (this instanceof Familie) {
			((Familie)this).calculeazaPunctajGrup();
		}
		if (this instanceof Grup) {
			((Grup)this).calculeazaPunctajGrup();
		}
		if (this instanceof Singur) {
			((Singur)this).calculeazaPunctajGrup();
		}
	}
	
	/**
	 * @param g: grupul general pe care il verifica
	 * Daca acesta este de fapt o instanta a unei structuri "fiu"
	 * @return Intoarce punctajul specific acelei structuri
	 */
	public int intoarcePunctajInstanta(GrupGeneral g) {
		if (g instanceof Familie) {
			return ((Familie)g).getPunctajGrup();
		}
		if (g instanceof Grup) {
			return ((Grup)g).getPunctajGrup();
		}
		if (g instanceof Singur) {
			return ((Singur)g).getPunctajGrup();
		}
		
		return -1; //error case
	}
	
	/**
	 * @param persoana: Persoana pe care o adaugam in grup pe indexul curent
	 * Dupa aceea se incrementeaza indexul.
	 */
	public void addPersoana(Persoana persoana) {
		this.membru[this.index]=persoana;
		this.index++;
	}
	
	/** Sterge o persoana pe baza numelui dintr-un GrupGeneral
	 * @param nume: numele persoanei
	 * Muta referinta ultimului element pe pozitia celui al carui nume a fost gasit, elibereaza ultima pozitie, decrementeaza indexul cu privire la numarul de membrii
	 * Garbage colector va sterge persoana care nu mai are referinta (cea pe care doream sa o eliminam) 
	 */
	public void stergePersoana(String nume) {
		int i;
		for (i = 0; i < this.index; i++) {
			if(this.membru[i].nume.contentEquals(nume)) {
				break;
			}
		}
		
		int ultimul_elem=this.index-1;
		this.membru[i]=this.membru[ultimul_elem];
		this.membru[ultimul_elem]=null;
		this.index--;
	}
}