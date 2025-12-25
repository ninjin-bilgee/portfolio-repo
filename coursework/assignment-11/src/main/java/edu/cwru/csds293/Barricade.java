package edu.cwru.csds293;

import java.util.Map;
import java.util.Objects;

/**
 * Utility class that validates the consistency of a graph's adjacency sets.
 * 
 * <p>This class performs a centralized validation of a graph's edges, checking
 * for null adjacency sets, missing endpoints, and asymmetric pairs. The class cannot be instantiated.</p>
 */
public final class Barricade{

    //No instance of barricade will be made
    private Barricade() {} 

    /**
     * Validates the adjacency sets of the graph and updates the provided
     * {@link AdjacencySetStatus} object with any inconsistencies found.
     *
     * <p>This method checks for:</p>
     * <ol>
     *   <li>Vertices with null adjacency sets</li>
     *   <li>Vertices with missing endpoints</li>
     *   <li>Asymmetric vertex pairs</li>
     * </ol>
     *
     * @param edges a map from each {@link Vertex} to its {@link EdgeSet}
     * @param status the {@link AdjacencySetStatus} object to record any issues found
     * @throws NullPointerException if either {@code edges} or {@code status} is null
     */
    public static void validate(Map<Vertex, EdgeSet> edges, AdjacencySetStatus status) {
        Objects.requireNonNull(edges, "edges cannot be null");
        Objects.requireNonNull(status, "status cannot be null");

        status.setNullVertices(NullIncidenceValidator.find(edges));
        status.setMissingVertices(MissingEndpointsValidator.find(edges));
        status.setAsymmetricPairs(AsymmetricPairsValidator.find(edges));
    }
}
