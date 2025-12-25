package edu.cwru.csds293;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import edu.cwru.csds293.AdjacencySets.VertexPair;

/**
 * Represents the validation status of an {@link AdjacencySets} structure.
 *
 * This class records all structural inconsistencies found in the adjacency
 * representation of a graph (null incidence sets, missing endpoints, or 
 * asymmetric adjacency pairs). When no issues are found, the status is "ok."
 */
public final class AdjacencySetStatus{

    private Set<Cause> causes;
    private Set<Vertex> nullIncidenceVertices;
    private Set<Vertex> missingEndpoints;
    private Set<VertexPair> asymmetricPairs;

    /**
     * Enumeration of possible inconsistency types that may be 
     * detected in an adjacency set.
     */
    public enum Cause{

        /**
         * Cause of error when there is a null incidence edge.
         */
        NULL_INCIDENCE,

        /**
         * Cause of error when there is or are missing endpoints.
         */
        MISSING_ENDPOINTS,

        /**
         * Cause of error when there is asymmetry in edge creation.
         */
        ASYMMETRIC_PAIRS
    }

    /**
     * Creates a new, empty status object with no recorded inconsistencies (default state).
     */
    public AdjacencySetStatus(){
        this.causes = new HashSet<>();
        this.nullIncidenceVertices = new HashSet<>();
        this.missingEndpoints = new HashSet<>();
        this.asymmetricPairs = new HashSet<>();
    }

    /**
     * Returns an unmodifiable view of all inconsistency causes recorded for this status.
     *
     * @return a set of failure causes; empty if no issues present
     */
    public Set<Cause> causes(){
        return Collections.unmodifiableSet(causes);
    }

    /**
     * Reports whether this status contains no errors.
     *
     * @return {@code true} if there are no inconsistency causes; {@code false} otherwise
     */
    public boolean isOk(){
        return causes.isEmpty();
    }

    /**
     * Returns an unmodifiable view of all vertices who had null incidence vertices.
     *
     * @return a set of vertices with null adjacency sets
     */
    public Set<Vertex> nullIncidenceVertices(){
        return Collections.unmodifiableSet(nullIncidenceVertices);
    }

    /**
     * Returns an unmodifiable view of all vertices whose incident edges reference
     * missing or undefined endpoint vertices.
     *
     * @return a set of vertices with missing endpoint(s)
     */
    public Set<Vertex> missingEndpoints(){
        return Collections.unmodifiableSet(missingEndpoints);
    }

    /**
     * Returns an unmodifiable view of all asymmetric adjacency pairs.
     * An asymmetric pair is when a vertex lists an edge to another,
     * but the latter does not list the corresponding reverse edge.
     *
     * @return a set of vertex pairs representing asymmetric relationships
     */
    public Set<VertexPair> asymmetricPairs(){
        return Collections.unmodifiableSet(asymmetricPairs);
    }

    /*
     * Internal, centralized generic helper for updating inconsistencies and status accordingly.
     */
    private <T> boolean setStatus(Set<T> set, Cause cause, Setter<T> setter){
        assert set != null : "Set cannot be null";
        assert cause != null : "Cause cannot be null";
        assert setter != null : "Setter cannot be null";

        if(set.isEmpty()){
            return false;
        }
        setter.set(Set.copyOf(set));
        causes.add(cause);
        return true;
    }

    /**
     * Records vertices whose adjacency sets were null.
     *
     * @param set the set of vertices with null adjacency sets
     * @return {@code true} if the set was non-empty and the status updated
     */
    boolean setNullVertices(Set<Vertex> set){
        return setStatus(set, Cause.NULL_INCIDENCE, s -> nullIncidenceVertices = s);
    }

    /**
     * Records vertices whose adjacency sets reference endpoints that do 
     * not exist in the graph.
     *
     * @param set the vertices with missing adjacency endpoints
     * @return {@code true} if the set was non-empty and the status updated
     */
    boolean setMissingVertices(Set<Vertex> set){
        return setStatus(set, Cause.MISSING_ENDPOINTS, s -> missingEndpoints = s);
    }

    /**
     * Records asymmetric adjacency pairs.
     *
     * @param set the asymmetric vertex pairs
     * @return {@code true} if the set was non-empty and the status updated
     */
    boolean setAsymmetricPairs(Set<VertexPair> set){
        return setStatus(set, Cause.ASYMMETRIC_PAIRS, s -> asymmetricPairs = s);
    }
}
