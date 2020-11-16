import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author Chiru
 * Clasa ce reprezinta un interval de o ora si care contine un ArrayList cu temperaturile sau umiditatile
 * 									inregistrate in acel interval orar
 * @param <E> parametru generic, poate fi Celula_Temperatura sau Celula_Umiditate
 */
public class IntervalOrar<E> {
	Durata ora;
	ArrayList<E> inputs;
	
	//constructor vid
	public IntervalOrar() {}
	
	/**Constructor parametrizat care initializeaza
	 * @param ora = durata intervalului, contine limitale sale inferioara si superioara 
	 */
	public IntervalOrar(Durata ora) {
		this.ora=ora;
		this.inputs=new ArrayList<E>();
	}
	
	/**Metoda care asigura adaugarea sortata a celulelor de temperatura
	 * Daca lista e vida se adauga direct fara comparatii
	 * Daca exista elemente se fac comparatii pana se gaseste un element mai mare decat cel curent
	 * 		(caz in care se introduce celula in asa fel incat sa se pastreze ordinea crescatoare a temperaturilor),
	 * 		sau un element egal (caz in care nu se introduce nimic conform cerintei (deja exista temperatura)).
	 * Daca in urma parcurgerii complete a listei nu a fost adaugata nicaieri celula, inseamna ca are temperatura cea
	 * 		mai mare si atunci va fi adaugata la sfarsitul listei
	 * @param cel_temp = noua celula care ar trebui introdusa in lista de celule
	 */
	public void adaugareSortataTemp(E cel_temp) {
		ArrayList<E> lista_temperaturi= this.inputs;
		if(lista_temperaturi.isEmpty()) {
			lista_temperaturi.add(cel_temp);
			return;
		}
		
		ListIterator<E> iterator_temperaturi= lista_temperaturi.listIterator();
		while (iterator_temperaturi.hasNext()) {
			E celula_aux=iterator_temperaturi.next();
			if(((Celula_Temperatura) cel_temp).compareTo(celula_aux)==-1) {
				lista_temperaturi.add(iterator_temperaturi.nextIndex()-1, cel_temp);
				return;
			}
			if(((Celula_Temperatura) cel_temp).compareTo(celula_aux)==0) {
				return;
			}
			
		}
		lista_temperaturi.add(cel_temp);
	}
	
	/**Metoda care asigura adaugarea sortata a celulelor de umiditate, similar cu cea folosita pentru temperatura
	 * Daca lista e vida se adauga direct fara comparatii
	 * Daca exista elemente se fac comparatii pana se gaseste un element mai mic decat cel curent
	 * 		(caz in care se introduce celula in asa fel incat sa se pastreze ordinea descrescatoare),
	 * 		sau un element egal (caz in care nu se introduce nimic).
	 * Daca in urma parcurgerii completa a listei nu a fost adaugata nicaieri celula,
	 * 		atunci va fi adaugata la sfarsitul listei.
	 * @param cel_umid = noua celula care ar trebui introdusa in lista de celule
	 */
	public void adaugareSortataUmid(E cel_umid) {
		ArrayList<E> lista_umiditati= this.inputs;
		if(lista_umiditati.isEmpty()) {
			lista_umiditati.add(cel_umid);
			return;
		}
		ListIterator<E> iterator_umiditati= lista_umiditati.listIterator();
		while (iterator_umiditati.hasNext()) {
			E celula_aux=iterator_umiditati.next();
			if(((Celula_Umiditate) cel_umid).compareTo(celula_aux)==1) {
				lista_umiditati.add(iterator_umiditati.nextIndex()-1, cel_umid);
				return;
			}
			if(((Celula_Umiditate) cel_umid).compareTo(celula_aux)==0) {
				return;
			}	
		}
		lista_umiditati.add(cel_umid);
	}	
}