package edu.cwru.csds293;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MissingEndpointsValidator
 * Utility class that detects vertices that are missing endpoints in the graph.
 * 
 * <p>Non-instantiable class.</p>
 */
public class MissingEndpointsValidator{
    
    //NO instance will be made
    private MissingEndpointsValidator() {}

    /**
     * Finds all vertices that are missing from the endpoints of one or more edges.
     *
     * @param edges a map from {@link Vertex} to {@link EdgeSet} representing the adjacency sets
     * @return a {@link Set} of vertices for which at least one incident edge does not include the vertex as an endpoint
     */
    public static Set<Vertex> find(Map<Vertex, EdgeSet> edges){
        return edges.entrySet().stream()
            .filter(e -> e.getValue() != null)
            .filter(e -> e.getValue().getEdgeSet().stream()
                            .anyMatch(edge -> !edge.isPresent(e.getKey())))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }
}
