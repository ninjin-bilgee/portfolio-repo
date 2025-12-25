package edu.cwru.csds293;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import edu.cwru.csds293.AdjacencySets.VertexPair;

/**
 * Utility class that identifies asymmetric vertex pairs in a graph.
 * 
 * <p>Non-instantiable class.</p>
 */
public class AsymmetricPairsValidator{

    private AsymmetricPairsValidator(){}

    /**
     * Finds all asymmetric vertex pairs in the given adjacency sets.
     *
     * <p>An asymmetric pair is represented as a {@link VertexPair} where the
     * edge exists in the adjacency set of one vertex but not in the adjacency
     * set of its neighbor.</p>
     *
     * @param edges a map from each vertex to its {@link EdgeSet}
     * @return a {@link Set} of {@link VertexPair} representing all asymmetric pairs
     */
    public static Set<VertexPair> find(Map<Vertex, EdgeSet> edges){
        return edges.entrySet().stream()
            .filter(entry -> entry.getValue() != null)
            .flatMap(entry -> {
                Vertex u = entry.getKey();
                EdgeSet edgeSet = entry.getValue();
                return edgeSet.getEdgeSet().stream()
                    .map(e -> {
                        Vertex v = e.neighbor(u).orElse(null);
                        return (v != null && !edges.get(v).getEdgeSet().contains(e)) ? new VertexPair(v, u) : null;
                    })
                    .filter(Objects::nonNull);
            })
            .collect(Collectors.toSet());
    }
}
