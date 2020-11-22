package ex1.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

import ex1.src.*;

class MyWGraph_DSTest {

    weighted_graph graph = new WGraph_DS();
    weighted_graph graph1;

    @BeforeEach
    /*Clears the graph from any changes that was made in it and rebuilds it with 10 new nodes
    That has keys 0 to 9
     */
    void clearAndBuild() {
        Iterator<node_info> iterator = graph.getV().iterator();
        while (iterator.hasNext()) {
            graph.removeNode(iterator.next().getKey());
        }
        for (int i = 0; i < 10; i++) {
            graph.addNode(i);
        }
    }

    @org.junit.jupiter.api.Test
    void getNode() {
        node_info n1 = graph.getNode(1);
        node_info n2 = graph.getNode(2);
        node_info n3 = graph.getNode(3);

        node_info n_1 = graph.getNode(1);
        node_info n_2 = graph.getNode(2);
        node_info n_3 = graph.getNode(3);

        assertSame(n1, n_1);
        assertSame(n2, n_2);
        assertSame(n3, n_3);
        assertNotSame(n1, n2);
        assertNotSame(n2, n3);
        assertNotSame(n3, n1);


    }

    @org.junit.jupiter.api.Test
    void hasEdge() {
        graph.connect(0, 1, 3);
        graph.connect(1, 2, 3);
        graph.connect(2, 3, 3);
        graph.connect(3, 4, -9);
        Assertions.assertTrue(graph.hasEdge(0, 1));
        Assertions.assertTrue(graph.hasEdge(1, 0));
        Assertions.assertTrue(graph.hasEdge(1, 2));
        Assertions.assertTrue(graph.hasEdge(2, 1));
        Assertions.assertTrue(graph.hasEdge(2, 3));
        Assertions.assertTrue(graph.hasEdge(3, 2));
        Assertions.assertFalse(graph.hasEdge(0, 2));
        Assertions.assertFalse(graph.hasEdge(2, 0));
        Assertions.assertFalse(graph.hasEdge(0, 3));
        Assertions.assertFalse(graph.hasEdge(3, 0));
        Assertions.assertFalse(graph.hasEdge(1, 3));
        Assertions.assertFalse(graph.hasEdge(3, 1));
        Assertions.assertFalse(graph.hasEdge(0, 0));
        Assertions.assertFalse(graph.hasEdge(1, 1));
        Assertions.assertFalse(graph.hasEdge(2, 2));
        Assertions.assertFalse(graph.hasEdge(3, 3));
        Assertions.assertFalse(graph.hasEdge(3, 4));
    }

    @org.junit.jupiter.api.Test
    void getEdge() {
        graph.connect(0, 1, 1);
        graph.connect(1, 2, 2);
        graph.connect(2, 3, 3);
        graph.connect(3, 4, -4);
        Assertions.assertEquals(graph.getEdge(0, 1), 1);
        Assertions.assertEquals(graph.getEdge(1, 0), 1);
        Assertions.assertEquals(graph.getEdge(1, 2), 2);
        Assertions.assertEquals(graph.getEdge(2, 1), 2);
        Assertions.assertEquals(graph.getEdge(2, 3), 3);
        Assertions.assertEquals(graph.getEdge(3, 2), 3);
        Assertions.assertEquals(graph.getEdge(0, 2), -1);
        Assertions.assertEquals(graph.getEdge(0, 3), -1);
        Assertions.assertEquals(graph.getEdge(0, 4), -1);
        Assertions.assertEquals(graph.getEdge(1, 4), -1);
        Assertions.assertEquals(graph.getEdge(3, 4), -1);


    }

    @org.junit.jupiter.api.Test
    void addNode() {
        graph.addNode(0);
        graph.addNode(1);
        graph.addNode(1000);
        graph.addNode(1);
        graph.addNode(-1);
    }

    @org.junit.jupiter.api.Test
    void connect() {
        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.connect(1, 2, 3);
        graph.connect(1, 3, 5);
        graph.connect(1, 1, 10);
        graph.connect(5, 10, 1);
        graph.connect(1, 3, 2);

    }

    @org.junit.jupiter.api.Test
    void getV() {
        Collection<node_info> nodes = graph.getV();
        Iterator<node_info> iter = nodes.iterator();
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(iter.next().getKey(), i);
        }
        WGraph_DS g = new WGraph_DS();
        for (int i = 0; i < 10; i++) {
            g.addNode(i);
        }
        for (int i = 0; i < 10; i++) {
            assertNotSame(graph.getNode(i), g.getNode(i));
        }
    }

    @org.junit.jupiter.api.Test
    void testGetV() {
        graph.connect(1, 2, 1);
        graph.connect(1, 3, 1);
        Collection nei1 = graph.getV(1);
        Collection nei2 = graph.getV(2);
        Collection nei3 = graph.getV(3);
        Collection nei5 = graph.getV(5);
        Collection nei10 = graph.getV(10);
        assertNotSame(nei1, nei2);
        assertNotSame(nei1, nei3);
        assertEquals(nei1.size(), 2);
        assertEquals(nei3.size(), 1);
        assertEquals(nei3.size(), nei2.size());
        assertEquals(nei5.size(), 0);
        assertNull(nei10);
    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        graph.removeNode(1);
        graph.removeNode(2);
        assertNull(graph.getNode(1));
        assertNull(graph.getNode(2));
        graph.connect(3, 4, 1);
        Assertions.assertTrue(graph.hasEdge(3, 4));
        graph.removeNode(3);
        Assertions.assertFalse(graph.hasEdge(3, 4));
        assertNull(graph.removeNode(20));
    }

    @org.junit.jupiter.api.Test
    void removeEdge() {
        graph.connect(1, 2, 1);
        graph.connect(2, 3, 2);
        Assertions.assertTrue(graph.hasEdge(1, 2));
        Assertions.assertTrue(graph.hasEdge(2, 3));
        Assertions.assertFalse(graph.hasEdge(1, 3));
        graph.removeEdge(1, 2);
        Assertions.assertFalse(graph.hasEdge(1, 2));
        Assertions.assertFalse(graph.hasEdge(2, 1));
        Assertions.assertTrue(graph.hasEdge(2, 3));
        graph.removeEdge(1, 2);
        graph.removeEdge(3, 4);
        Assertions.assertEquals(graph.getEdge(1, 2), -1);
        Assertions.assertEquals(graph.getEdge(2, 1), -1);
    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
        Assertions.assertEquals(graph.nodeSize(), 10);
        graph.removeNode(1);
        Assertions.assertEquals(graph.nodeSize(), 9);
        graph.addNode(1);
        Assertions.assertEquals(graph.nodeSize(), 10);
        WGraph_DS g1 = new WGraph_DS();
        Assertions.assertEquals(g1.nodeSize(), 0);

    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
        Assertions.assertEquals(graph.edgeSize(), 0);
        graph.connect(0, 1, 0);
        graph.connect(1, 2, 1);
        Assertions.assertEquals(graph.edgeSize(), 2);
        graph.removeNode(1);
        Assertions.assertEquals(graph.edgeSize(), 0);
        graph.addNode(1);
        graph.connect(1, 0, 0);
        Assertions.assertEquals(graph.edgeSize(), 1);
        graph.connect(1, 2, 0);
        Assertions.assertEquals(graph.edgeSize(), 2);


    }

    @org.junit.jupiter.api.Test
    void getMC() {
        Assertions.assertEquals(graph.getMC(), 10);
        graph.removeNode(3);
        Assertions.assertEquals(graph.getMC(), 11);
        graph.connect(0, 1, 0);
        Assertions.assertEquals(graph.getMC(), 12);
        graph.connect(1, 2, 0);
        Assertions.assertEquals(graph.getMC(), 13);
        graph.removeNode(1);
        Assertions.assertEquals(graph.getMC(), 16);

    }

    @Test
    void equals() {
        graph1 = graph;
        Assertions.assertTrue(graph.equals(graph1));
        graph1 = new WGraph_DS();
        Iterator<node_info> iterator = graph1.getV().iterator();
        while (iterator.hasNext()) {
            graph1.removeNode(iterator.next().getKey());
        }
        Assertions.assertFalse(graph.equals(graph1));
        for (int i = 0; i < 10; i++) {
            graph1.addNode(i);
        }
        Assertions.assertTrue(graph.equals(graph1));
        graph.connect(1, 2, 1);
        Assertions.assertFalse(graph.equals(graph1));
        graph1.connect(1, 2, 2);
        Assertions.assertFalse(graph.equals(graph1));
        graph1.connect(1, 2, 1);
        Assertions.assertTrue(graph.equals(graph1));
        graph1 = null;
        Assertions.assertFalse(graph.equals(graph1));
        graph = null;
        //assertFalse(graph.equals(graph1));


    }
}