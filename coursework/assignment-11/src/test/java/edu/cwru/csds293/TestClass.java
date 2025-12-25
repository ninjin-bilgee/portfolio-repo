package edu.cwru.csds293;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.cwru.csds293.AdjacencySets.VertexPair;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestClass{

    Vertex v1, v2, v3;
    Edge e1, e2;
    Set<Vertex> vertices;
    Map<Vertex, EdgeSet> edgeMap;
    AdjacencySetStatus status;

    @BeforeEach
    void setup() throws EdgeException {
        v1 = new Vertex("v1");
        v2 = new Vertex("v2");
        v3 = new Vertex("v3");

        e1 = Edge.from(v1, v2);
        e2 = Edge.from(v2, v3);

        vertices = new HashSet<>(Arrays.asList(v1, v2, v3));

        edgeMap = new HashMap<>();
        edgeMap.put(v1, EdgeSet.from(Set.of(e1)));
        edgeMap.put(v2, EdgeSet.from(Set.of(e1, e2)));
        edgeMap.put(v3, EdgeSet.from(Set.of(e2)));

        status = new AdjacencySetStatus();
    }

    @Test
    void vertexRecordTest() {
        assertEquals("v1", v1.label());
        assertNotEquals(v1, v2);
    }

    @Test
    void edgeCreationAndNeighbors() throws EdgeException {
        Edge e = Edge.from(v1, v2);
        assertTrue(e.isPresent(v1));
        assertTrue(e.isPresent(v2));
        assertEquals(Optional.of(v2), e.neighbor(v1));
        assertEquals(Optional.of(v1), e.neighbor(v2));

        Edge eRev = Edge.from(v2, v1);
        assertEquals(e, eRev);
        assertEquals(e.hashCode(), eRev.hashCode());

        String str = "(" + v1.label() + " --- " + v2.label() + ")";
        assertEquals(str, e.toString());
    }


    @Test
    void edgeExceptionTest() {
        assertThrows(EdgeException.class, () -> Edge.from(v1, v1));
        assertThrows(EdgeException.class, () -> Edge.from(null, v1));
        assertThrows(EdgeException.class, () -> Edge.from(null, null));
    }

    @Test
    void edgeSetTest() {
        EdgeSet es = EdgeSet.from(Set.of(e1, e2));
        assertEquals(2, es.getEdgeSet().size());
        String repr = es.toString().replaceAll("\\s+", "");
        assertTrue(repr.contains(e1.toString().replaceAll("\\s+", "")));
        assertTrue(repr.contains(e2.toString().replaceAll("\\s+", "")));
    }

    @Test
    void plainGraphValidTest() {
        PlainGraph graph = new PlainGraph(vertices, edgeMap);
        assertEquals(vertices, graph.vertices());
        assertEquals(edgeMap, graph.edges());
    }

    @Test
    void plainGraphInvalidTest() {
        Map<Vertex, EdgeSet> invalidEdges = new HashMap<>();
        invalidEdges.put(v1, null); 
        assertThrows(IllegalArgumentException.class, () -> new PlainGraph(vertices, invalidEdges));
    }

    @Test
    void plainAnnotatedGraphWithPropsTest() {
        Map<Vertex, String> vProps = Map.of(v1, "A");
        Map<Edge, String> eProps = Map.of(e1, "B");

        AnnotatedGraph<Optional<String>, Optional<String>> ag =
                PlainAnnotatedGraph.from(new PlainGraph(vertices, edgeMap), vProps, eProps);

        assertEquals(Optional.of("A"), ag.property(v1));
        assertEquals(Optional.empty(), ag.property(v2));
        assertEquals(Optional.of("B"), ag.property(e1));
        assertEquals(Optional.empty(), ag.property(e2));
    }

    @Test
    void plainAnnotatedGraphEmptyPropsTest() {
        AnnotatedGraph<Optional<String>, Optional<String>> ag =
                PlainAnnotatedGraph.from(new PlainGraph(vertices, edgeMap));

        assertEquals(Optional.empty(), ag.property(v1));
        assertEquals(Optional.empty(), ag.property(e1));
    }


    @Test
    void shrinkableGraphBuilderContractEdgeTest() throws EdgeException {
        ShrinkableGraph.Builder builder = new ShrinkableGraph.Builder();
        builder.addVertex(v1, Set.of(v1))
            .addVertex(v2, Set.of(v2))
            .addEdge(e1, 2.0);

        builder.contractEdge(e1);

        Vertex merged = new Vertex("v1v2");
        assertTrue(builder.vertexAnnotations.containsKey(merged));
        assertFalse(builder.vertexAnnotations.containsKey(v1));
        assertFalse(builder.vertexAnnotations.containsKey(v2));

        for (Vertex v : Set.of(merged)) { 
            for (Edge edge : builder.adjacencyBuilder.edges(v)) {
                if (edge.endPoints().contains(merged)) {
                    assertTrue(builder.edgeAnnotations.getOrDefault(edge, 0.0) >= 0.0);
                }
            }
        }
    }

    @Test
    void asymmetricPairsValidatorTest() throws EdgeException {
        Map<Vertex, EdgeSet> testEdges = new HashMap<>();
        testEdges.put(v1, EdgeSet.from(Set.of(e1)));
        testEdges.put(v2, EdgeSet.from(Set.of(e2))); 
        testEdges.put(v3, EdgeSet.from(Set.of(e2)));

        Set<AdjacencySets.VertexPair> asymPairs = AsymmetricPairsValidator.find(testEdges);
        assertFalse(asymPairs.isEmpty());
    }

    @Test
    void edgesMethodReturnsUnmodifiableMap() {
        PlainGraph graph = new PlainGraph(vertices, edgeMap);
        Map<Vertex, EdgeSet> edges = graph.edges();

        assertEquals(edgeMap, edges);

        assertThrows(UnsupportedOperationException.class, () -> edges.put(v1, EdgeSet.from(Set.of(e1))));
    }

    @Test
    void verticesMethodReturnsUnmodifiableSet() {
        PlainGraph graph = new PlainGraph(vertices, edgeMap);
        Set<Vertex> verts = graph.vertices();

        assertEquals(vertices, verts);

        assertThrows(UnsupportedOperationException.class, () -> verts.add(new Vertex("v4")));
    }

    @Test
    void vertexPairInvertWorks() {
        VertexPair pair = new AdjacencySets.VertexPair(v1, v2);
        
        VertexPair inverted = pair.invert();
        assertEquals(v2, inverted.u());
        assertEquals(v1, inverted.v());
        assertEquals(v1, pair.u());
        assertEquals(v2, pair.v());

        VertexPair reversed = new AdjacencySets.VertexPair(v2, v1);
        assertEquals(reversed.u(), inverted.u());
        assertEquals(reversed.v(), inverted.v());
    }

    @Test
    void testFromValidAdjSet() {
        Optional<AdjacencySets> optional = AdjacencySets.from(edgeMap, status);
        assertTrue(optional.isPresent(), "Valid adjacency sets should produce a non-empty Optional");
    }

    @Test
    void testFromInvalidAdjSet() {
        Map<Vertex, EdgeSet> badMap = new HashMap<>();
        badMap.put(v1, null); 
        Optional<AdjacencySets> optional = AdjacencySets.from(badMap, status);
        assertTrue(optional.isEmpty(), "Invalid adjacency sets should return empty Optional");
    }

    @Test
    void testEdgesAndVerticesAdjSet() {
        AdjacencySets adj = AdjacencySets.from(edgeMap, status).orElseThrow();
        Map<Vertex, EdgeSet> edgesView = adj.edges();
        Set<Vertex> verticesView = adj.vertices();

        assertEquals(edgeMap.keySet(), verticesView);
        assertEquals(edgeMap, edgesView);

        assertThrows(UnsupportedOperationException.class, () -> edgesView.put(v1, EdgeSet.from(Set.of())));
        assertThrows(UnsupportedOperationException.class, () -> verticesView.add(new Vertex("v4")));
    }

    @Test
    void testVertexPairInvertAdjSet() {
        AdjacencySets.VertexPair pair = new AdjacencySets.VertexPair(v1, v2);
        AdjacencySets.VertexPair inverted = pair.invert();
        assertEquals(pair.u(), inverted.v());
        assertEquals(pair.v(), inverted.u());
    }

    @Test
    void testAdjSetToString() {
        AdjacencySets adj = AdjacencySets.from(edgeMap, status).orElseThrow();
        String output = adj.toString();
        assertTrue(output.contains("v1"));
        assertTrue(output.contains("v2"));
        assertTrue(output.contains("v3"));
        assertTrue(output.contains("---"));
    }
}

