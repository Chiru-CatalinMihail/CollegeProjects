

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * @author Chiru
 * Mainul Temei 3
 */
public class Main {
	public static void main(String[] args) {
		StringBuilder storer = new StringBuilder(); //StringBuilder cu ajutorul caruia se va scrie outputul
		File file_with_teams = new File(args[1]);
		FactorySingleton unique_instance=FactorySingleton.getinstance();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file_with_teams));
			String linereader;
			ArrayList<Team> lista_echipe= new ArrayList<Team>();
			/*Array de stringuri analog celui care pastreaza echipele
			  necesar la taskul 2 pentru a adauga echipele in competitie*/
			ArrayList<String> numele_echipelor= new ArrayList<String>();
			int index_echipa_curenta=0; //indexul echipei curente, il utilizez pentru taskul 1
			
			/*while de prelucrare a inputului referitor la echipe
			  acest while este necesar atat pentru "inscriere", cat si pentru "competitie"*/
			while ((linereader = br.readLine()) != null) {
				String[] bucati= linereader.split("[,\r\n]");
				lista_echipe.add(unique_instance.factory.createTeam(bucati[0], bucati[1].strip(),
										bucati[2].strip(), Integer.parseInt(bucati[3].strip())));
				numele_echipelor.add(bucati[1].strip());
				
				//adauga pe rand jucatorii din input la echipa lor corespunzatoare
				for (int i = 0; i < lista_echipe.get(index_echipa_curenta).getNumberOfPlayers(); i++) {
					linereader=br.readLine();
					
					bucati= linereader.split("[,\r\n]");
					lista_echipe.get(index_echipa_curenta).addPlayer(new Player(bucati[0], Integer.parseInt(bucati[1].strip())));
				}
				index_echipa_curenta++;
			}
			br.close();
			
			//Afiseaza in storer fiecare echipa cu jucatorii si caracteristicile sale
			if (args[0].contentEquals("inscriere")) {
				for (int i = 0; i < lista_echipe.size(); i++) {
					lista_echipe.get(i).showTeam(storer);
				}
				storer.deleteCharAt(storer.length()-1);
			}
			
			//taskul 2
			if (args[0].contentEquals("competitie")) {
				File file_with_the_competition = new File(args[2]);
				br = new BufferedReader(new FileReader(file_with_the_competition));
				linereader=br.readLine();
				String[] bucati= linereader.split("[,\r\n]");
				/*creeaza competitia, adauga la aceasta fiecare echipa din input si dupa terminarea
				  campionatului afiseaza in storer podiumul si locul ocupat de fiecare echipa*/
				Competition liga=new Competition(bucati[0], bucati[1].strip());
				while ((linereader = br.readLine()) != null) {
					liga.addInCompetition(lista_echipe.get(numele_echipelor.indexOf(linereader.strip())));					
				}
				liga.afisareClasament(storer);
			}
			
			//scrierea in fisierul de output cu ajutorul lui storer
			File file_in= new File(args[3]);
			FileWriter in_file_writer= new FileWriter(file_in);
			BufferedWriter writer= new BufferedWriter(in_file_writer);
			writer.write(storer.toString());
			writer.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		} 		
	}
}
