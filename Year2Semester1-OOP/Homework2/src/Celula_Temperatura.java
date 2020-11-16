/**
 * @author Chiru
 * Clasa care contine o celula de temperatura observata
 * In ea se stocheaza temperatura si timestampul la care a fost aceasta inregistrata la o intrare a sistemului
 */
public class Celula_Temperatura implements Comparable<Object> {
	double temp;
	long tstp;	
	
	//constructor vid, initializeaza temperatura si timestampul cu 0
	public Celula_Temperatura() {
		this.temp=0;
		this.tstp=0;
	}
	
	/**Constructor parametrizat cu
	 * @param temp = temperatura din celula
	 * @param stamp = timestampul la care s-a inregistrat temperatura respectiva
	 */
	public Celula_Temperatura(double temp, long stamp) {
		this.temp=temp;
		this.tstp=stamp;
	}
	
	/** Suprascriere a lui compareTo din interfata Comparable
	 *  putem spune ca o celula este mai mica, egala sau mai mare decat alta celula pe baza relatiei de egalitate dintre temperaturile lor
	 *  1= celula de unde se apeleaza compareTo este mai mare decat cea data ca parametru
	 *  0= sunt egale
	 *  -1= celula de unde se apeleaza este mai mica decat cea data ca parametru
	 */  	@Override
	public int compareTo(Object o) {
		if (this.temp > ((Celula_Temperatura)o).temp)
			return 1;
			else if ( this.temp < ((Celula_Temperatura)o).temp )
				return -1;
			else return 0;
	}
}
