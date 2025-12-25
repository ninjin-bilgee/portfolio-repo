package edu.cwru.csds293;
 
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
 
/**
 * Represents a set of edges in a graph.
 */
public final class EdgeSet{

    //Set of edges to be turned into an EdgeSet
    private final Set<Edge> edgeSet;

    //Private constructor for EdgeSet
    private EdgeSet(Set<Edge> edgeSet){
        this.edgeSet = edgeSet;
    }

    /**
     * Factory method to create an EdgeSet from a given set of edges.
     * Validates that the input set and all edges are non-null.
     *
     * @param edges the set of edges to wrap
     * @return a new EdgeSet instance containing the validated edges
     * @throws NullPointerException if edges is null or contains null elements
     */
    public static final EdgeSet from(Set<Edge> edges){
        
        Objects.requireNonNull(edges, "The input set of edges cannot be null.");
 
        Set<Edge> validatedEdgeSet = edges.stream()
                                   .peek(e -> Objects.requireNonNull(e, "Edges cannot be null!"))
                                   .collect(Collectors.toCollection(HashSet::new));
        
        return new EdgeSet(validatedEdgeSet);
    }

    /**
     * Returns an unmodifiable view of the edge set.
     * <p>This prevents external code from modifying the internal set of edges.</p>
     *
     * @return an unmodifiable set of edges
     */
    Set<Edge> getEdgeSet(){
        return Collections.unmodifiableSet(edgeSet);
    }
 
    /**
     * Returns a string representation of the EdgeSet.
     *
     * @return a string listing all edges in the set
     */
    @Override
    public String toString(){
        return edgeSet.stream()
                      .map(Edge::toString)
                      .collect(Collectors.joining(", ", "[", "]"));
    }
}
