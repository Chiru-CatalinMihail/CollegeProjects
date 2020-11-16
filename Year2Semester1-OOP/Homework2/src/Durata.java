/**
 * @author Chiru
 * Clasa care incapsuleaza un interval de timp intre o margine inferioara si o margine superioara
 */
public class Durata {
	long stamp_inf; //marginea inferioara
	long stamp_sup; //marginea superioara
	
	//constructor vid
	public Durata() {
		this.stamp_inf=0;
		this.stamp_sup=0;
	}
	
	/**Constructor parametrizat
	 * @param stamp_inf = marginea inferioara a duratei
	 * @param stamp_sup = marginea superioara a duratei
	 */
	public Durata(long stamp_inf, long stamp_sup) {
		this.stamp_inf=stamp_inf;
		this.stamp_sup=stamp_sup;
	}
}
