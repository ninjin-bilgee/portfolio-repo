package edu.cwru.csds293;

import java.util.Map;
import java.util.Set;
 
/**
 * Represents a graph consisting of vertices and edges connecting them.
 */
public interface Graph{
 
    /**
     * Returns an unmodifiable view of the vertices in the graph.
     *
     * @return a {@link Set} of {@link Vertex} objects representing the vertices
     */
    public Set<Vertex> vertices();
 
     /**
     * Returns an unmodifiable view of the adjacency sets for each vertex.
     *
     * @return a {@link Map} from {@link Vertex} to {@link EdgeSet}
     */
    public Map<Vertex, EdgeSet> edges();
}
