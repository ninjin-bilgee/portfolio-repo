package edu.cwru.csds293;
 
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;
import java.util.Iterator;
import static java.util.Objects.requireNonNull;
 
/**
 * Represents an undirected edge between two distinct vertices.
 */
public final class Edge implements Iterable<Vertex>{

    //Set of vertices to create an edge from
    private final Set<Vertex> endPoints;

    //Private constructor for an Edge
    private Edge(Set<Vertex> endPoints){
        this.endPoints = endPoints;
    }
 
    /**
     * Creates a new Edge between the given vertices.
     *
     * @param u first vertex
     * @param v second vertex
     * @return a new Edge connecting u and v
     * @throws EdgeException if either vertex is null or both vertices are equal
     */
    public static Edge from(Vertex u, Vertex v) throws EdgeException{
        if(u == null || v == null || u.equals(v)){
            throw new EdgeException(u, v);
        }

        Set<Vertex> endPoints = new HashSet<>();
        endPoints.add(u);
        endPoints.add(v);
        return new Edge(endPoints);
    }
 
    /**
     * Returns an unmodifiable view of the endpoints.
     *
     * @return the endpoints of this edge
     */
    public Set<Vertex> endPoints(){
        return Collections.unmodifiableSet(endPoints);
    }
 
    /**
     * Checks if the given vertex is an endpoint of this edge.
     *
     * @param z vertex to check
     * @return true if z is an endpoint
     */
    public boolean isPresent(Vertex z){
        requireNonNull(z, "Vertex cannot be null.");
        return (endPoints.contains(z));
    }

    /**
     * Returns the other endpoint of this edge that is not the given vertex.
     *
     * @param z a vertex of the edge
     * @return optional containing the neighbor vertex if it exists
     */
    public Optional<Vertex> neighbor(Vertex z){

        requireNonNull(z, "Vertex cannot be null.");
 
        return endPoints.stream()
                        .filter(v -> !v.equals(z))
                        .findFirst()
                        .filter(v -> endPoints.contains(z));  
    }
 
    /**
     * Helper method to return the unique unshared vertex.
     */
    private Optional<Vertex> uniqueVertex(int foundMatches, Vertex u){

        assert u != null : "The vertex cannot be null.";

        /* Case 1 */
        if(foundMatches == 1){
            return Optional.of(u);
        } 
        /* Case 2 and 3 */
        else{
            return Optional.empty();
        }
    }
 
    /**
     * Returns the endpoint of this edge that is not contained in the given set of vertices.
     *
     * @param vertexSet set of vertices to check
     * @return optional containing the unshared vertex, empty if none
     */
    public Optional<Vertex> neighbor(Set<Vertex> vertexSet){

        requireNonNull(vertexSet, "The vertex set cannot be null.");

        Vertex unsharedVertex = null;
        int oneMatchFound = 0;
 
        for(Vertex v: endPoints){
            if(vertexSet.contains(v)){
                oneMatchFound++;
            } 
            else{
                unsharedVertex = v;
            }
        }
        return uniqueVertex(oneMatchFound, unsharedVertex);
    }
 
    /**
     * Checks equality of two edges. Edges are equal if they contain the same endpoints,
     * regardless of order.
     *
     * @param o object to compare
     * @return true if edges are equal
     */
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Edge)){
            return false;
        }
        Edge other = (Edge) o;
        return endPoints.equals(other.endPoints);
    }

    /**
     * Returns the hash code of the edge, consistent with equality.
     *
     * @return hash code of the edge
     */
    @Override
    public int hashCode(){
        return endPoints.hashCode();
    }
 
    /**
     * Returns an iterator over the edge's endpoints.
     *
     * @return iterator of vertices
     */
    @Override
    public Iterator<Vertex> iterator(){
        return endPoints.iterator();
    }

    /**
     * Returns a string representation of the edge.
     *
     * @return string in the form "(u --- v)"
     */
    @Override
    public String toString(){
        Iterator<Vertex> e = this.iterator();
        Vertex u = e.next();
        Vertex v = e.next();
        return "(" + u.label() + " --- " + v.label() + ")";
    }
}
 
