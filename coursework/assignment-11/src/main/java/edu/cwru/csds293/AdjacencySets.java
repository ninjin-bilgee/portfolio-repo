package edu.cwru.csds293;
 
import java.util.stream.Collectors;
import java.util.*;
import static java.util.Objects.requireNonNull;
 
/**
 * Represents sets of edges incident into a vertex. Each vertex is mapped 
 * to its set of edges ({@link EdgeSet}), forming the adjacency sets of a graph.
 */
public final class AdjacencySets{

    //Map of edges to create an adjacency set from
    //don't have to comment on private stuff
        //usually try to aim for self-documenting code
    private final Map<Vertex, EdgeSet> edges;

    //Create an adjacency set visual with a map of edges
    private AdjacencySets(Map<Vertex, EdgeSet> edges){
        this.edges = new HashMap<>(edges);
    }
 
    /**
     * Factory method to create an AdjacencySets object from a given map of edges.
     * Validates the input map using a Barricade and status object.
     * 
     * @param edges  map of vertices to their incident edges
     * @param status status object used to capture validation results
     * @return Optional containing a new AdjacencySets object if valid; empty Optional otherwise
     */
    public static Optional<AdjacencySets> from(Map<Vertex, EdgeSet> edges, AdjacencySetStatus status){
 
        requireNonNull(edges, "Edges map cannot be null");
        requireNonNull(status, "Status cannot be null");
 
        Barricade.validate(edges, status);

        if (!status.isOk()) {
            return Optional.empty();
        }
        return Optional.of(new AdjacencySets(edges));
    }
 
    /**
     * Returns an unmodifiable view of the adjacency edges.
     * 
     * @return unmodifiable map of vertices to their EdgeSet
     */
    public Map<Vertex, EdgeSet> edges(){
        return Collections.unmodifiableMap(edges);
    }
 
    /**
     * Returns an unmodifiable view of all vertices in the adjacency sets.
     * 
     * @return set of vertices
     */
    public Set<Vertex> vertices(){
        return Collections.unmodifiableSet(edges.keySet());
    }
 
    /**
     * @param u the first endpoint in a vertex pair
     * @param v the second endpoint in a vertex pair
     * Nested record that represents an unordered pair of vertices. (u,v) is the same as (v,u).
     */
    public record VertexPair(Vertex u, Vertex v){

        /**
         * Returns a new VertexPair with the vertices inverted.
         * 
         * @return a new VertexPair with u and v swapped
         */
        public VertexPair invert(){
            return new VertexPair(v, u);
        }
    }

     /**
     * Returns a string visual representation of the adjacency sets.
     * Each line contains a vertex and its incident edges.
     * 
     * @return string representation of the graph's adjacency sets
     */
    @Override
    public String toString(){
        StringBuilder toPrint = new StringBuilder();
        toPrint.append("AdjacencySets:\n");
 
        for(Map.Entry<Vertex, EdgeSet> entry: edges.entrySet()){
            Vertex current = entry.getKey();
            toPrint.append("   ").append(current).append(" --> [");
 
            String edgeStr = entry.getValue().getEdgeSet().stream()
                    .map(e -> "(" + current + " --- " + e.neighbor(current).orElse(null) + ")")
                    .collect(Collectors.joining(", "));
            toPrint.append(edgeStr).append("]\n");
        }
        return toPrint.toString();
    }
}