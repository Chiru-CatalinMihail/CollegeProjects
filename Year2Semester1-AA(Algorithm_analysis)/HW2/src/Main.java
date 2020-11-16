import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {

	public static void main(String[] args) {
		File file = new File("graph.in"); 
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			Graph graph= Graph.generateGraphFromInput(br);	//se citeste graphul din input
			br.close();
			File file_out= new File("bexpr.out");
			BufferedWriter buffered_writer = new BufferedWriter(new FileWriter(file_out));
			StringBuilder str_builder= new StringBuilder();
			Graph.HCP2pSAT(graph, str_builder);				//se scrie reducerea polinomiala la SAT in output
			buffered_writer.write(str_builder.toString());
			buffered_writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
