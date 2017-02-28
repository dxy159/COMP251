import java.util.*;

public class Kruskal{

    public static WGraph kruskal(WGraph g){

        /* Fill this method (The statement return null is here only to compile) */
    	
    	WGraph resultGraph = new WGraph();
    	ArrayList<Edge> edges = g.listOfEdgesSorted();		// Get list of edges
    	DisjointSets p = new DisjointSets(g.getNbNodes());		// Get a disjoint set for each node
    	
    	for (int i = 0; i < edges.size(); i++) {		// Iterate through each edge
    		Edge edge = edges.get(i);

    		if (IsSafe(p, edge)) {				// If safe, add edge to resulting graph
    			resultGraph.addEdge(edge);
    		}
    	}
        
        return resultGraph;
    }

    public static Boolean IsSafe(DisjointSets p, Edge e){

        /* Fill this method (The statement return 0 is here only to compile) */
    	int node1 = e.nodes[0];
    	int node2 = e.nodes[1];
        if (p.find(node1) == p.find(node2)) {			// If node1 and node2 are in the same set, return false
        	return Boolean.FALSE;
        }
        
        p.union(node1, node2);				// Union the two nodes and return true
        return Boolean.TRUE;
    
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        WGraph t = kruskal(g);
        System.out.println(t);

   } 
}