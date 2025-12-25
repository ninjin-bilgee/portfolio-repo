package edu.cwru.csds293;

/**
 * Represents a Graph with additional properties attached to its vertices and edges.
 * @param <V> type of vertex annotation stored
 * @param <E> type of edge annotation stored
 */
public interface AnnotatedGraph<V, E> extends Graph{
 
    /**
     * Returns the annotation associated with a vertex.
     *
     * @param vertex the vertex whose annotation is requested
     * @return the annotation of type {@code V} attached to the vertex
     */
    V property(Vertex vertex);
 
    /**
     * Returns the annotation associated with an edge.
     *
     * @param edge the edge whose annotation is requested
     * @return the annotation of type {@code E} attached to the edge
     */
    E property(Edge edge);
}
