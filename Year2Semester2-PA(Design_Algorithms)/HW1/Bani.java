import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Chiru Clasa corespunzatoare rezolvarii primei probleme din tema
 */
public class Bani {

	static int kMod = 1000000007;

	/**
	 * @param instructiune  poate fi de tipul 1 sau 2 conform cerintei
	 * @return numarul de variante in care pot fi asezate numarBancnote bancnote
	 */
	private static long getResult(int instructiune, long numarBancnote) {

		/*
		 * Abordare DP, numarul de variante pentru o bancnota curenta este dat de
		 * celelate bancnote de la pasul anterior
		 */
		if (instructiune == 2) {
			long variante10lei = 1, variante50lei = 1, variante100lei = 1;
			long variante200lei = 1, variante500lei = 1;
			long var_crt_10lei = 0, var_crt_50lei = 0, var_crt_100lei = 0;
			long var_crt_200lei = 0, var_crt_500lei = 0;
			for (int i = 1; i < numarBancnote; i++) {
				var_crt_10lei = (((variante50lei % kMod) + (variante100lei % kMod)) % kMod
						+ (variante500lei % kMod)) % kMod;
				var_crt_50lei = ((variante10lei % kMod) + (variante200lei % kMod)) % kMod;
				var_crt_100lei = (((variante10lei % kMod) + (variante100lei % kMod)) % kMod
						+ (variante200lei % kMod)) % kMod;
				var_crt_200lei = ((variante50lei % kMod) + (variante500lei % kMod)) % kMod;
				var_crt_500lei = variante200lei % kMod;

				variante10lei = var_crt_10lei;
				variante50lei = var_crt_50lei;
				variante100lei = var_crt_100lei;
				variante200lei = var_crt_200lei;
				variante500lei = var_crt_500lei;
			}

			return (((((variante10lei % kMod) + (variante50lei % kMod)) % kMod
					+ (variante100lei % kMod)) % kMod + (variante200lei % kMod)) % kMod
					+ (variante500lei % kMod)) % kMod;

		} else {
			/*
			 * Prima bancnota poate fi de oricare din cele 5 tipuri Dupa aceea oricare
			 * bancnota aleasa va genera 2 posibile branchuri la nivelul urmator Ceea ce
			 * inseamna ca pentru tipul de instructiune 1, formula care ofera numarul de
			 * posibile aranjari este 5*2^(NumarDeBancnote-1)
			 */
			return 5 * fast_pow(2, numarBancnote - 1) % kMod;
		}
	}

	/*
	 * Varianta de ridicarea unei baze la o putere folosind tactica Divide et Impera
	 * Deoarece testele de dimensiune mare (~= 10^9) nu pot returna solutia in 2
	 * secunde, folosind aceasta functie coboram complexitatea de la O(N) la O(log
	 * N)
	 *
	 * @param base = baza de ridicat la o putere
	 * 
	 * @param exponent = puterea la care dorim sa ridicam
	 * 
	 * @return rezultatul ridicarii la putere
	 */
	private static long fast_pow(int base, long exponent) {
		if (exponent == 1) {
			return base % kMod;
		}

		if (exponent == 0) {
			return 1;
		}

		long result = 1;
		long x = fast_pow(base, exponent / 2);
		if (exponent % 2 == 0) {
			result = 1L * x * x;
		} else {
			result = 1L * x * x * base;
		}

		return result % kMod;
	}

	public static void main(String[] args) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader("bani.in"));
			PrintWriter printer = new PrintWriter(new FileWriter("bani.out"));

			String linereader = reader.readLine();
			String[] bucati = linereader.split(" ");

			int instructiune = Integer.parseInt(bucati[0]);
			long numarBancnote = Long.parseLong(bucati[1]);
			printer.print(getResult(instructiune, numarBancnote));
			printer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}