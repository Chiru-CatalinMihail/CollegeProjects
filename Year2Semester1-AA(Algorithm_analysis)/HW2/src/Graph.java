import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Chiru
 * Clasa ce implementeaza un graph neorientat cu metode statice specifice
 * Si metodele modulare necesare reducerii in timp polinomial a HCP la SAT
 */
public class Graph {
	private int V;
	private Integer adjMatrix[][];

	// constructor
	private Graph(int V) {
		this.V = V + 1;	//consideram V=V+1 pentru a porni indexarea nodurilor de la 1, si nu de la 0
		this.adjMatrix = new Integer[this.V][this.V];

		for (int i = 1; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				if (i==j) {
					adjMatrix[i][i]=-1;
				}else {
					adjMatrix[i][j]=0;
				}
			}
		}
	}
	
	/**Adauga muchiile in graph
	 * @param graph
	 * @param src indexul nodului de unde porneste muchia
	 * @param dest indexul nodului unde ajunge aceasta
	 */
	private static void addEdge(Graph graph, int src, int dest) {
		// Adauga muchie intre sursa si destinatie
		graph.adjMatrix[src][dest]=1;

		// Fiind neorientat adauga si intre destinatie si sursa
		graph.adjMatrix[dest][src]=1;
	}

	/**Metoda care prelucreaza inputul si intoarce graful cu muchiile precizate 
	 * @param br
	 * @return intoarce graphul descris de fisierul din input
	 * @throws IOException
	 */
	public static Graph generateGraphFromInput(BufferedReader br) throws IOException {
		if (br == null) {
			return null;
		}

		String linereader = br.readLine();
		Graph graph = new Graph(Integer.parseInt(linereader));
		while (!(linereader = br.readLine()).contentEquals("-1")) {
			String[] bucati = linereader.split("[ ]");
			addEdge(graph, Integer.parseInt(bucati[0]), Integer.parseInt(bucati[1]));
		}
		return graph;
	}
	
	/**Metoda care intoarce numarul de vecini al unui nod dat
	 * @param graph
	 * @param vertex_number numarul nodului transmis
	 * @return numarul de vecini cu care este conectat nodul dat
	 */
	private static int numberOfNeighbours(Graph graph, int vertex_number) {
		int nr_vecini=0;
		for (int i = 1; i < graph.adjMatrix.length; i++) {
			if (graph.adjMatrix[vertex_number][i]==1) {
				++nr_vecini;
			}
		}
		return nr_vecini;
	}
	
	/**Metoda care stabileste grosier daca graful are sau nu un ciclu hamiltonian
	 * Daca oricare dintre noduri nu are cel putin 2 vecini, graful nu poate avea ciclu hamiltonian
	 * @param graph
	 * @return valoarea de adevar a afirmatiei "hasNoCycle"
	 */
	private static boolean hasNoCycle(Graph graph) {
		for (int i = 1; i < graph.adjMatrix.length; i++) {
			if (Graph.numberOfNeighbours(graph, i)<2) {
				return true;
			}
		}
		return false;
	}
	
	/**Scrie sub forma de expresie booleana conectarea unui nod la alte noduri conform transformarii
	 * Daca nodul are doar 2 muchii se scriu cele 2 muchii, daca are mai mult de 2 muchii se scriu
	 * combinari de cate muchii are luate cate 2, in care sunt negate toate cu exceptia a cate 2 muchii
	 * (ajuta la timpul polinomial al transformarii)
	 * @param graph
	 * @param str_builder
	 * @param vertex_number
	 */
	private static void writeNodeConnections2SAT(Graph graph, StringBuilder str_builder, int vertex_number) {
		if(Graph.numberOfNeighbours(graph, vertex_number)==2) {
			str_builder.append("((");
			for (int i = 1; i < graph.adjMatrix.length; i++) {
				if(graph.adjMatrix[vertex_number][i]==1) {
					str_builder.append("x"+vertex_number+"-"+i+"&");
				}
			}
			str_builder.deleteCharAt(str_builder.length()-1);
			str_builder.append("))&");
		}else {
			str_builder.append("(");
			ArrayList<Integer> vecini= new ArrayList<Integer>();
			vecini.add(-1);
			
			for (int i = 1; i < graph.adjMatrix.length; i++) {
					if (graph.adjMatrix[vertex_number][i]==1) {
						vecini.add(i);
					}
			}
			
			for (int h = 1; h < vecini.size()-1; h++) {
				for (int i = h+1; i < vecini.size(); i++) {
					str_builder.append("(x"+vertex_number+"-"+vecini.get(h)
					+"&x"+vertex_number+"-"+vecini.get(i)+"&");
					for (int j = 1; j < vecini.size(); j++) {
						if ((j!=h)&&(j!=i)) {
							str_builder.append("~x"+vertex_number+"-"+vecini.get(j)+"&");
						}
					}
					str_builder.deleteCharAt(str_builder.length()-1);
					str_builder.append(")|");
				}
			}
			str_builder.deleteCharAt(str_builder.length()-1);				
			str_builder.append(")&");
		}
	}
	
	/**Metoda care converteste la SAT faptul ca 
	 * 		la orice nod trebuie sa se ajunga in de la 1 la maxim N/2+1 pasi
	 * @param graph
	 * @param str_builder
	 * @param vertex_number
	 */
	private static void writePossibleDistances2SAT(Graph graph, StringBuilder str_builder, int vertex_number) {
		str_builder.append("(");
		for (int i = 1; i <= (graph.V-1)/2+1; i++) {
			str_builder.append("a"+i+"-"+vertex_number+"|");
		}
		str_builder.deleteCharAt(str_builder.length()-1);
		str_builder.append(")&");

	}
	
	/**Verificare SAT ca Graful este neorientat
	 * @param graph
	 * @param str_builder
	 */
	private static void writeTautologies(Graph graph, StringBuilder str_builder) {
		//trebuie mereu sa obtinem 1 daca x_i-j=x_j-i, conditia de graf neorientat
		for (int i = 1; i < graph.adjMatrix.length; i++) {
			for (int j = i+1; j < graph.adjMatrix.length; j++) {
				if (graph.adjMatrix[i][j]==1) {
					str_builder.append("((x"+i+"-"+j+"|~x"+j+"-"+i+")&(~x"+i+"-"+j+"|x"+j+"-"+i+"))&");
				}
			}
		}
	}
	
	private static void writePathLengthsFromStartingVertex(Graph graph, StringBuilder str_builder) {
		//nu se poate ajunge de la nodul 1 la un nod cu cost 1 decat daca exista muchie directa
		for (int j = 2; j < graph.adjMatrix.length; j++) {
			if (graph.adjMatrix[1][j]==1) {
				str_builder.append("((a1-"+j+"|~x1-"+j+")&(~a1-"+j+"|x1-"+j+"))&");
			}
		}
		
		//ne asiguram ca restrictionam nodurile cu care 1 nu are muchie
		for (int j = 1; j < graph.adjMatrix.length; j++) {
			if (graph.adjMatrix[1][j]!=1) {
				str_builder.append("~a1-"+j+"&");
			}
		}		
	}
	
	/**Verificare scrisa boolean care arata daca se poate ajunge la orice alt nod din graf
	 *  in numarul restrictiv de pasi <= N/2+1 avand in vedere vecinii nodului de start si cat
	 *  ia de la acestia sa se ajunga in celelalte noduri
	 *  Restrictia aceasta (ca oriunde sa se ajunga in N/2+1 pasi) reprezinta cheia reducerii polinomiale
	 *  	catre SAT, limitand favorabil timpul.
	 * @param graph
	 * @param str_builder
	 */
	private static void writePahtsFromEachNodeToEverywhere(Graph graph, StringBuilder str_builder) {
		for (int i = 2; i <= (graph.V-1)/2+1; i++) {
			for (int j = 2; j < graph.adjMatrix.length; j++) {
				str_builder.append("((a"+i+"-"+j+"|~((");
				for (int j2 = 2; j2 < graph.adjMatrix.length; j2++) {
					if (graph.adjMatrix[j][j2]==1) {
						str_builder.append("(a"+(i-1)+"-"+j2+"&x"+j2+"-"+j+")|");
					}
				}
				str_builder.deleteCharAt(str_builder.length()-1);
				str_builder.append(")&~(");
				for (int k = 1; k < i; k++) {
					str_builder.append("a"+k+"-"+j+"|");
				}
				str_builder.deleteCharAt(str_builder.length()-1);
				str_builder.append(")))&(~a"+i+"-"+j+"|((");
				for (int j2 = 2; j2 < graph.adjMatrix.length; j2++) {
					if (graph.adjMatrix[j][j2]==1) {
						str_builder.append("(a"+(i-1)+"-"+j2+"&x"+j2+"-"+j+")|");
					}
				}
				str_builder.deleteCharAt(str_builder.length()-1);
				str_builder.append(")&~(");
				for (int k = 1; k < i; k++) {
					str_builder.append("a"+k+"-"+j+"|");
				}
				str_builder.deleteCharAt(str_builder.length()-1);
				str_builder.append("))))&");
			}
		}
	}
	
	/**Metoda care scrie modularizat reducerea polinomiala HCP <= p SAT
	 * @param graph
	 * @param str_builder
	 */
	public static void HCP2pSAT(Graph graph, StringBuilder str_builder) {
		if (graph.V-1 == 1) { // graful trivial cu un singur nod
			str_builder.append("x1-1 | ~x1-1\n");
			return;
		}
		
		/*daca graful nu respecta conditia minima de cycle
		se poate inlocui instantaneu cu o expresie permanent nula*/
		if (Graph.hasNoCycle(graph)) {
			str_builder.append("x1-1 & ~x1-1");
			return;
		}
		
		Graph.writeNodeConnections2SAT(graph, str_builder, 1);
		for (int i = 2; i < graph.adjMatrix.length; i++) {
			Graph.writeNodeConnections2SAT(graph, str_builder, i);
			Graph.writePossibleDistances2SAT(graph, str_builder, i);
		}
		Graph.writeTautologies(graph, str_builder);
		Graph.writePathLengthsFromStartingVertex(graph, str_builder);
		Graph.writePahtsFromEachNodeToEverywhere(graph, str_builder);
		str_builder.deleteCharAt(str_builder.length()-1);
	}
}