package ex1.src;

import java.util.*;

public class WGraph_DS implements weighted_graph {

    public class NodeInfo implements node_info {

        private int key = 0;
        private String info = "hi";
        private double tag = 0.0;

        /**
         * A Simple constructor, you need to provide a key in order to construct a new node.
         *
         * @param key
         */
        public NodeInfo(int key) {
            this.key = key;
        }

        /**
         * Implements ex1.src.node_info
         *
         * @return The key of this node
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * Implements ex1.src.node_info
         *
         * @return The info of this node
         */
        @Override
        public String getInfo() {
            return info;
        }

        /**
         * Sets the info of this node to the given String
         *
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * Implements ex1.src.node_info
         *
         * @return The tag of this node
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * Sets the info of this node to the given double
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        @Override
        public String toString() {
            return "NodeInfo{" +
                    "key=" + key +
                    ", info='" + info + '\'' +
                    ", tag=" + tag +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final node_info other = (node_info) obj;

            boolean flag;
            flag = (this.key == other.getKey() && this.info.equals(getInfo()) && this.tag == other.getTag());
            return flag;
        }
    }

    //Contains nodes in this graph. The keys for this map are the keys of the nodes stored at value
    HashMap<Integer, node_info> nodes;

    /*Contains edges in the graph and their weight. The keys for this map are the keys of nodes.
    For each key, the value of this map contains a Hashmap, representing the neighbors of the node
    with the original key. The key of the "neighbors" Hashmap is the key of the neighboring node.
    The value of the "neighbors" Hashmap is an ordered pair in which its left component is the neighboring node and
    its right component is the weight of the edge between the two nodes.
     */
    HashMap<Integer, HashMap<Integer, OrderedPair<node_info, Double>>> edges;

    //Primitive variables we will be returning if asked to. Names are self explanatory.
    private int nodeSize = 0;
    private int edgeSize = 0;
    private int modeCounter;


    public WGraph_DS() {
        nodes = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Implement weighted_grpah and is used to get the specified node, accessing it by its key
     *
     * @param key - the node_id
     * @return The node with the specified key.
     */
    @Override
    public node_info getNode(int key) {
        if (!nodes.containsKey(key)) return null;
        return nodes.get(key);
    }//Tested

    /**
     * Implements weighted graph.
     *
     * @param node1
     * @param node2
     * @return True if and only if there is an edge between node with key node1 and node with key node2
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (node1 == node2 || node1 < 0 || node2 < 0) return false;
        return edges.get(node1).containsKey(node2);
    }//Tested

    /**
     * Implements ex1.src.weighted_graph
     *
     * @param node1
     * @param node2
     * @return If no such edge exists: -1;
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (node1 == node2 || node1 < 0 || node2 < 0) return -1;
        if (!edges.get(node1).containsKey(node2) || !nodes.containsKey(node1) || !nodes.containsKey(node2)) return -1;
        return (Double) edges.get(node1).get(node2).getRight();
    }//Tested

    /**
     * Adds the node with the specifed key to this graph
     *
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (key < 0 || nodes.containsKey(key)) return;
        node_info newNode = new NodeInfo(key);
        HashMap<Integer, OrderedPair<node_info, Double>> neighbours = new HashMap<>();
        nodes.put(key, newNode);
        edges.put(key, neighbours);
        modeCounter++;
        nodeSize++;
    }//Tested

    /**
     * Adds node1 to node2's adjacency list and vice versa. Also stores the weight of the
     * edge that is connecting the two nodes.
     *
     * @param node1 is the key of one of the nodes.
     * @param node2 is the key of one of the nodes.
     * @param w     is the weight of the edge connecting the two nodes.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (node1 == node2 || node1 < 0 || node2 < 0 || w < 0
                || !nodes.containsKey(node1) || !nodes.containsKey(node2))
            return;
        if (edges.get(node1).containsKey(node2)) edgeSize--;
        OrderedPair<node_info, Double> oneToTwo = new OrderedPair<>(nodes.get(node2), w);
        OrderedPair<node_info, Double> twoToOne = new OrderedPair<>(nodes.get(node1), w);
        HashMap oneNei = edges.get(node1);
        HashMap twoNei = edges.get(node2);
        oneNei.put(node2, oneToTwo);
        twoNei.put(node1, twoToOne);
        modeCounter++;
        edgeSize++;
        return;


    }//Tested x2

    /**
     * @return A collection of the nodes in this graph
     */
    @Override
    public Collection<node_info> getV() {
        return nodes.values();
    }//Tested

    /**
     * Get the list of neighbors of node with key node_id
     *
     * @param node_id
     * @return A LinkedList of ex1.src.node_info's
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (!nodes.containsKey(node_id)) return null;
        Collection<node_info> v = new LinkedList<>();
        Iterator<OrderedPair<node_info, Double>> iterator = edges.get(node_id).values().iterator();
        while (iterator.hasNext()) {
            v.add((node_info) iterator.next().getLeft());
        }
        return v;
    }//Tested

    /**
     * Remove the node with the specified key and remove all the edges that were connected to it
     *
     * @param key
     * @return A pointer to the removed node.
     */
    @Override
    public node_info removeNode(int key) {
        if (!nodes.containsKey(key)) return null;

        Iterator<Integer> iterator = edges.get(key).keySet().iterator();
        while (iterator.hasNext()) {
            edges.get(iterator.next()).remove(key);
            edgeSize--;
            modeCounter++;
        }
        modeCounter++;
        nodeSize--;
        edges.get(key).clear();
        return nodes.remove(key);
    }

    /**
     * Disconnects node with key node1 and node with key node2
     *
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (node1 == node2 || !nodes.containsKey(node1) || !nodes.containsKey(node2)
                || !edges.get(node1).containsKey(node2)) return;
        edges.get(node1).remove(node2);
        edges.get(node2).remove(node1);
        modeCounter++;
        edgeSize--;
    }

    /**
     * A simple equals method. Checks to see whether this graph and another graph are equal.
     * Two graph being equal means that all their nodes have the same keys,all nodes are connected to the
     * same neighbors and with the same edge weight.
     *
     * @param obj
     * @return True iff the graphs are equal by the terms mentioned above.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final weighted_graph other = (weighted_graph) obj;

        if (this.nodeSize != other.nodeSize() || this.edgeSize != other.edgeSize()) return false;

        Iterator<node_info> nodeIter = this.getV().iterator();
        Iterator<node_info> neiIter;
        node_info pointer, neiPointer;
        while (nodeIter.hasNext()) {
            pointer = nodeIter.next();
            if (other.getNode(pointer.getKey()) == null) return false;
            if (!other.getNode(pointer.getKey()).equals(pointer)) return false;
            neiIter = this.getV(pointer.getKey()).iterator();
            while (neiIter.hasNext()) {
                neiPointer = neiIter.next();
                if (!other.hasEdge(neiPointer.getKey(), pointer.getKey())) return false;
                if (other.getEdge(neiPointer.getKey(), pointer.getKey()) != this.getEdge(neiPointer.getKey(), pointer.getKey()))
                    return false;
                //if(!neiPointer.equals(other.getNode(neiPointer.getKey()))); return false;
            }
        }

        return true;
    }

    @Override
    public int nodeSize() {
        return nodeSize;
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return modeCounter;
    }

    @Override
    public String toString() {
        return "ex1.src.WGraph_DS{" +
                "nodeSize=" + nodeSize +
                ", edgeSize=" + edgeSize +
                ", modeCounter=" + modeCounter +
                '}';
    }

}


