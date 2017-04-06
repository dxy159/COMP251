import java.io.*;
import java.util.*;




public class FordFulkerson {

	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		/* YOUR CODE GOES HERE
		//
		//
		//
		//
		//
		//
		//
		*/
		// Initialize path and visited arraylists 
        ArrayList<Integer> path = new ArrayList<>();
        ArrayList<Integer> visited = new ArrayList<>();

        visited.add(source);
        path.add(source);

        // Using a stack, run the algorithm while there are still valid paths
        while(!path.isEmpty()){
        	
        	// Get the last element in the path stack and add it to visited
        	int sizePath = path.size() - 1;
            Integer a = path.get(sizePath);
            path.remove(sizePath);
            visited.add(a);
            
            // Get the last element of Stack, and get the edge, remove top if edge is null or weight is 0
            while(!Stack.isEmpty()){
            	int stackSize = Stack.size() - 1;
                Integer b = Stack.get(stackSize);
                Edge edge = graph.getEdge(b, a);
                
                if(edge == null || edge.weight == 0) {
                    Stack.remove(stackSize);
                } else {
                    break;
                }
            }
            
            // Now add a to Stack
            Stack.add(a);
            
            // Loop through every edge, and add every node that is valid to path stack
            for(Edge edge: graph.getEdges()) {
            	
                if((edge.nodes[0] == a && edge.weight > 0) && (!visited.contains(edge.nodes[1]))){
                	// If the end node is destination, clear the path so algorithm terminates
                    if(edge.nodes[1] == destination){
                        Stack.add(destination);
                        path.clear();
                    } else {
                        path.add(edge.nodes[1]);
                    }
                }
            }
        }

        return Stack;
    }
	
	
	
	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
		String answer="";
		String myMcGillID = "260674646"; //Please initialize this variable with your McGill ID
		int maxFlow = 0;

		/* YOUR CODE GOES HERE
//
//
//
//
//
//
//
*/
		// Create a residual and capacity graph with initial values
		WGraph residual = new WGraph(graph);
		WGraph capacity = new WGraph(graph);
		
		// Set all edge weights to 0
		for(Edge edge: graph.getEdges()){
			graph.setEdge(edge.nodes[0], edge.nodes[1], 0);
		}
		
		// If source and destination are both in pathDFS, then it is successful
		while(pathDFS(source, destination, residual).contains(source) && pathDFS(source, residual.getDestination(), residual).contains(destination)){
			ArrayList<Integer> dfs = pathDFS(source, destination, residual);
			int bottleneck = Integer.MAX_VALUE;
			
			// Find the bottleneck flow
			for(int i = 0; i < dfs.size()-1; i++){
				Edge edge = residual.getEdge(dfs.get(i), dfs.get(i+1));
				if(edge != null && edge.weight < bottleneck){
					bottleneck = edge.weight;
				}
			}
			// Augment the max flow
			for(int i = 0; i < dfs.size() - 1; i++){
				Integer n1 = dfs.get(i);
				Integer n2 = dfs.get(i + 1);
				Edge edge = graph.getEdge(n1, n2);
				if(edge != null){
					graph.setEdge(n1, n2, edge.weight + bottleneck);
				}
				else{
					graph.setEdge(n1, n2, edge.weight - bottleneck);
				}
			}
			
			// Set residual graph
			for(int i=0; i<dfs.size()-1; i++){
				Integer n1 = dfs.get(i);
				Integer n2 = dfs.get(i + 1);
				Edge edge = graph.getEdge(n1, n2);
				Edge capEdge = capacity.getEdge(n1, n2);
				
				if(edge.weight <= capEdge.weight){
					residual.setEdge(n1, n2, capEdge.weight - edge.weight);
				} else if (edge.weight > 0) {
					Edge residualEdge = residual.getEdge(n1, n2);
					if(residualEdge == null){
						Edge backEdge = new Edge(n1, n2, edge.weight);
						residual.addEdge(backEdge);
					}
					else{
						residual.setEdge(n2, n1, edge.weight);
					}
				}
				
			}
			
			maxFlow += bottleneck;
			bottleneck = Integer.MAX_VALUE;
		}
		
		
		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
	}
	
	
	public static void writeAnswer(String path, String line){
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it
		
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line+"\n");	
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
	 }
}