import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author Chiru
 * Casa contine o lista cu toate camerele ordonate crescator
 * La nivelul acestei Clase sunt dezvoltate metodele principale ale temei
 */
public class Casa {
	ArrayList<Camera> rooms;
	BufferedWriter writer; 
	StringBuilder ajutor; //StringBuilder folosit pentru a inmagazina informatia de outputat a intregii teme
	
	//constructor care initializeaza ArrayListul de camere, bufferwriterul si stringbuilderul
	public Casa() throws IOException {
		this.rooms=new ArrayList<Camera>();
		File file_in= new File("therm.out");
		FileWriter scriitor= new FileWriter(file_in);
		this.writer = new BufferedWriter(scriitor);
		this.ajutor=new StringBuilder();
	}
	
	
	/**Metoda care intoarce temperatura minima din ultima ora in care a inregistrat deviceul
	 * @param camera
	 * @return valoarea temperaturii minime din ultima ora din camera respectiva
	 */
	public double intoarceTemperaturaMinima(Camera camera) {
		ArrayList<IntervalOrar<Celula_Temperatura>>lista_intervale_orare=camera.date_temp;
		
		for (int i = 0; i < lista_intervale_orare.size(); i++) {
			//daca ultima ora nu contine nicio temperatura se trece mai in spate cu o ora
			if(lista_intervale_orare.get(23-i).inputs.isEmpty()) {
				continue;
			}else {
				/*cand se gaseste un interval in care exista cel putin un element, intoarce prima temperatura
				din acel interval, datorita ordonarii crescatoare*/
				return lista_intervale_orare.get(23-i).inputs.get(0).temp;
			}
		}
		return -3;
	}
	
	
	/**Metoda care intoarce umiditatea maxima din ultima ora in care a inregistrat deviceul
	 * @param camera
	 * @return valoarea temperaturii maxime din ultima ora din camera respectiva
	 * similar cu intoarceTemperaturaMinima
	 */
	public double intoarceUmiditateaMaxima(Camera camera) {
		ArrayList<IntervalOrar<Celula_Umiditate>>lista_intervale_orare=camera.date_umid;
		
		for (int i = 0; i < lista_intervale_orare.size(); i++) {
			if(lista_intervale_orare.get(23-i).inputs.isEmpty()) {
				continue;
			}else {
					//maximul este primul element datorita ordonarii descrecatoare a umiditatilor
					return lista_intervale_orare.get(23-i).inputs.get(0).umid;
			}
		}
		return -3;
	}
	
	
	
	/**Metoda trigger calculeaza conform cerintei temperatura medie a intregii locuinte
	 * Compara aceasta temperatura cu temperatura globala trimisa ca parametru si in functie de aceasta
	 * comparatie intoarce 1 daca media e mai mica decat valoarea globala si 0 altfel
	 * @param temp_globala
	 * @return 0/1
	 */
	public int trigger(double temp_globala) {
		ListIterator<Camera> iterator_camere=this.rooms.listIterator();
		double numarator=0, numitor=0;
		Camera camera_aux = null;
		
		while (iterator_camere.hasNext()) {
			camera_aux=iterator_camere.next();
			double temperatura=this.intoarceTemperaturaMinima(camera_aux);
			numarator+= temperatura*camera_aux.surface;
			numitor += camera_aux.surface;
		}
		
		if(numitor==0) {
			return 0;
		}
		
		double mean=numarator/numitor;
		if (mean<temp_globala) {
			return 1;
		}else {
			return 0;
		}
	}
	
	
	
	/**Similar cu trigger, calculeaza umiditatea medie a casei pe care o compara cu umiditatea globala
	 * @param umid_globala
	 * @return 0 daca media este mai mare decat temperatura globala, 1 altfel
	 */
	public int triggerh(double umid_globala) {
		ListIterator<Camera> iterator_camere=this.rooms.listIterator();
		double numarator=0, numitor=0;
		Camera camera_aux = null;
		
		while (iterator_camere.hasNext()) {
			camera_aux=iterator_camere.next();
			double temperatura=this.intoarceUmiditateaMaxima(camera_aux);
			numarator+= temperatura*camera_aux.surface;
			numitor += camera_aux.surface;
		}
		
		if(numitor==0) {
			return 1;
		}
		
		double mean=numarator/numitor;
		if (mean>umid_globala) {
			return 0;
		}else {
			return 1;
		}
	}
	
	/**Metoda care cauta camera in care a fost inregistrata o noua temperatura, cauta intervalul
	 * orar in care poate fi incadrata pe baza timestampului celula de temperatura si o insereaza
	 * corespunzator
	 * @param device_id
	 * @param cel_temp = celula care contine temperatura si timestampul la care a fost transmisa aceasta
	 */
	public void observe(String device_id, Celula_Temperatura cel_temp) {
		ListIterator<Camera> iterator_camere=this.rooms.listIterator();
		Camera camera_aux = null;
		
		//cauta camera in functie de id
		while (iterator_camere.hasNext()) {
			camera_aux=iterator_camere.next();
			if (camera_aux.nume_device.equals(device_id)) {
				break;
			}
		}
		
		ArrayList<IntervalOrar<Celula_Temperatura>>lista_intervale_orare=camera_aux.date_temp;
		ListIterator<IntervalOrar<Celula_Temperatura>> iterator_intervale=lista_intervale_orare.listIterator();
		
		//cauta si adauga in intervalul corespunzator celula cu temperatura
		while(iterator_intervale.hasNext()) {
			IntervalOrar<Celula_Temperatura> segment_orar=iterator_intervale.next();
			if ((segment_orar.ora.stamp_inf<=cel_temp.tstp)&&(segment_orar.ora.stamp_sup>cel_temp.tstp)) {
				segment_orar.adaugareSortataTemp(cel_temp);
				return;
			}
		}
	}
	
	//Metoda analoaga lui observe pentru umiditati
	public void observeh(String device_id, Celula_Umiditate cel_umid) {
		ListIterator<Camera> iterator_camere=this.rooms.listIterator();
		Camera camera_aux = null;
		while (iterator_camere.hasNext()) {
			camera_aux=iterator_camere.next();
			if (camera_aux.nume_device.equals(device_id)) {
				break;
			}
		}
		ArrayList<IntervalOrar<Celula_Umiditate>>lista_intervale_orare=camera_aux.date_umid;
		ListIterator<IntervalOrar<Celula_Umiditate>> iterator_intervale=lista_intervale_orare.listIterator();
		while(iterator_intervale.hasNext()) {
			IntervalOrar<Celula_Umiditate> segment_orar=iterator_intervale.next();
			if ((segment_orar.ora.stamp_inf<=cel_umid.tstp)&&(segment_orar.ora.stamp_sup>cel_umid.tstp)) {
				segment_orar.adaugareSortataUmid(cel_umid);
				return;
			}
		}
	}
	
	
	
	/**Metoda care cauta camera pe baza numelui sau si afiseaza de la intervalul orar cel mai recent catre
	 * cel mai indepartat temperaturile inregistrate intr-un interval de referinta dat ca parametru prin
	 * cele doua capete ale sale
	 * @param nume_camera
	 * @param start_interval inceputul intervalului de referinta
	 * @param stop_interval sfarsitul intervalului de referinta
	 */
	public void list(String nume_camera, long start_interval, long stop_interval) {
		ListIterator<Camera> iterator_camere=this.rooms.listIterator();
		Camera camera_aux = null;
		//gaseste camera dupa nume
		while (iterator_camere.hasNext()) {
			camera_aux=iterator_camere.next();
			if (camera_aux.nume_camera.equals(nume_camera)) {
				break;
			}
		}
		
		//scrie numele camerei in "ajutor"
		this.ajutor.append(nume_camera);
		ArrayList<IntervalOrar<Celula_Temperatura>>lista_intervale_orare=camera_aux.date_temp;
		
		/*verifica de la recent spre indepartat intervalele orare, iar temperaturile celulelor al
		caror timestamp este cuprins in intervalul dat sunt scrise in "ajutor"*/
		for (int i = 0; i < lista_intervale_orare.size(); i++) {
			IntervalOrar<Celula_Temperatura> segment_orar=lista_intervale_orare.get(23-i);
			ArrayList<Celula_Temperatura> lista_temperaturi=segment_orar.inputs;
			ListIterator<Celula_Temperatura> iterator_temperaturi=lista_temperaturi.listIterator();
			
			//itereaza prin toate temperaturile inregistrate intr-un anumit interval
			while (iterator_temperaturi.hasNext()) {
				Celula_Temperatura cel_auxiliara=iterator_temperaturi.next();
				if((start_interval<=cel_auxiliara.tstp)&&(cel_auxiliara.tstp<=stop_interval)) {
					
					/*converteste valorile in asa fel incat sa fie afisate cu 2 zecimale
					(ex: transforma 22 in 22.00 la afisaj)*/
					DecimalFormat df= new DecimalFormat();
					df.setMaximumFractionDigits(2);
					df.setMinimumFractionDigits(2);
					this.ajutor.append(" "+df.format(cel_auxiliara.temp));
				}
			}
		}
	this.ajutor.append("\n"); //scrie newline
	}
}