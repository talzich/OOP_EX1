package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

    weighted_graph myGraph, copy;
    HashMap<node_info, node_info> parents = new HashMap();

    /**
     * Sets the graph this object will be operating on to the specified graph.
     *
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        if (g == null) return;
        this.myGraph = g;
    }//v

    /**
     * @return A pointer to the ex1.src.weighted_graph this object operates on.
     */
    @Override
    public weighted_graph getGraph() {
        return myGraph;
    }//v

    /**
     * Iterates through an original graph and produces a deep copy of it. This time, the copied graph DOES NOT share the
     * mode counter with the original graph.
     *
     * @return A new graph, which is a deep copy of the graph this object operates on.
     */
    @Override
    public weighted_graph copy() {
        if (myGraph == null) return null;
        node_info origPointer, copyPointer;
        copy = new WGraph_DS();
        Iterator<node_info> origin = myGraph.getV().iterator();
        Iterator<node_info> neiIter;
        while (origin.hasNext()) {
            origPointer = origin.next();
            if (copy.getNode(origPointer.getKey()) == null) {
                addCopiedNode(copy, origPointer);
//                copy.addNode(origPointer.getKey());
//                copyPointer = copy.getNode(origPointer.getKey());
//                copyPointer.setInfo(origPointer.getInfo());
//                copyPointer.setTag(origPointer.getTag());
            }
            neiIter = myGraph.getV(origPointer.getKey()).iterator();
            while (neiIter.hasNext()) {
                node_info nei = neiIter.next();
                if (copy.getNode(nei.getKey()) == null) {
                    addCopiedNode(copy, nei);
//                    copy.addNode(nei.getKey());
//                    copyPointer = copy.getNode(nei.getKey());
//                    copyPointer.setInfo(nei.getInfo());
//                    copyPointer.setTag(nei.getTag());
                }
                copy.connect(origPointer.getKey(), nei.getKey(), myGraph.getEdge(origPointer.getKey(), nei.getKey()));
            }
        }
        return copy;
    }//Tested

    /**
     * Iterates through the first connected component we encounter in this graph and compare the number of nodes
     * in it to the number of nodes in the entire graph.
     *
     * @return True if and only if the graph is connected i.e. for every given node in the graph, there is a path
     * to every other node in the graph.
     */
    @Override
    public boolean isConnected() {
        if (myGraph == null) return false;
        //If graph has 1 or less nodes, the graph is vacuously connected
        if (isGraphEmpty(myGraph) || myGraph.nodeSize() == 1) return true;
        /*
            This method relies heavily on the nodes' tags. Therefore, we need to know that all the tags are
            zeroed before starting the process.
         */
        zeroAllTags(myGraph);

        /*
            This iterator will iterate through the first connected component in the graph
         */
        Iterator<node_info> vIterator = myGraph.getV().iterator();
        //Will keep track of the size of the connected component
        int size = 0;
        Queue<node_info> neiQueue = new LinkedList<>();
        node_info pointer = vIterator.next();
        neiQueue.add(pointer);
        pointer.setTag(1);
        while (!neiQueue.isEmpty()) {
            pointer = neiQueue.poll();
            size++;
            vIterator = myGraph.getV(pointer.getKey()).iterator();
            while (vIterator.hasNext()) {
                pointer = vIterator.next();
                if (pointer.getTag() != 1) {
                    neiQueue.add(pointer);
                    pointer.setTag(1);
                }

            }

        }
        return (size == myGraph.nodeSize());
    }//Tested x2

    /**
     * This methods receives two keys as parameters; A source node key, and a destination node key.
     * The method goes on the check what is the shortest distance between those two nodes, taking
     * into account the weight of each edge in the graph.
     * This method also builds a Hashmap of nodes and their "closest" parent to later be used in shortestPath method.
     * This method implements Dijkstra's Algorithm, as seen in the Coursera video (see Readme file for all sources)
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return The shortest distance between node with key src to node with key dest
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        //Return null if the graph was not initialized
        if (myGraph == null) return -1;

        node_info source = myGraph.getNode(src);
        node_info destination = myGraph.getNode(dest);
        if (source == null || destination == null) return -1;
        //Distance from a node to itself is 0
        if (source == destination) return 0;

        //Will be used to track distance from src to every other node
        double dist;

        //Both strings will be used to mark which nodes were already enqueued
        String b = "Black";
        String w = "White";

        node_info pointer, nei; //Pointers
        Iterator<node_info> neiIter;
        double edgeWeight;//Will store the weight of the edge between pointer and nei (see above)
        PriorityQueue<OrderedPair> queue = new PriorityQueue();

        /*This method relies heavily on each node's tag and info so we need to know all of their values before starting
            The following two methods help us 'zero' all tags and infos to values that we will later use to keep track
            of visited nodes and current known distance
         */
        infiAllTags(myGraph);
        blackenAllNodes(myGraph, b);

        //Source's distance from itself is 0
        source.setTag(0);

        //Our PQ stores pairs of nodes and their currently known distance from source
        OrderedPair<node_info, Double> srcCouple = new OrderedPair(source, source.getTag());
        OrderedPair<node_info, Double> nextStep;

        //Dijkstra's Algorithm
        queue.add(srcCouple);
        while (!queue.isEmpty()) {
            pointer = (node_info) queue.remove().getLeft();
            if (pointer == destination) return pointer.getTag();
            if (pointer.getInfo() == b) {
                pointer.setInfo(w);
                neiIter = myGraph.getV(pointer.getKey()).iterator();
                while (neiIter.hasNext()) {
                    nei = neiIter.next();
                    if (nei.getInfo() == b) {
                        edgeWeight = myGraph.getEdge(pointer.getKey(), nei.getKey());
                        dist = pointer.getTag() + edgeWeight;

                        if (nei.getTag() > dist) ;
                        {
                            nei.setTag(dist);
                            nextStep = new OrderedPair<>(nei, nei.getTag());
                            queue.add(nextStep);
                            parents.put(nei, pointer);
                        }
                    }
                }
            }
        }

        if (destination.getTag() == Double.MAX_VALUE * 2) return -1; // In case source and dist are not connected
        return destination.getTag();
    }

    /**
     * This method relies heavily on the shortestPathDist method and will not work without it.
     * While shortestPathDist is running, it stores for each node its "closest" parent i.e. the parent node that is
     * a part of the shortest path.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return A LinkedList of nodes, starting at source going through the shortest path and ending in destination.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        /*
         * We will add to that list in reverse order i.e. the shortest path from dest to src and we will reverse it
         * back before returning it.
         */
        LinkedList<node_info> path = new LinkedList<>();

        /*
            shortestPathDist will return -1 if src and dest are not connected, i.e. there is no path between them.
            Therefore, we will return an empty list if both nodes exist but simply not connected
            or null if one of the nodes does not exist.
         */
        if (shortestPathDist(src, dest) == -1) {
            if (myGraph.getNode(src) == null || myGraph.getNode(dest) == null) return null;

            else return path;
        }

        node_info destination = myGraph.getNode(dest);
        node_info source = myGraph.getNode(src);
        node_info pointer = destination;
        //Adding to the list
        while (true) {
            path.add(pointer);
            if (pointer == source) break;
            pointer = parents.get(pointer);
        }
        //reversing and returning the list.
        return reverseList(path);
    }

    @Override
    public boolean save(String file) {
        try {
            FileWriter fw = new FileWriter(file);
            PrintWriter outs = new PrintWriter(fw);
            outs.println(this.getClass().getSimpleName() + " Contains " + myGraph.nodeSize() + " nodes and " + myGraph.edgeSize() + " edges\n");
            node_info pointer, neiPointer;
            Iterator<node_info> nodeIter = myGraph.getV().iterator();
            Iterator<node_info> neiIter;
            while (nodeIter.hasNext()) {
                pointer = nodeIter.next();
                outs.print("[ " + pointer.getKey() + " , " + pointer.getInfo() + " , " + pointer.getTag() + " ] ");
                outs.print("\tNeighbors:");
                neiIter = myGraph.getV(pointer.getKey()).iterator();
                while (neiIter.hasNext()) {
                    neiPointer = neiIter.next();
                    outs.print("[ " + neiPointer.getKey() + " , " + myGraph.getEdge(pointer.getKey(), neiPointer.getKey()) + " ] ");

                }
                outs.println("\n");
            }
            fw.close();
            outs.close();
        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
        return true;
    }

    @Override
    public boolean load(String file) {
        myGraph = new WGraph_DS();
        String line;
        StringTokenizer tokenizer;
        int index = 0;
        try {
            Scanner scan = new Scanner(new File(file));
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                index++;
                if (line.isBlank()) continue;
                if (index >= 2) {
                    ArrayList<String> words = new ArrayList<>();
                    tokenizer = new StringTokenizer(line);
                    while (tokenizer.hasMoreTokens()) {
                        words.add(tokenizer.nextToken());
                    }
                    int currKey = Integer.parseInt(words.get(1));
                    Double currTag = Double.parseDouble(words.get(5));
                    String currInfo = words.get(3);
                    myGraph.addNode(currKey);
                    node_info curr = myGraph.getNode(currKey);
                    curr.setInfo(currInfo);
                    curr.setTag(currTag);
                    for (int i = 8; i < words.size(); i = i + 5) {
                        int neiKey = Integer.parseInt(words.get(i));
                        Double edgeWeight = Double.parseDouble(words.get(i + 2));
                        myGraph.connect(currKey, neiKey, edgeWeight);
                    }
                }
            }
            scan.close();
        } catch (Exception e) {
            System.err.println("Error");
            return false;
        }
        return true;
    }

    private void zeroAllTags(weighted_graph g) {
        Iterator<node_info> iterator = g.getV().iterator();
        node_info pointer;
        while (iterator.hasNext()) {
            pointer = iterator.next();
            pointer.setTag(0);
        }
    }

    private void infiAllTags(weighted_graph g) {
        if (g == null) return;
        Iterator<node_info> nodes = g.getV().iterator();
        while (nodes.hasNext()) {
            nodes.next().setTag(Double.MAX_VALUE * 2);
        }
    }

    private void blackenAllNodes(weighted_graph g, String flag) {
        if (g == null) return;
        Iterator<node_info> nodes = g.getV().iterator();
        while (nodes.hasNext()) {
            nodes.next().setInfo(flag);
        }
    }

    private LinkedList reverseList(LinkedList list) {
        if (list == null) return null;
        if (list.isEmpty()) return null;
        LinkedList reverse = new LinkedList();
        Iterator iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            reverse.add(iterator.next());
        }
        return reverse;

    }

    private void addCopiedNode(weighted_graph dupGraph, node_info originalNode) {
        dupGraph.addNode(originalNode.getKey());
        node_info copyPointer = dupGraph.getNode(originalNode.getKey());
        copyPointer.setInfo(originalNode.getInfo());
        copyPointer.setTag(originalNode.getTag());
    }

    private boolean isGraphEmpty(weighted_graph g) {
        return g.getV().isEmpty();
    }

}
