
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Chiru
 * Mosteneste ideea de VectorDeGrupuri dar are un comportament specific de Heap
 * Vectorul CoadaDePrioritate se comporta ca un arbore
 */
public class CoadaDePrioritate extends VectorDeGrupuri {
	BufferedWriter writer;
	StringBuilder ajutor;
	
	/** Constructor care genereaza o CoadaDePrioritate si creeaza fisierul de output "queue.out"
	 * @param n: analog cu structura parinte VectorDeGrupuri se aloca n+1 spatii pentru a incepe numaratoarea heapului de la 1
	 * (Exista un risc mult mai mic de a gresi in while-uri si alte structuri repetitive facand div sa se ajunga la 1 decat sa fie totul translatat pentru a tine cont si de 0)
	 */
	public CoadaDePrioritate(int n) throws IOException {
		super(n+1);
		
		File file_in= new File("queue.out");
		FileWriter scriitor= new FileWriter(file_in);
		this.writer = new BufferedWriter(scriitor);
		this.ajutor=new StringBuilder();
	}
	
	/**
	 * Interschimba persistent doua GrupuriGenerale date ca parametru
	 * @param g1
	 * @param g2
	 */
	public void mySwap(GrupGeneral g1, GrupGeneral g2) {
		GrupGeneral aux= new GrupGeneral();
		aux.nume_grup=g1.nume_grup;
		aux.punctaj=g1.punctaj;
		aux.bilet=g1.bilet;
		aux.membru=g1.membru;
		aux.imbarcare_prioritara=g1.imbarcare_prioritara;
		aux.index=g1.index;
		
		g1.nume_grup=g2.nume_grup;
		g1.punctaj=g2.punctaj;
		g1.bilet=g2.bilet;
		g1.membru=g2.membru;
		g1.imbarcare_prioritara=g2.imbarcare_prioritara;
		g1.index=g2.index;
		
		g2.nume_grup=aux.nume_grup;
		g2.punctaj=aux.punctaj;
		g2.bilet=aux.bilet;
		g2.membru=aux.membru;
		g2.imbarcare_prioritara=aux.imbarcare_prioritara;
		g2.index=aux.index;
	}
	
	//suprascriem metoda din VectorDeGrupuri pentru o adapta necesitatilor de CoadaDePrioritate shiftand pozitia initiala de pe 0 pe 1 (este mai usor pentru a organiza heapul)
	public void adaugaGrupInVector(GrupGeneral g) {
		if(this.index_grupuri==0) {
			this.vector_grupuri[1]=g;
			this.index_grupuri=2;
		}else {
			this.vector_grupuri[this.index_grupuri]=g;
			this.index_grupuri++;
		}
	}
	
	/** Metoda modulara apelata in functia mare de insert
	 * Verifica daca ultimul element introdus are prioritate mai mare decat parintele sau.
	 * Daca da, ultimul element introdus urca in arbore, prin interschimbari succesive pana se respecta ordinea prioritatilor
	 * Daca nu, heapul este aranjat si se opreste din interschimbari
	 */
	public void sorteazaCoadaDupaAdaugare() {
		if(this.index_grupuri==2) {		//exista un singur element in arbore si nu are sens sortarea
			return;
		}
		int index_aux=this.index_grupuri-1, //indexul ultimului element introdus
			index_parinte=index_aux/2; 		//indexul parintelui acestuia
		while(index_aux!=1) {
			int punctaj_parinte, punctaj_copil;
			punctaj_copil=this.vector_grupuri[index_aux].punctaj;
			punctaj_parinte=this.vector_grupuri[index_parinte].punctaj;
			if(punctaj_copil>punctaj_parinte) {
				this.mySwap(this.vector_grupuri[index_aux], this.vector_grupuri[index_parinte]);
			}else return;
			index_aux=index_parinte;	//se updateaza indicele cu cel al parintelui
			index_parinte=index_aux/2;  //noul parinte este parintele parintelui
		}
	}
	
	/** Metoda auxiliara folosita in embark si in delete
	 *	Specific heapului inlocuieste rootul sau elementul de sters cu ultimul element introdus, sterge ultimul nod din arbore
	 *	Suplimentar, datorita implementarii decrementeaza indexul de grupuri la care se poate adauga in arbore
	 *  @param i: indexul de la care se face stergerea
	 */
	public void deleteNode(int i) {
		int index_aux=this.index_grupuri-1;
		this.mySwap(this.vector_grupuri[i], this.vector_grupuri[index_aux]);
		this.vector_grupuri[index_aux]=null;
		this.index_grupuri=this.index_grupuri-1;
	}
	
	
	//am pus aici >= reminder pt cand mai treci pe aici
	/** Metoda modulara apelata in functia mare de embark/delete
	 * Verifica daca noul root/element inlocuitor are prioritate mai mica decat copiii sai.
	 * Daca da, copilul maxim urca in locul sau, acesta devine copil si se verifica iar de la el prin interschimbari succesive pana se respecta ordinea prioritatilor
	 * Daca nu, heapul este aranjat si se opreste din interschimbari
	 * @param i: indexul de la care se face sortarea
	 */
	public void sorteazaCoadaDupaStergere(int index) {
		int index_aux=index,
			index_copil_1=2*index_aux,
			index_copil_2=2*index_aux+1;
		while(index_aux<this.index_grupuri) {
			int punctaj_aux, punctaj_copil_1, punctaj_copil_2;
			
			/* daca unul dintre copii este null atunci ii punem in punctaj mai mic decat cel mai mic punctaj posibi (0)
			   spre exemplu -1 sa fim siguri intotdeauna ca pierde si in fata celuilalt nod (nenul) sau in fata parintelui */
			if(this.vector_grupuri[index_copil_1]==null) {
				punctaj_copil_1=-1;
			}else {
				punctaj_copil_1=this.vector_grupuri[index_copil_1].punctaj;
			}
			if(this.vector_grupuri[index_copil_2]==null) {
				punctaj_copil_2=-1;
			}else {
				punctaj_copil_2=this.vector_grupuri[index_copil_2].punctaj;
			}
			
			punctaj_aux=this.vector_grupuri[index_aux].punctaj;
			
			//in situatie de egalitate se intra mereu pe copilul din stanga
			if(punctaj_copil_1>=punctaj_copil_2) {
				if(punctaj_copil_1>punctaj_aux) {
					this.mySwap(this.vector_grupuri[index_aux], this.vector_grupuri[index_copil_1]);
					index_aux=index_copil_1;
				}else return;
			}
			if(punctaj_copil_2>punctaj_copil_1) {
				if(punctaj_copil_2>punctaj_aux) {
					this.mySwap(this.vector_grupuri[index_aux], this.vector_grupuri[index_copil_2]);
					index_aux=index_copil_2;
				}else return;	
			}
			
			index_copil_1=2*index_aux;
			index_copil_2=2*index_aux+1;
		}
	}
	
	/** pe baza numelui cauta daca un grup exista in vectorul de grupuri
	 * daca exista intoarce indicele la care se gaseste grupul respectiv
	 * daca nu intoarce error codeul -1
	 */
	public int cautaGrupInVector(String nume_grup) {
		for (int i = 1; i < this.index_grupuri; i++) {
			if(this.vector_grupuri[i].nume_grup.equals(nume_grup)) {
				return i;
			}
		}
		return -1;
	}
	
	/** Metoda auxiliara apelata de functia list, construieste recursiv un StringBuilder ce contine afisajul in preordine al heapului
	 * @param index_afisare: indexul curent de pe care se concateneaza la StringBuilder
	 * @param ajutor: StringBuilderul in care este stocata informatia
	 */
	public void afisaj(int index_afisare, StringBuilder ajutor) {
		if(this.vector_grupuri[index_afisare]==null)
			return;
		if(2*index_afisare>=this.index_grupuri) {
			ajutor.append(this.vector_grupuri[index_afisare].nume_grup+" ");
			return;
		}
		ajutor.append(this.vector_grupuri[index_afisare].nume_grup+" ");
		this.afisaj(2*index_afisare, ajutor);
		this.afisaj(2*index_afisare+1, ajutor);
	}
	
	
	
	/** Functia de insert ceruta in enuntul temei
	 * @param g: Entitatea Pasager pe care trebuie sa o adaugam
	 * @param punctaj: punctajul lui g transmis specific unuia dintre cele 3 tipuri (Familie, Grup, Singur)
	 */
	public void insert(GrupGeneral g, int punctaj) {
		if(g.getPunctajGrup() != punctaj) {
			System.err.println("Punctajul grupului nu corespunde cu punctajul introdus");
			return;
		}
		this.adaugaGrupInVector(g);
		this.sorteazaCoadaDupaAdaugare();
	}
	
	//Functia de embark ceruta in enuntul temei
	public void embark() {
		if(this.index_grupuri==0)
			return;
		this.deleteNode(1); //sterge rootul
		this.sorteazaCoadaDupaStergere(1); //sorteaza pornind de la inlocuitorul rootului
	}
	
	//Functia list ceruta in enuntul temei
	public void list() {
		this.afisaj(1, this.ajutor);
		this.ajutor=this.ajutor.deleteCharAt(ajutor.length()-1);	//elimina ultimul spatiu cauzat de recurenta
		this.ajutor.append('\n'); //adauga pe ultima pozitie un newline
	}
	
	
	//Functia de stergere a unui grup intreg de persoane
	public void delete(GrupGeneral p) {
		String nume_de_cautat=p.nume_grup;
		int index=this.cautaGrupInVector(nume_de_cautat);
		if(index ==-1) return;
		this.deleteNode(index);
		this.sorteazaCoadaDupaStergere(index);
	}
	
	//Functia de stergere a unei singure persoane din GrupulGeneral p
	public void deletePersoana(GrupGeneral p, String nume_persoana) {
		int index=this.cautaGrupInVector(p.nume_grup);
		if(index ==-1) return;
		this.vector_grupuri[index].stergePersoana(nume_persoana);
		this.vector_grupuri[index].calculeazaPunctajInstantiat();
		this.sorteazaCoadaDupaStergere(index);
	}
}
