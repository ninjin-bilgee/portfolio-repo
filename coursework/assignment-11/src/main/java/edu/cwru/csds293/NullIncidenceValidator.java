package edu.cwru.csds293;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class that detects vertices with null adjacency sets in a graph.
 * <p>Non-instantiable class.</p>
 */
public final class NullIncidenceValidator{

    //No instance will be made
    private NullIncidenceValidator() {}

    /**
     * Finds all vertices that have a null adjacency set.
     *
     * @param edges a map from {@link Vertex} to {@link EdgeSet} representing adjacency sets
     * @return a {@link Set} of vertices whose adjacency sets are {@code null}
     */
    public static Set<Vertex> find(Map<Vertex, EdgeSet> edges){
        return edges.entrySet().stream()
            .filter(entry -> entry.getValue() == null)
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }
}
