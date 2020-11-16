/**
 * @author Chiru
 * Similar cu Celula_Temperatura, clasa care contine o celula de umiditate observata
 * In ea se stocheaza umiditatea si timestampul la care a fost aceasta inregistrata la o intrare a sistemului
 */
public class Celula_Umiditate implements Comparable<Object> {
	double umid;
	long tstp;
	
	//constructor vid, initializeaza cu 0 datele clasei
	public Celula_Umiditate() {
		this.umid=0;
		this.tstp=0;
	}
	
	/**Constructor parametrizat
	 * @param umid = umiditatea observata
	 * @param stamp = stampul la care s-a observat
	 */
	public Celula_Umiditate(double umid, long stamp) {
		this.umid=umid;
		this.tstp=stamp;
	}
	
	/** Suprascriere a lui compareTo din interfata Comparable
	 *  O celula se compara cu o alta pe baza relatiei de egalitate dintre umiditatile lor
	 *  1= celula de unde se apeleaza compareTo este mai mare decat cea data ca parametru
	 *  0= sunt egale
	 *  -1= celula de unde se apeleaza este mai mica decat cea data ca parametru
	 */ @Override
	public int compareTo(Object o) {
		if (this.umid > ((Celula_Umiditate)o).umid)
			return 1;
			else if ( this.umid < ((Celula_Umiditate)o).umid )
				return -1;
			else return 0;
	}	
}
