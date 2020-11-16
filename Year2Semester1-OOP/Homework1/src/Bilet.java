
/**
 * @author Chiru
 * pentru a gestiona tema intr-o maniera orientata pe obiecte m-am gandit ca biletul este in sine un obiect care are un punctaj in functie de tipul sau
 */
public class Bilet {
	char tip_bilet;
	
	//constructorul vid
	public Bilet(){}
	
	/** constructorul ce initiaza litera corespunzatoare tipului de bilet
	 * @param litera corespunzatoare tipului de bilet (b-business, p-premium, e-economic)
	 */
	public Bilet(char litera){
		this.tip_bilet=litera;
	}
	
	/**
	 * @return intoarce punctajul in functie de tipul biletului
	 */
	public int getPunctajBilet(){
		if(this.tip_bilet=='b')
			return 35;
		if(this.tip_bilet=='p')
			return 20;
		if(this.tip_bilet=='e')
			return 0;
		
		return -1;
	}
}
