import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * @author Chiru Clasa care caracterizeaza o gantera prin valoare(greutate) si
 *         numar de repetari corespunzator Totodata implementeaza suprascrierea
 *         metodei compareTo pentru a putea sorta ganterele descrescator dupa
 *         valoare si in caz de egalitate dupa numarul de repetari
 */
class Gantera implements Comparable<Gantera> {
	int valoare;
	int repetari;

	public Gantera(int valoare, int repetari) {
		this.valoare = valoare;
		this.repetari = repetari;
	}

	/**
	 *
	 */
	@Override
	public int compareTo(Gantera g2) {

		/*
		 * In cazul in care 2 valori sunt la egalitate vom ordona descrecator dupa
		 * numarul de repetari
		 */
		if (this.valoare == g2.valoare) {
			if (this.repetari < g2.repetari) {
				return 1;
			} else {
				return -1;
			}
		}

		if (this.valoare < g2.valoare) {
			return 1;
		} else {
			return -1;
		}
	}
}

/**
 * @author Chiru Clasa corespunzatoare rezolvarii celei de-a patra probleme
 *         (Bonus) din tema
 */
public class Sala {

	public static void main(String[] args) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader("sala.in"));
			final PrintWriter printer = new PrintWriter(new FileWriter("sala.out"));

			String linereader = reader.readLine();
			String[] bucati = linereader.split(" ");
			int N = Integer.parseInt(bucati[0]), M = Integer.parseInt(bucati[1]);
			ArrayList<Gantera> gantere = new ArrayList<Gantera>();
			long suma_maxima = -1;

			for (int i = 0; i < N; i++) {
				linereader = reader.readLine();
				bucati = linereader.split(" ");
				gantere.add(new Gantera(Integer.parseInt(bucati[0]), Integer.parseInt(bucati[1])));
			}

			Collections.sort(gantere);

			long nr_repetari = 0;

			PriorityQueue<Integer> min_heap = new PriorityQueue<Integer>();

			/*
			 * Avand vectorul deja sortat alegem ca primele <=M gantere sa fie cea mai buna
			 * optiune pentru Gigel
			 */
			for (int i = 0; i < M; i++) {
				Gantera crt = gantere.get(i);
				min_heap.add(crt.repetari);
				nr_repetari += crt.repetari;

				/*
				 * Tratam posibilitatea ca alternativa optima sa fie data de un nr mai mic de M
				 * gantere
				 * 
				 * Valoarea minima din M necesara pentru formula va fi intotdeauna valoarea
				 * curenta, deoarece vectorul este sortat descrescator
				 */
				if ((crt.valoare * nr_repetari) > suma_maxima) {
					suma_maxima = crt.valoare * nr_repetari;
				}
			}

			/*
			 * Verificam ajutandu-ne de min_heap daca exista optiuni mai avantajoase care sa
			 * produca un scor mai bun
			 */
			for (int i = M; i < N; i++) {
				Gantera crt = gantere.get(i);

				/*
				 * Este redundant sa eliminam radacina pentru a introduce ceva ce nu este
				 * avantajos si care va fi eliminat la urmatoarea iteratie producand un scor mai
				 * slab
				 */
				if (min_heap.peek() > crt.repetari) {
					continue;
				}
				nr_repetari -= min_heap.peek();
				min_heap.poll();
				min_heap.add(crt.repetari);
				nr_repetari += crt.repetari;

				if ((crt.valoare * nr_repetari) > suma_maxima) {
					suma_maxima = crt.valoare * nr_repetari;
				}
			}

			printer.print(suma_maxima);
			printer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}