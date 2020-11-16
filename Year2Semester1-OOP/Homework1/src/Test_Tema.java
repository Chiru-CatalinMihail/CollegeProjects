import java.io.*;
public class Test_Tema {

	public static void main(String[] args) throws Exception {
		File file = new File("queue.in"); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		String linereader=br.readLine();
		int nr_persoane=Integer.parseInt(linereader); //citeste numarul de persoane care vor fi transmise ulterior
		
		//initiaza vectorul de grupuri si heapul
		VectorDeGrupuri vect_groups= new VectorDeGrupuri(nr_persoane);
		CoadaDePrioritate prio_heap=new CoadaDePrioritate(nr_persoane);
		
		//citeste fiecare persoana si creeaza structura specifica ei
		for (int i = 0; i < nr_persoane; i++) {
			linereader=br.readLine();
			String[] bucati= linereader.split("[ \r\n]");
			String nume_grup_origine=bucati[0];
			String nume=bucati[1];
			int varsta=Integer.parseInt(bucati[2]);
			char tip_bilet=bucati[3].charAt(0);
			boolean imbarcare_prioritara=Boolean.parseBoolean(bucati[4]);
			boolean nevoi_speciale=Boolean.parseBoolean(bucati[5]);
			Persoana aux_person= new Persoana(nume_grup_origine, nume, varsta, tip_bilet, imbarcare_prioritara, nevoi_speciale);
			vect_groups.adaugaPersoanaInVector(aux_person);
		}
		
		//calculeaza punctajele pentru toate GrupurileGenerale create conform persoanelor
		for (int i = 0; i < vect_groups.index_grupuri; i++) {
			vect_groups.vector_grupuri[i].calculeazaPunctajInstantiat();
		}
		
		//citeste comenzi, le prelucreaza si le executa corespunzator (insert/embark/list/delete) pana la sfarsitul fisierului de input
		while ((linereader = br.readLine()) != null) {
		  if(linereader.equals("embark")) {
			  prio_heap.embark();
		  }
		  if(linereader.equals("list")) {
			  prio_heap.list();
		  }
		  if(linereader.contains("insert")){
			  String[] bucati= linereader.split("[ \r\n]");
			  if (bucati.length > 1) {
				  String nume_grup_de_adaugat=bucati[1];
				  int index_ajutator=vect_groups.cautaGrupInVector(nume_grup_de_adaugat);
				  prio_heap.insert(vect_groups.vector_grupuri[index_ajutator], vect_groups.vector_grupuri[index_ajutator].punctaj);
				  }
		  } else {
			  String[] bucati= linereader.split("[ \r\n]");
			  if (bucati.length > 1) {
				  String nume_grup_de_sters=bucati[1];
				  if(bucati.length<=2) {
					  int index_ajutator=vect_groups.cautaGrupInVector(nume_grup_de_sters);
					  prio_heap.delete(vect_groups.vector_grupuri[index_ajutator]);
				 } else {
					  int index_ajutator=vect_groups.cautaGrupInVector(nume_grup_de_sters);
					  String nume_persoana_stearsa=bucati[2];
					  prio_heap.deletePersoana(vect_groups.vector_grupuri[index_ajutator], nume_persoana_stearsa);
					 }
			}
		  }
		}
		br.close();
		
		//scrierea in fisier cu ajutorul lui StringBuilder folosit ca un buffer
		if (prio_heap.ajutor.length()!=0) {
			prio_heap.ajutor=prio_heap.ajutor.deleteCharAt(prio_heap.ajutor.length()-1); //sterge ultimul newline
		}
		String scriitor=prio_heap.ajutor.toString();
		prio_heap.writer.write(scriitor); //scrie in fisierul de output informatia prelucrata
		prio_heap.writer.close();
	}
}
