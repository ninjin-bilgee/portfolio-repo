package edu.cwru.csds293;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A simple, validated implementation of {@link Graph}.
 * <p>
 * Graph validity is checked during construction using {@link Barricade}.
 * </p>
 */
public class PlainGraph implements Graph{

    private final Set<Vertex> vertices;
    private final Map<Vertex, EdgeSet> edges;
    private final AdjacencySetStatus status;

    /**
     * Builds a validated PlainGraph.
     * @param vertices vertices in the graph
     * @param edges adjacency sets
     * @throws IllegalArgumentException if the graph is invalid
     * @throws NullPointerException if either argument is null
     */
    public PlainGraph(Set<Vertex> vertices, Map<Vertex, EdgeSet> edges){

        requireNonNull(vertices, "The vertices cannot be null.");
        requireNonNull(edges, "The edges cannot be null.");

        this.vertices = Collections.unmodifiableSet(new HashSet<>(vertices));
        this.edges = Collections.unmodifiableMap(new HashMap<>(edges));

        this.status = new AdjacencySetStatus();
        Barricade.validate(this.edges, status);

        if(!status.isOk()){
            throw new IllegalArgumentException("Invalid graph construction: " + status);
        }
    }

    /**
     * Returns an unmodifiable view of the graph's vertices.
     *
     * @return a {@link Set} of vertices
     */
    @Override
    public Set<Vertex> vertices(){
        return Set.copyOf(vertices);
    }

    /**
     * Returns an unmodifiable view of the graph's adjacency sets.
     *
     * @return a {@link Map} from vertex to {@link EdgeSet}
     */
    @Override
    public Map<Vertex, EdgeSet> edges(){
        return Map.copyOf(edges);
    }

    /**
     * Returns a visual String representation of the graph.
     *
     * @return string representation of the graph
     */
    @Override
    public String toString(){
        StringBuilder toPrint = new StringBuilder("PlainGraph:\n");
        for(Vertex v: vertices){
            toPrint.append("   ").append(v).append(" --> ");
            EdgeSet edgeSet = edges.get(v);
            if(edgeSet != null){
                String edgeString = edgeSet.getEdgeSet().stream()
                .map(e -> {
                    Vertex other = e.neighbor(v).orElse(null);
                    return other != null ? v + " --- " + other : "";
            })
            .filter(s -> !s.isEmpty())
            .collect(Collectors.joining(", "));
            toPrint.append(edgeString);
        }
            toPrint.append("\n");
        }
        return toPrint.toString();
    }
}

