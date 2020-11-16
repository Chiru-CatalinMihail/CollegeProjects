import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class main {
	
		public static void main(String[] args) throws IOException {
			File file = new File("therm.in"); 
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			String linereader=br.readLine();
			String[] bucati= linereader.split("[ \r\n]");
			int nr_argumente_prima_linie=bucati.length;
			short nr_camere;
			double temperatura_globala, umiditate_globala;
			long referinta;
			
			if((bucati.length>=3)&&(bucati.length<4)) {
				umiditate_globala=0;
				referinta=Long.parseLong(bucati[2]);
			}else {
				umiditate_globala=Double.parseDouble(bucati[2]);
				referinta=Long.parseLong(bucati[3]);
			}
			nr_camere=Short.parseShort(bucati[0]);
			temperatura_globala=Double.parseDouble(bucati[1]);
			Casa casa=new Casa();			
			
			//citeste fiecare camera si o adauga in ArrayListul de camere din casa
			for (int i = 0; i < nr_camere; i++) {
				linereader=br.readLine();
				bucati= linereader.split("[ \r\n]");
				String nume_camera=bucati[0];
				String device_id=bucati[1];
				short suprafata=Short.parseShort(bucati[2]);
				Camera camera_aux=new Camera(referinta, suprafata, nume_camera, device_id);
				casa.rooms.add(camera_aux);
			}

			//citeste comenzile, le prelucreaza si le executa corespunzator pana la sfarsitul fisierului de input
			while ((linereader = br.readLine()) != null) {
			  
				if(linereader.contains("TRIGGER")) {
				  int ok=0;
				  if (nr_argumente_prima_linie>=4) {
					  ok=casa.triggerh(umiditate_globala);
					  
					  /*instructiunea triggerh afecteaza doar pentru "NO" adica pentru ok=0
					  pentru ok=1 se parcurge trigger care stabileste final "YES" sau "NO" */
					  if(ok==0) {
						  casa.ajutor.append("NO\n");
						  continue;
					  }
				  }
				  
				  ok=casa.trigger(temperatura_globala);
				  if(ok==1) {
					  casa.ajutor.append("YES\n");
				  }else {
					  casa.ajutor.append("NO\n");
				  }
			  }
			  
			  if(linereader.contains("TEMPERATURE")) {
				  bucati=linereader.split("[ \r\n]");
				  if(bucati.length>1) {
					  temperatura_globala=Double.parseDouble(bucati[1]);
				  }
			  }
			  			  
			  /*am pus "OBSERVE_" fiindca si OBSERVEH accepta cuvantul cheie "OBSERVE",
			   insa aceasta trebuie executata doar pentru comanda "OBSERVEH"*/
			  if(linereader.contains("OBSERVE ")){
				  bucati= linereader.split("[ \r\n]");
				  if (bucati.length > 1) {
					  String device_id_obs=bucati[1];
					  long tmpstmp=Long.parseLong(bucati[2]);
					  Double temperatura=Double.parseDouble(bucati[3]);
					  Celula_Temperatura cel_temp= new Celula_Temperatura(temperatura, tmpstmp);
					  casa.observe(device_id_obs, cel_temp);
				  }
			  }
			  
			  if(linereader.contains("OBSERVEH")){
				  bucati= linereader.split("[ \r\n]");
				  if (bucati.length > 1) {
					  String device_id_obsh=bucati[1];
					  long tmpstmph=Long.parseLong(bucati[2]);
					  Double umiditate=Double.parseDouble(bucati[3]);
					  Celula_Umiditate cel_umid= new Celula_Umiditate(umiditate, tmpstmph);
					  casa.observeh(device_id_obsh, cel_umid);
				  }
			  }
			  
			  if(linereader.contains("LIST")){
				  bucati= linereader.split("[ \r\n]");
				  if (bucati.length > 1) {
					  String nume_camera=bucati[1];
					  long start_interval=Long.parseLong(bucati[2]);
					  long stop_interval=Long.parseLong(bucati[3]);
					  casa.list(nume_camera, start_interval, stop_interval);
				  }
			  }
			}
			br.close();
			
			//scrierea in fisier cu ajutorul lui StringBuilder folosit ca un buffer
			String scriitor=casa.ajutor.toString();
			casa.writer.write(scriitor); //scrie in fisierul de output informatia prelucrata
			casa.writer.close();			
		}
}



