import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Chiru Clasa ce implementeaza o pereche caracterizata de start
 *         (inceputul gardului) si stop (sfarsitul gardului) Totodata suprascrie
 *         metoda de comparare pentru a ordona perechile crescator dupa inceput
 *         si descrescator dupa sfarsit
 */
class Pair implements Comparable<Pair> {
	int start;
	int end;

	public Pair(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public int compareTo(Pair p2) {

		/*
		 * In cazul in care 2 intervale sunt la egalitate vom ordona descrecator dupa
		 * lungimea de sfarsit a gardului
		 */
		if (this.start == p2.start) {
			if (this.end < p2.end) {
				return 1;
			} else {
				return -1;
			}
		}

		if (this.start > p2.start) {
			return 1;
		} else {
			return -1;
		}
	}

}

/**
 * @author Chiru Clasa corespunzatoare rezolvarii celei de-a doua probleme din
 *         tema
 */
public class Gard {

	public static void main(String[] args) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader("gard.in"));
			final PrintWriter printer = new PrintWriter(new FileWriter("gard.out"));

			String linereader = reader.readLine();
			String[] bucati = linereader.split(" ");
			ArrayList<Pair> garduri = new ArrayList<Pair>();

			int nr_bucati_de_gard = Integer.parseInt(bucati[0]);
			for (int i = 0; i < nr_bucati_de_gard; i++) {
				linereader = reader.readLine();
				bucati = linereader.split(" ");
				garduri.add(new Pair(Integer.parseInt(bucati[0]), Integer.parseInt(bucati[1])));
			}

			Collections.sort(garduri);
			int current_end = garduri.get(0).end;
			int redundats = 0;

			for (int i = 1; i < nr_bucati_de_gard; i++) {

				Pair crt_pair = garduri.get(i);

				/*
				 * Un gard este redundant daca este cuprins intre startul si endul altui gard
				 * Din felul in care am realizat functia compareTo gardurile sunt ordonate
				 * crescator dupa inceput si descrescator dupa sfarsit, in acest sens, daca avem
				 * 2 garduri, unul de lungime [4,100] celalalt de lungime [4,6] current_end va
				 * fi 100 si il va numara drept redundant pe [4,6]
				 */
				if (crt_pair.end > current_end) {
					current_end = crt_pair.end;
				} else {
					redundats++;
				}
			}
			printer.print(redundats);
			printer.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}