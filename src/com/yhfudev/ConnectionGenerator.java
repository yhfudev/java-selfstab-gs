package com.yhfudev;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Create a connected graph with assigned degree
 */
public class ConnectionGenerator extends BaseGenerator {
    int currentIndex = 0;
    int degree = 2;
    Graph theGraph;
    boolean connectToSelf = false;
    boolean isFixDegree = false;

    // g is the graph
    // degree is the expected degree of each node
    // connectToSelf = true if connect to self edge is allowed
    // isFixDegree = true if the degree of each node should exactly equal to the 'degree'
    public ConnectionGenerator(Graph g, int degree_init, boolean connectToSelf_init, boolean isFixDegree_init)
    {
        degree = degree_init;
        theGraph = g;
        connectToSelf = connectToSelf_init;
        isFixDegree = isFixDegree_init;
        setUseInternalGraph(true);
    }

    public void begin() {
        addNode();
    }

    public boolean nextEvents() {
        addNode();
        return true;
    }

    public void end() {
        connectAllNodes();
    }

    protected void addNode() {
        int thisIndex = currentIndex;
        currentIndex ++;
        sendNodeAdded(sourceId, Integer.toString(thisIndex));
        if (thisIndex == 0) {
            return;
        }
        // connect to a randomly selected node
        Node node;
        String edgeId;
        int randomNum = 0;
        int numNodes = this.theGraph.getNodeSet().size();
        int tryTimes = 0;
        int i = 0;
        int startRand;
        startRand = this.random.nextInt(numNodes);
        while (true) {
            tryTimes ++;
            randomNum = this.random.nextInt(numNodes);
            if (tryTimes > numNodes * 2) {
                if (i >= numNodes) {
                    System.out.println ("DEBUG: Can't connect the node " + thisIndex + " to graph sz=" + numNodes);
                    return;
                }
                randomNum = (startRand + i) % numNodes;
                i ++;
            }
            node = this.theGraph.getNode(randomNum);
            if (node.getDegree() >= degree) {
                continue;
            }
            if (! node.getId().equals(Integer.toString(thisIndex))) {
                edgeId = "" + Integer.toString(thisIndex) + "-" + node.getId();
                sendEdgeAdded(sourceId, edgeId, Integer.toString(thisIndex), node.getId(), false);
                break;
            }
        }
        if (! isFixDegree) {
            // connect to another nodes base on preset degree randomly
            for (i = 1; i < degree; i++) {
                if (Math.random() > 0.7) {
                    randomNum = this.random.nextInt(numNodes);
                    node = this.theGraph.getNode(randomNum);
                    System.out.println ("DEBUG: add edge to the node " + node.getId());
                    // check the degrees of part of nodes and connect to them
                    if (node.getDegree() < degree) {
                        // check if the edge exist
                        edgeId = "" + Integer.toString(thisIndex) + "-" + node.getId();
                        Edge edge = theGraph.getEdge(edgeId);
                        if (null == edge) {
                            // check if the id is self
                            if ((node.getId().equals(Integer.toString(thisIndex))) && (!connectToSelf)) {
                                break;
                            }
                            sendEdgeAdded(sourceId, edgeId, Integer.toString(thisIndex), node.getId(), false);
                        }
                    }
                }
            }
        }
    }

    protected void connectAllNodes() {
        if (! isFixDegree) {
            return;
        }
        // all the nodes are connected
        int i;
        int j;
        int randomNum = 0;
        int startRand = 0;
        Node node;
        Node node_peer;
        String edgeId;
        int numNodes;
        int tryTimes;
        numNodes = this.theGraph.getNodeSet().size();
        startRand = this.random.nextInt(numNodes);
        for (i = 0; i < theGraph.getNodeCount(); i ++) {
            node = this.theGraph.getNode(i);
            System.out.println ("DEBUG: add edges for the node " + node.getId());
            j = 0;
            for ( tryTimes = 0; (node.getDegree() < degree); tryTimes ++) {
                randomNum = this.random.nextInt(numNodes);
                if (tryTimes > numNodes * 2) {
                    if (j >= numNodes) {
                        System.out.println ("DEBUG: Can't connect the node " + node.getId() + " to degree " + degree);
                        break;
                    }
                    randomNum = (startRand + j) % numNodes;
                    System.out.println ("DEBUG: node " + node.getId() + "sequence try peer node " + randomNum);
                    j ++;
                }
                // check if the id is self
                //if (connectToSelf || (!node.getId().equals(node_peer.getId()))) {
                if (connectToSelf || (randomNum != i)) {
                    node_peer = this.theGraph.getNode(randomNum);
                    if (node_peer.getDegree() < degree) {
                        // check if the edge exist
                        edgeId = "" + node.getId() + "-" + node_peer.getId();
                        Edge edge = theGraph.getEdge(edgeId);
                        if (null == edge) {
                            edgeId = "" + node_peer.getId() + "-" + node.getId();
                            edge = theGraph.getEdge(edgeId);
                        }
                        if (null == edge) {
                            sendEdgeAdded(sourceId, edgeId, node.getId(), node_peer.getId(), false);
                        }
                    }
                }
            }
        }
    }
}