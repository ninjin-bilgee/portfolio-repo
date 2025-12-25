package edu.cwru.csds293;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
 
/**
 * A simple implementation of {@link AnnotatedGraph} that wraps a {@link Graph}
 * and provides optional annotations for vertices and edges.
 *
 * @param <V> type of vertex annotation
 * @param <E> type of edge annotation
 */
public class PlainAnnotatedGraph<V, E> implements AnnotatedGraph<Optional<V>, Optional<E>>{

    private final Graph graph;
    private final Map<Vertex, V> vertexProperties;
    private final Map<Edge, E> edgeProperties;

    //Private constructor for a plain annotated graph
    //Construction done with factory method from
    private PlainAnnotatedGraph(Graph graph, Map<Vertex, V> vertexProperties, Map<Edge, E> edgeProperties){
        this.graph = graph;
        this.vertexProperties = vertexProperties;
        this.edgeProperties = edgeProperties;
    }
 
    /**
     * Creates a {@link PlainAnnotatedGraph} from a graph with given vertex and edge annotations.
     *
     * @param graph the underlying graph
     * @param vertexProperties map of vertex annotations
     * @param edgeProperties map of edge annotations
     * @param <V> type of vertex annotation
     * @param <E> type of edge annotation
     * @return a {@link PlainAnnotatedGraph} with optional annotations
     * @throws NullPointerException if any argument is null
     */
    public static <V, E> AnnotatedGraph<Optional<V>, Optional<E>> from(Graph graph, Map<Vertex, V> vertexProperties, Map<Edge, E> edgeProperties){
 
        if(graph == null){
            throw new NullPointerException("The graph cannot be null!");
        }
        if(vertexProperties == null){
            throw new NullPointerException("The additional vertex properties cannot be null!");
        }
        if(edgeProperties == null){
            throw new NullPointerException("The additional edge properties cannot be null!");
        }
 
        return new PlainAnnotatedGraph<>(graph, vertexProperties, edgeProperties);
    }
 
    /**
     * Creates a {@link PlainAnnotatedGraph} from a graph with no vertex or edge annotations.
     *
     * @param graph the underlying graph
     * @param <V> type of vertex annotation
     * @param <E> type of edge annotation
     * @return a {@link PlainAnnotatedGraph} with empty optional annotations
     * @throws NullPointerException if graph is null
     */
    public static <V, E> AnnotatedGraph<Optional<V>, Optional<E>> from(Graph graph){
 
        if(graph == null){
            throw new NullPointerException("The graph cannot be null!");
        }
 
        return new PlainAnnotatedGraph<>(graph, Collections.emptyMap(), Collections.emptyMap());
    }
 
    /**
     * Returns the optional annotation of a vertex.
     *
     * @param v the vertex
     * @return {@link Optional} containing the vertex annotation, or empty if none exists
     * @throws NullPointerException if vertex is null
     */
    @Override
    public Optional<V> property(Vertex v){
 
        if(v == null){
            throw new NullPointerException("The vertex cannot be null!");
        }
        if(vertexProperties.containsKey(v)){
            return Optional.of(vertexProperties.get(v));
        } 
        else{
            return Optional.empty();
        }
    }
 
    /**
     * Returns the optional annotation of an edge.
     *
     * @param e the edge
     * @return {@link Optional} containing the edge annotation, or empty if none exists
     * @throws NullPointerException if edge is null
     */
    @Override
    public Optional<E> property(Edge e){
 
        if(e == null){
            throw new NullPointerException("The edge cannot be null!");
        }
        if(edgeProperties.containsKey(e)){
            return Optional.of(edgeProperties.get(e));
        } 
        else{
            return Optional.empty();
        }
    }

    /**
     * Returns the set of vertices in the underlying graph.
     *
     * @return unmodifiable {@link Set} of vertices
     */
    @Override
    public Set<Vertex> vertices(){
        return graph.vertices();
    }

    /**
     * Returns the adjacency mapping of the underlying graph.
     *
     * @return unmodifiable {@link Map} from vertex to {@link EdgeSet}
     */
    @Override
    public Map<Vertex, EdgeSet> edges(){
        return graph.edges();
    }
}