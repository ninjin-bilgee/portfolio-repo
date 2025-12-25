package edu.cwru.csds293;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * Represents a dynamically annotated graph where
 * vertices are annotated with components (sets of vertices)
 * and edges are annotated with weights (doubles).
 */
public class ShrinkableGraph extends DynamicallyAnnotatedGraph<Set<Vertex>, Double> {

    //Constructs a ShrinkableGraph with given adjacency sets, vertex components, and edge weights.
    private ShrinkableGraph(AdjacencySets adjacencySets, Map<Vertex, Set<Vertex>> components, Map<Edge, Double> weights){
        super(adjacencySets, components, weights);
    }

    /**
     * Returns the component set of a vertex.
     *
     * @param v the vertex whose component set is requested
     * @return a set of vertices representing the component of {@code v}
     */
    @Override
    public Set<Vertex> property(Vertex v) {
        requireNonNull(v, "Vertex cannot be null");
         return vertexAnnotations.getOrDefault(v, Set.of());
    }

    /**
     * Returns the weight of an edge.
     *
     * @param e the edge whose weight is requested
     * @return the weight of {@code e}, or 0.0 for default
     */
    @Override
    public Double property(Edge e) {
        requireNonNull(e, "Edge cannot be null");
         return edgeAnnotations.getOrDefault(e, 0.0);
    }

    /**
     * Nested Builder class
     * 
     * Extends {@link DynamicallyAnnotatedGraph.Builder}.
     */
    public static final class Builder extends DynamicallyAnnotatedGraph.Builder<Set<Vertex>, Double>{

        /**
         * Builder takes from adjacency set builder and its methods.
         */
        public Builder() {
            super();
        }

        /**
         * Contracts an edge {@code e = {u, v}} into a new vertex {@code w}.
         * <ol>
         *   <li>{@code w}'s component is the union of {@code u} and {@code v}'s components.</li>
         *   <li>Edges incident to {@code w} have weights equal to the sum of corresponding edges from {@code u} and {@code v} plus any existing weight.</li>
         * </ol>
         *
         * @param e the edge to contract
         * @return this Builder instance
         * @throws EdgeException if an invalid edge is provided
         * @throws IllegalStateException if either vertex of the edge is missing
         */
        public Builder contractEdge(Edge e) throws EdgeException{
            requireNonNull(e, "Edge cannot be null");

            Iterator<Vertex> it = e.iterator();
            Vertex u = it.next();
            Vertex v = it.next();

            if (!adjacencyBuilder.contains(u) || !adjacencyBuilder.contains(v)) {
                throw new IllegalStateException("Cannot contract edge with missing vertices");
            }

            Vertex w = new Vertex(u.label() + v.label());

            Set<Vertex> newComponent = new HashSet<>(vertexAnnotations.getOrDefault(u, Set.of(u)));
            newComponent.addAll(vertexAnnotations.getOrDefault(v, Set.of(v)));
            vertexAnnotations.put(w, newComponent);

            adjacencyBuilder.contract(e);

            Set<Edge> incident = new HashSet<>();
            incident.addAll(adjacencyBuilder.edges(w));

            //Can leave notes for longer methods using double slash
            for (Edge oldEdge: incident) {
                Vertex other = oldEdge.neighbor(w).orElse(null);
                if (other == null){
                    continue;
                }

                double newWeight = edgeAnnotations.getOrDefault(Edge.from(u, other), 0.0) + edgeAnnotations.getOrDefault(Edge.from(v, other), 0.0) + edgeAnnotations.getOrDefault(oldEdge, 0.0);
                edgeAnnotations.put(oldEdge, newWeight);
            }

            vertexAnnotations.remove(u);
            vertexAnnotations.remove(v);

            return this;
        }
    }
}
