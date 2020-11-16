import java.util.ArrayList;

/**
 * @author Chiru
 * Clasa contine numele care contine numele camerei, numele dispozitivului, suprafata incaperii
 * 			si 2 liste ce contin 24 de intervale de cate o ora fata de timestampul de referinta in care
 * 			sunt pastrate temperaturile si umiditatile inregistrate in aceasta perioada
 */
public class Camera {
	ArrayList<IntervalOrar<Celula_Temperatura>> date_temp; //lista de 24 de ore in care sunt tinute datele despre temperatura
	ArrayList<IntervalOrar<Celula_Umiditate>> date_umid;   //lista de 24 de ore in care sunt tinute datele despre umiditate
	short surface; //suprafata camerei
	String nume_camera; 
	String nume_device;
	
	//constructor vid
	public Camera() {}
	
	/**
	 * Constructor care initializeaza complet camera, creand cele doua liste de 24 de ore pentru temperatura si umiditate
	 * @param timp_de_ref
	 * @param surface
	 * @param nume_camera
	 * @param nume_device
	 */
	public Camera(long timp_de_ref, short surface, String nume_camera, String nume_device) {
		this.date_temp= new ArrayList<IntervalOrar<Celula_Temperatura>>();
		this.date_umid= new ArrayList<IntervalOrar<Celula_Umiditate>>();
		this.surface=surface;
		this.nume_camera=nume_camera;
		this.nume_device=nume_device;
		
		/*pentru a avea o lista cu stampuri ordonate crescator folosim iteratorul pentru a scadea
		si a obtine intervalele orare de la prima ora, la ultima (cea mai recenta) al carui prag
		superior este timpul de referinta*/
		for (int i = 0; i < 24; i++) {
			Durata interval_orar= new Durata(timp_de_ref-3600*(24-i), timp_de_ref-3600*(23-i));
			IntervalOrar<Celula_Temperatura> ora_temp=new IntervalOrar<Celula_Temperatura>(interval_orar);
			IntervalOrar<Celula_Umiditate> ora_umiditate=new IntervalOrar<Celula_Umiditate>(interval_orar);
			this.date_temp.add(ora_temp);
			this.date_umid.add(ora_umiditate);
		}
	}
}
