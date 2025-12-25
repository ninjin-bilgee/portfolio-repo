package edu.cwru.csds293;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Iterator;
import static java.util.Objects.requireNonNull;

/**
 * A mutable builder for constructing and modifying adjacency-set–based graph representations. 
 * This class maintains an internal mapping from each vertex to the set of incident edges, supporting 
 * adding/removing vertices, adding edges, removing edges, and contracting edges. Once the desired structure 
 * is created, it can be converted into an immutable {@link AdjacencySets} instance with {@link AdjacencySetStatus}.
 *
 * <p>This builder guarantees that all adjacency information is kept consistent across operations.</p>
 */
public final class AdjacencySetBuilder{

    //Map of edges
    private final Map<Vertex, Set<Edge>> edges;

    /**
     * Constructs an empty builder with no vertices and no edges.
     */
    public AdjacencySetBuilder() {
        this.edges = new HashMap<>();
    }

    /**
     * Returns an unmodifiable view of all edges currently incident on the
     * given vertex. If the vertex is not present in the builder, an empty
     * set is returned.
     *
     * @param u the vertex whose adjacency set is requested
     * @return an unmodifiable set of edges incident on {@code u}
     * @throws NullPointerException if {@code u} is null
     */
    public Set<Edge> edges(Vertex u){
        requireNonNull(u, "Vertex cannot be null");
        return Collections.unmodifiableSet(edges.getOrDefault(u, Collections.emptySet()));
    }

    /**
     * Returns all edges incident on either endpoint of the given edge,
     * excluding the given edge. 
     *
     * @param edge the edge whose adjacent edges are to be retrieved
     * @return a set of edges incident on the endpoints of {@code edge},
     *         except for {@code edge} itself
     * @throws NullPointerException if {@code edge} is null
     */
    public Set<Edge> incidentEdges(Edge edge){
        requireNonNull(edge, "Edge cannot be null");

        /* Loop through each vertex in the given edge's endpts */
        return edge.endPoints().stream()
            .flatMap(v -> edges.getOrDefault(v, Collections.emptySet()).stream())
            .filter(e -> !e.equals(edge))
            .collect(Collectors.toSet());
    }

    /**
     * Adds the specified vertex to the builder if it does not already
     * exist. The vertex is initialized with an empty adjacency set.
     * If the vertex is already present, this method does nothing.
     *
     * @param u the vertex to add
     * @return this builder instance 
     * @throws NullPointerException if {@code u} is null
     */
    public AdjacencySetBuilder add(Vertex u){
        requireNonNull(u, "Vertex cannot be null");
        edges.putIfAbsent(u, new HashSet<>());
        return this;
    }

    /**
     * Adds the given edge to the adjacency sets of both its endpoints.
     * New vertices are automatically created if the endpoints are not yet
     * present in the builder.
     *
     * @param edge the edge to add
     * @return this builder instance 
     * @throws NullPointerException if {@code edge} is null
     */
    public AdjacencySetBuilder add(Edge edge){
        requireNonNull(edge, "Edge cannot be null");
        for (Vertex v : edge.endPoints()) {
            edges.computeIfAbsent(v, k -> new HashSet<>()).add(edge);
        }
        return this;
    }

    /**
     * Removes an edge from both of its endpoints' adjacency sets. If the
     * edge is not present at one or both endpoints, nothing happens to the builder.
     *
     * @param edge the edge to remove
     * @return this builder instance 
     * @throws NullPointerException if {@code edge} is null
     */
    public AdjacencySetBuilder remove(Edge edge){
        requireNonNull(edge, "Edge cannot be null");
        for (Vertex v : edge.endPoints()){
            edges.getOrDefault(v, Collections.emptySet()).remove(edge);
        }
        return this;
    }

    /**
     * Removes the given vertex and all edges incident on it. All
     * neighboring vertices are updated to remove their references to that vertex's
     * incident edges. The method is removed from the builder if it exists.
     *
     * @param u the vertex to remove
     * @throws NullPointerException if {@code u} is null
     */
    public void removeVertex(Vertex u){
        requireNonNull(u, "Vertex cannot be null");
        Set<Edge> incident = new HashSet<>(edges.getOrDefault(u, Collections.emptySet()));
        for (Edge e : incident) {
            e.endPoints().stream().filter(v -> !v.equals(u)).forEach(v -> edges.get(v).remove(e));
        }
        edges.remove(u);
    }

    /**
     * Removes the specified vertex and all edges incident on it, returning
     * this builder for chaining. Identical to
     * {@link #removeVertex(Vertex)}.
     *
     * @param u the vertex to remove
     * @return this builder instance
     * @throws NullPointerException if {@code u} is null
     */
    public AdjacencySetBuilder remove(Vertex u){
        requireNonNull(u, "Vertex cannot be null");
        Set<Edge> incident = new HashSet<>(edges.getOrDefault(u, Collections.emptySet()));
        for (Edge e : incident) {
            e.endPoints().stream().filter(v -> !v.equals(u)).forEach(v -> edges.get(v).remove(e));
        }
        edges.remove(u);
        return this;
    }

    /**
     * Removes all vertices in the given set and all incident edges.
     *
     * @param vertices the set of vertices to remove
     * @return this builder instance
     * @throws NullPointerException if {@code vertices} is null
     */
    public AdjacencySetBuilder remove(Set<Vertex> vertices){
        requireNonNull(vertices, "Vertices cannot be null");
        vertices.forEach(this::removeVertex);
        return this;
    }

    /**
     * Builds an immutable {@link AdjacencySets} instance using the current
     * adjacency data and the given status. The status determines how the
     * resulting structure is validated and interpreted.
     *
     * @param status the guaranteed status
     * @return an {@code Optional} containing the constructed adjacency sets
     *         if successful, or empty if the structure is invalid
     * @throws NullPointerException if {@code status} is null
     */
    public Optional<AdjacencySets> build(AdjacencySetStatus status){
        requireNonNull(status, "Status cannot be null");
        Map<Vertex, EdgeSet> edgeSets = new HashMap<>();
        edges.forEach((v, set) -> edgeSets.put(v, EdgeSet.from(set)));
        return AdjacencySets.from(edgeSets, status);
    }

    /**
     * A private helper that collects all edges incident on either of two
     * vertices, excluding a given edge.
     */
    private Set<Edge> collectIncidentEdgesExcluding(Edge excludedEdge, Vertex u, Vertex v){
        assert excludedEdge != null: "The inputted edge to exclude cannot be null.";
        assert u != null: "The inputted first endpoint cannot be null.";
        assert v != null: "The inputted second endpoint cannot be null.";
        
        Set<Edge> result = new HashSet<>();
        for(Edge e: incidentEdges(excludedEdge)){
            if(!e.equals(excludedEdge)){
                result.add(e);
            }
        }
        return result;
    }

    /**
     * Returns whether the given vertex is currently in the builder.
     *
     * @param vertex the vertex to test
     * @return {@code true} if the vertex exists, otherwise {@code false}
     * @throws NullPointerException if {@code vertex} is null
     */
    public boolean contains(Vertex vertex){
        requireNonNull(vertex, "Vertex cannot be null.");
        return edges.containsKey(vertex);
    }

    /**
     * Contracts the specified edge by merging its two endpoints into a new
     * combined vertex whose label is the concatenation of the original labels.
     * Functionalities:
     * <ol>
     *   <li>Collects all incident edges except the given edge in the method.</li>
     *   <li>Removes both original vertices and their incident edges.</li>
     *   <li>Adds a new combined vertex.</li>
     *   <li>Adds back the edges from the two original vertices, now changed to be 
     *       connected to the combined vertex.</li>
     * </ol>
     *
     * @param edge the edge to contract
     * @return this builder instance
     * @throws NullPointerException if {@code edge} is null
     * @throws EdgeException if edge creation fails during the changes
     */
    public AdjacencySetBuilder contract(Edge edge) throws EdgeException{
        requireNonNull(edge, "Edge cannot be null");

        Iterator<Vertex> source = edge.iterator();
        Vertex u = source.next();
        Vertex v = source.next();

        Vertex comboVertex = new Vertex(u.label() + v.label());

        Set<Edge> incidentEdgesForOldPts = collectIncidentEdgesExcluding(edge, u, v);

        this.removeVertex(u);
        this.removeVertex(v);

        this.add(comboVertex);

        for(Edge toRemove : incidentEdgesForOldPts){
            this.remove(toRemove);
        }

        for(Edge e: incidentEdgesForOldPts){
            Optional<Vertex> neighbor = e.neighbor(u);

            if(!neighbor.isPresent()){
                neighbor = e.neighbor(v);
            }

            Vertex comboNeighbor = neighbor.orElseThrow(
                () -> new IllegalStateException("Edge has no neighbor to attach")
            );
            Edge newEdge = Edge.from(comboVertex, comboNeighbor);
            this.add(newEdge);
        }
        return this;
    }
}
 
