package ex1.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

import ex1.src.*;

class MyWGraph_AlgoTest {

    weighted_graph graph = new WGraph_DS();
    weighted_graph_algorithms alg = new WGraph_Algo();

    @BeforeEach
    /*Clears the graph from any changes that was made in it and rebuilds it with 10 new nodes
    That has keys 0 to 4
     */
    void clearAndBuild() {
        Iterator<node_info> iterator = graph.getV().iterator();
        while (iterator.hasNext()) {
            graph.removeNode(iterator.next().getKey());
        }
        for (int i = 0; i < 5; i++) {
            graph.addNode(i);
        }

        alg.init(graph);
    }

    @Test
    void isConnected() {
        Assertions.assertFalse(alg.isConnected());
        graph.connect(0, 1, 1);
        graph.connect(1, 2, 1);
        graph.connect(2, 3, 1);
        graph.connect(3, 4, 1);
        Assertions.assertTrue(alg.isConnected());
        graph.removeEdge(1, 2);
        Assertions.assertFalse(alg.isConnected());
        graph.connect(2, 1, 1);
        Assertions.assertTrue(alg.isConnected());
        graph.removeEdge(0, 1);
        Assertions.assertFalse(alg.isConnected());
        graph.removeNode(0);
        Assertions.assertTrue(alg.isConnected());
        graph.addNode(0);
        Assertions.assertFalse(alg.isConnected());
        graph.connect(0, 0, 0);
        Assertions.assertFalse(alg.isConnected());
        graph.connect(0, 4, 1);
        Assertions.assertTrue(alg.isConnected());
        graph.connect(0, 3, 1);
        graph.connect(0, 2, 1);
        graph.connect(0, 1, 1);
        Assertions.assertTrue(alg.isConnected());
        graph.removeEdge(1, 2);
        graph.removeEdge(2, 3);
        graph.removeEdge(3, 4);
        Assertions.assertTrue(alg.isConnected());
        graph.removeNode(0);
        Assertions.assertFalse(alg.isConnected());

    }

    @Test
    void shortestPathDist() {
        Assertions.assertEquals(alg.shortestPathDist(0, 4), -1);
        graph.connect(0, 1, 2);
        graph.connect(1, 2, 1);
        graph.connect(1, 4, 6);
        graph.connect(2, 3, 2);
        graph.connect(3, 4, 1);
        Assertions.assertEquals(alg.shortestPathDist(0, 4), 6);
        graph.connect(1, 2, 10);
        Assertions.assertEquals(alg.shortestPathDist(0, 4), 8);
        graph.removeNode(4);
        Assertions.assertEquals(alg.shortestPathDist(0, 4), -1);
    }

    @Test
    void shortestPath() {
        graph.connect(0, 1, 2);
        graph.connect(1, 2, 1);
        graph.connect(1, 4, 6);
        graph.connect(2, 3, 2);
        graph.connect(3, 4, 1);
        LinkedList myList = new LinkedList();
        myList.add(graph.getNode(0));
        myList.add(graph.getNode(1));
        myList.add(graph.getNode(2));
        myList.add(graph.getNode(3));
        myList.add(graph.getNode(4));
        Assertions.assertEquals(myList, alg.shortestPath(0, 4));
    }

    @Test
    void copy() {
        graph.connect(0, 1, 2);
        graph.connect(1, 2, 1);
        graph.connect(1, 4, 6);
        graph.connect(2, 3, 2);
        graph.connect(3, 4, 1);
        alg.init(graph);
        weighted_graph copy = alg.copy();
        Assertions.assertEquals(graph, copy);
        assertNotSame(graph, copy);
        graph.removeNode(3);
        Assertions.assertNotEquals(graph, copy);
        assertNotSame(graph, copy);
        copy.removeNode(3);
        Assertions.assertEquals(graph, copy);
    }

    @Test
    void save_load() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.connect(0, 1, 2.5);
        g.connect(0, 2, 4.4);
        g.connect(2, 1, 0.25);
        g.connect(3, 1, 3);
        g.connect(3, 2, 1);
        alg.init(g);
        alg.save("myGraph.txt");
        alg.load("myGraph.txt");
        graph = alg.getGraph();
        boolean flag = g.equals(graph);
        assertTrue(flag);

    }

}