/**
 * @author Chiru
 * Clasa ce contine un vector de GrupuriGenerale pentru gestionarea si memorarea informatiei oferite din input
 */
public class VectorDeGrupuri {
	GrupGeneral[] vector_grupuri;
	int index_grupuri; //contorizeaza pe a cata pozitie va fi adaugat un nou GrupGeneral. Scazand 1 se obtine numarul curent de GrupuriGenerale din Vector
	
	public VectorDeGrupuri() {}	
	
	/**
	 * @param n: numarul n de persoane anuntate la inceputul fisierului de intrare ca vor participa la CoadaDePrioritate
	 * (alegerea lui n ca numar maxim de persoane este un worst-case scenario, care ia in calcul posibilitatea limita ca toate persoanele care se imbarca sa faca parte doar din entitati diferite
	 */
	public VectorDeGrupuri(int n) {
		this.vector_grupuri=new GrupGeneral[n];
		this.index_grupuri=0;
	}
	
	/** 
	 * @param nume_grup: Pe baza numelui cauta daca un grup exista in vectorul de grupuri
	 * @return daca exista intoarce indicele la care se gaseste grupul respectiv, daca nu intoarce error codeul -1
	 */
	public int cautaGrupInVector(String nume_grup) {
		for (int i = 0; i < this.index_grupuri; i++) {
			if(this.vector_grupuri[i].nume_grup.equals(nume_grup)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @param g grupul de adaugat in vector
	 * verifica daca grupul general exista deja in vector, iar daca nu exista il asociaza ultimului index disponibil din vector si incrementeaza acest index
	 */
	public void adaugaGrupInVector(GrupGeneral g) {
		int exista=this.cautaGrupInVector(g.nume_grup);
		if(exista==-1) {
			this.vector_grupuri[this.index_grupuri]=g;
			this.index_grupuri++;
		}
	}
	
	/**
	 * @param p: persoana de adaugat in vectorul GrupuluiGeneral cu acelasi nume, din VectorulDeGrupuri
	 * Daca numele grupului nu exista in VectorulDeGrupuri, inseamna ca p este primul membru si atunci se initializeaza corespunzator informatiei din p
	 * un Grup/Singur/Familie cu acelasi nume si indexul de grupuri se incrementeaza
	 */
	public void adaugaPersoanaInVector(Persoana p) {
		int ok=this.cautaGrupInVector(p.grup_origine);
		if(ok!=-1) {
			this.vector_grupuri[ok].addPersoana(p);
		}else {
			if(p.grup_origine.charAt(0)=='s') {
				this.vector_grupuri[this.index_grupuri]=new Singur(p);
			}
				
			if(p.grup_origine.charAt(0)=='f') {
				this.vector_grupuri[this.index_grupuri]=new Familie(this.vector_grupuri.length, p);
			}
				
			if(p.grup_origine.charAt(0)=='g') {
				this.vector_grupuri[this.index_grupuri]=new Grup(this.vector_grupuri.length, p);
			}
			this.vector_grupuri[this.index_grupuri].index=1;
			this.index_grupuri++;
		}
	}
}