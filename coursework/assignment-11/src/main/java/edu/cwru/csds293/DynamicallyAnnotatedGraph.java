package edu.cwru.csds293;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Represents an annotated graph whose vertices and edges carry dynamic annotations.
 *
 * <p>This class implements {@link AnnotatedGraph}, allowing annotations for 
 * vertices and edges. 
 *
 * @param <V> the type of vertex annotation
 * @param <E> the type of edge annotation
 */
public class DynamicallyAnnotatedGraph<V, E> implements AnnotatedGraph<V, E> {

    /**
     * Adjacency set builder.
     */
    protected final AdjacencySets adjacencySets;

    /**
     * Vertex annotations.
     */
    protected final Map<Vertex, V> vertexAnnotations;

    /**
     * Edge annotations.
     */
    protected final Map<Edge, E> edgeAnnotations;

    /**
     * Constructs a dynamically annotated graph with the given adjacency sets
     * and annotations for vertices and edges.
     *
     * @param adjacencySets the graph's adjacency sets
     * @param vertexAnnotations a map of vertex annotations
     * @param edgeAnnotations a map of edge annotations
     */
    public DynamicallyAnnotatedGraph(AdjacencySets adjacencySets, Map<Vertex, V> vertexAnnotations, Map<Edge, E> edgeAnnotations) {
        this.adjacencySets = adjacencySets;
        this.vertexAnnotations = Map.copyOf(vertexAnnotations);
        this.edgeAnnotations = Map.copyOf(edgeAnnotations);
    }

    /**
     * Returns the annotation for the given vertex.
     *
     * @param v the vertex
     * @return the vertex's annotation
     */
    @Override
    public V property(Vertex v) {
        return vertexAnnotations.get(v);
    }

    /**
     * Returns the annotation for the given edge.
     *
     * @param e the edge
     * @return the edge's annotation
     */
    @Override
    public E property(Edge e) {
        return edgeAnnotations.get(e);
    }

    /**
     * Returns the set of vertices in the graph.
     *
     * @return set of vertices
     */
    @Override
    public Set<Vertex> vertices() {
        return adjacencySets.vertices();
    }

    /**
     * Returns a map of vertices to their incident edge sets.
     *
     * @return map of edges
     */
    @Override
    public Map<Vertex, EdgeSet> edges() {
        return adjacencySets.edges();
    }

    /**
     * Nested Builder class: Creates instances of {@link DynamicallyAnnotatedGraph}.
     *
     * @param <V> type of vertex annotation
     * @param <E> type of edge annotation
     */
    public static class Builder<V, E> {

        /**
         * Adjacency set builder object.
         */
        protected AdjacencySetBuilder adjacencyBuilder;

        /**
         * Vertex annotations.
         */
        protected Map<Vertex, V> vertexAnnotations;

        /**
         * Edge annotations.
         */
        protected Map<Edge, E> edgeAnnotations;

        /** Constructs a new Builder with empty adjacency sets and annotation maps */
        public Builder() {
            this.adjacencyBuilder = new AdjacencySetBuilder();
            this.vertexAnnotations = new HashMap<>();
            this.edgeAnnotations = new HashMap<>();
        }

        /**
         * Adds a vertex with the given annotation.
         *
         * @param v the vertex to add
         * @param annotation the annotation for the vertex
         * @return this builder
         * @throws NullPointerException if vertex is null
         */
        public Builder<V, E> addVertex(Vertex v, V annotation) {
            requireNonNull(v, "Vertex cannot be null");

            adjacencyBuilder.add(v);
            vertexAnnotations.put(v, annotation);
            return this;
        }

        /**
         * Removes a vertex and all its incident edges.
         *
         * @param v the vertex to remove
         * @return this builder
         * @throws NullPointerException if vertex is null
         */
        public Builder<V, E> removeVertex(Vertex v) {
            requireNonNull(v, "Vertex cannot be null");

            adjacencyBuilder.edges(v).forEach(edgeAnnotations::remove); 
            adjacencyBuilder.remove(v);
            vertexAnnotations.remove(v);
            return this;
        }

        /**
         * Removes multiple vertices and all their incident edges.
         *
         * @param toRemove set of vertices to remove
         * @return this builder
         * @throws NullPointerException if vertex set is null
         */
        public Builder<V, E> removeVertices(Set<Vertex> toRemove) {
            requireNonNull(toRemove, "Vertex set cannot be null");

            for(Vertex v : toRemove) {
                removeVertex(v);
            }
            return this;
        }

        /**
         * Adds an edge with the specified annotation.
         *
         * @param e the edge to add
         * @param annotation the annotation for the edge
         * @return this builder
         * @throws NullPointerException if given edge is null
         */
        public Builder<V, E> addEdge(Edge e, E annotation) {
            requireNonNull(e, "Edge cannot be null");

            adjacencyBuilder.add(e);
            edgeAnnotations.put(e, annotation);
            return this;
        }

        /**
         * Removes an edge and its annotation.
         *
         * @param e the edge to remove
         * @return this builder
         * @throws NullPointerException if given edge is null
         */
        public Builder<V, E> removeEdge(Edge e) {
            requireNonNull(e, "Edge cannot be null");
            
            adjacencyBuilder.remove(e);
            edgeAnnotations.remove(e);
            return this;
        }

        /**
         * Constructs a {@link DynamicallyAnnotatedGraph} from the current state of the builder.
         * 
         * <p>This method:</p>
         * <ol>
         *     <li>Creates a new {@link AdjacencySetStatus} object to track validation issues.</li>
         *     <li>Attempts to build an {@link AdjacencySets} instance from the {@link #adjacencyBuilder}.</li>
         *     <li>If the adjacency sets are invalid, throws an {@link IllegalStateException}.</li>
         *     <li>Otherwise, constructs a new {@link DynamicallyAnnotatedGraph} using the validated adjacency sets and the current vertex and edge annotations.</li>
         * </ol>
         *
         * @return a {@link ShrinkableGraph} instance
         * @throws IllegalStateException if the adjacency sets are invalid from status
         */
        public DynamicallyAnnotatedGraph<V,E> build(){
            AdjacencySetStatus status = new AdjacencySetStatus();
            Optional<AdjacencySets> optionalAdjSets = adjacencyBuilder.build(status);
            if (optionalAdjSets.isEmpty()) {
                throw new IllegalStateException("Invalid adjacency sets: " + status);
            }
            return new DynamicallyAnnotatedGraph<V,E>(optionalAdjSets.get(), vertexAnnotations, edgeAnnotations);
        }
    }
}

