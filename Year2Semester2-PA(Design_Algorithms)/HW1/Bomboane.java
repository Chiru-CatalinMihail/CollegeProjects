import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * @author Chiru Clasa corespunzatoare rezolvarii celei de-a treia probleme din
 *         tema
 */
public class Bomboane {
	static int kMod = 1000000007;

	public static void main(String[] args) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader("bomboane.in"));
			final PrintWriter printer = new PrintWriter(new FileWriter("bomboane.out"));

			String linereader = reader.readLine();
			String[] bucati = linereader.split(" ");
			int N = Integer.parseInt(bucati[0]), M = Integer.parseInt(bucati[1]);
			ArrayList<ArrayList<Integer>> dp = new ArrayList<ArrayList<Integer>>();

			/*
			 * Alocam pe heap o matrice NxM, care ne va ajuta sa aplicam programare dinamica
			 * asupra problemei, intelesul rezultatului stocat in dp[i][j] este "in cate
			 * moduri pot primii (i+1) copii j bomboane"
			 */
			for (int i = 0; i < N; i++) {
				dp.add(new ArrayList<Integer>());
				for (int j = 0; j <= M; j++) {
					dp.get(i).add(0);
				}
			}

			for (int i = 0; i < N; i++) {
				linereader = reader.readLine();
				bucati = linereader.split(" ");
				int start = Integer.parseInt(bucati[0]), stop = Integer.parseInt(bucati[1]);
				if (i == 0) {
					for (int j = start; j <= stop; j++) {
						/*
						 * Conditie pusa pentru a evita OOB, Gigel isi poate aminti ca un copil ar
						 * putea primii mai multe bomboane decat are el in total
						 */
						if (j == (M + 1)) {
							break;
						}

						/*
						 * Pentru primul copil, numarul de moduri in care poate primii bomboane
						 * este 1 si corespunde oricarui numar din intevalul amintit de Gigel
						 */
						dp.get(i).set(j, 1);
					}
				} else {
					/*
					 * Pentru i copii, numarul de moduri in care se pot primii bomboane depinde de
					 * felul in care au primit i-1 dupa formula urmatoare:
					 * 
					 * Numarul de moduri in care pot primii i copii, j bomboane este egal cu suma
					 * tuturor modurilor in care pot primi i-1 de la 0 la j bomboane cu conditia ca
					 * numarul de bomboane ramase (M - cat_au_primit_primii_i-1) sa apartina
					 * intervalului corespunzator celui de-al i-lea copil: start <= bomboane_ramase
					 * <= stop
					 */
					for (int j = 0; j <= M; j++) {
						int suma_crt = 0;
						for (int k = 0; k <= j; k++) {
							if ((start <= (j - k)) && ((j - k) <= stop)) {
								suma_crt = ((suma_crt % kMod) + (dp.get(i - 1).get(k) % kMod))
										% kMod;
							}
						}

						dp.get(i).set(j, suma_crt);
					}
				}
			}
			// Rezultatul final ne arata in cate moduri pot primii cei N copii M bomboane
			printer.print(dp.get(N - 1).get(M));
			printer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
