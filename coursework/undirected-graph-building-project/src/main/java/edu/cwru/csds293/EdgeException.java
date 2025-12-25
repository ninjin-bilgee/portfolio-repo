package edu.cwru.csds293;

/**
 * Exception thrown when an Edge cannot be created due to invalid vertices.
 *
 * This class handles two types of errors for Edge creation:
 * <ol>
 *     <li>NULL_VERTEX: one of the vertices is null</li>
 *     <li>SAME_VERTEX: both vertices are the same</li>
 * </ol>
 * Each exception stores the problematic vertex if invalid and the type of error.
 */
public final class EdgeException extends java.lang.Exception{

    //Serial version UID (can be any long value)
    private static final long serialVersionUID = 3L;

    /**
     * Types of edge exceptions.
     */
    public enum Type{
        /**
         * Exception thrown when a vertex on an edge is null, one or more.
         */
        NULL_VERTEX,

        /**
         * Exception thrown when the same vertices try to create an edge.
         */
        SAME_VERTEX     
    }

    /**
     * Type of exception.
     */
    private final Type type;

    /**
     * The vertex that caused this exception.
     */
    private final Vertex problemVertex;

    /**
     * Constructs an {@code EdgeException} for an invalid edge.
     * <p>
     * This exception occurs when either:
     * <ol>
     *   <li>One or both vertices are {@code null} (type {@link Type#NULL_VERTEX})</li>
     *   <li>The two vertices are identical (type {@link Type#SAME_VERTEX})</li>
     * </ol>
     *
     * @param u the first vertex of the edge (may be null)
     * @param v the second vertex of the edge (may be null)
     * @throws IllegalArgumentException if both vertices are non-null and distinct
     */
    public EdgeException(Vertex u, Vertex v) {
        if (u == null || v == null) {
            this.type = Type.NULL_VERTEX;
            this.problemVertex = (u != null) ? u : v; // whichever is non-null
        } else if (u.equals(v)) {
            this.type = Type.SAME_VERTEX;
            this.problemVertex = u;
        } else {
            throw new IllegalArgumentException(
                "EdgeException can only be used for null or same-vertex errors."
            );
        }
    }

    /**
     * Returns the vertex that caused this exception.
     *
     * @return the problematic vertex
     */
    public Vertex getProblemVertex(){
        return problemVertex;
    }

    /**
     * Returns the type of this exception.
     *
     * @return the type of error
     */
    public Type getType(){
        return type;
    }

    /**
     * Returns an error message describing the exception.
     *
     * @return error message
     */
    @Override
    public String getMessage() {
        return switch(type) {
            case NULL_VERTEX -> "Edge cannot be created with a null vertex.";
            case SAME_VERTEX -> "Edge cannot connect the same vertex to itself: " + problemVertex;
        };
    }
}