import java.util.ArrayList;
import java.util.Collections;

public class BellmanFord {

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws Exception{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         */
    	
    	// Initialize all the properties
    	int numNodes = g.getNbNodes();
        this.distances = new int[numNodes];
        this.predecessors = new int[numNodes];
        this.source = source;

        // Store all the nodes in an arraylist
        ArrayList<Integer> allNodes = new ArrayList<>(numNodes);
        for(Edge edge : g.getEdges()){
            allNodes.add(edge.nodes[0]);
            allNodes.add(edge.nodes[1]);
        }

        // Set values for distances and predecessors
        for(Integer node : allNodes){
            if(node == source){
                this.distances[node] = 0;
            } else {
                this.distances[node] = Integer.MAX_VALUE;
            }
            this.predecessors[node] = -1;
        }

        // Relax
        for(int i = 1; i < allNodes.size(); i++){
            for(Edge edge : g.getEdges()){
                if(distances[edge.nodes[0]] + edge.weight < distances[edge.nodes[1]]){
                    distances[edge.nodes[1]] = distances[edge.nodes[0]] + edge.weight;
                    predecessors[edge.nodes[1]] = edge.nodes[0];
                }
            }
        }
        
        // Find negative weight cycles and throw and exception
        for(Edge edge : g.getEdges()){
            if(distances[edge.nodes[0]] + edge.weight < distances[edge.nodes[1]]){
                throw new Exception("Cannot contain negative weight cycles");
            }
        }

    }

    public int[] shortestPath(int destination) throws Exception{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Error is thrown
         */

        // Add destination to arraylist in reverse order
        ArrayList<Integer> path = new ArrayList<>(this.predecessors.length);
        path.add(destination);

        int predecessor = destination;
        // Add all the predecessors in G until unreachable
        while((predecessor = this.predecessors[predecessor]) != -1){
            path.add(predecessor);
        }

        // Throw an exception if the last element isn't the source
        if(path.get(path.size() - 1) != this.source){
            throw new Exception("There does not exist a path between source and destination");
        }

        // Reverse the arraylist
        Collections.reverse(path);
        int[] arrayPath = new int[path.size()];
        for(int i = 0; i < arrayPath.length; i++){
            arrayPath[i] = path.get(i);
        }
        return arrayPath;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try{
            int[] path = this.shortestPath(destination);
            for(int i = 0; i < path.length; i++){
                int next = path[i];
                if(next == destination){
                    System.out.println(destination);
                }else{
                    System.out.print(next + "-->");
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }catch(Exception e){
            System.out.println(e);
        }

    }
}