package com.yhfudev;

import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.SinkAdapter;

/**
 * base on the self-stabilizing algorithm in "Self-stabilizing Algorithms for Minimal Dominating Sets and Maximal Independent Sets"
 * Created by yhfu on 5/22/16.
 */
public class SelfStabilizingDSLinear extends SinkAlgorithm {
    protected Graph theGraph;

    @Override
    public void terminate() {
        theGraph.removeSink(this);
    }

    @Override
    public void init(Graph graph) {
        theGraph = graph;
        theGraph.addSink(this);
        resetAllNode ();
    }

    @Override
    public void compute() {
        boolean is_changed = true;
        Node node;
        int s = 0;
        int i;

        int count_s = 0;
        int count_degree_min = 0;
        int count_degree_max = 0;
        int count_degree_avg = 0;
        int d_max = 0;
        int debug_round = 0; // debug

        System.out.println ("DEBUG: START compute");
        //for(n : theGraph.getEachNode() ) {
        int num = theGraph.getNodeCount();
        while (is_changed) {
            debug_round ++;
            processAnimationDelay();
            System.out.println ("DEBUG: +++++++ round: " + debug_round + " SelfStabilizingDSLinear");

            // calculate the next state
            count_s = 0;
            count_degree_min = 0;
            count_degree_max = 0;
            count_degree_avg = 0;
            d_max = 0;
            is_changed = false;
            for (i = 0; i < num; i++) {
                node = theGraph.getNode(i);
                s = node.getDegree();
                if (i == 0) {
                    count_degree_max = s;
                    count_degree_min = s;
                }
                if (count_degree_max < s) {
                    count_degree_max = s;
                }
                if (count_degree_min > s) {
                    count_degree_min = s;
                }
                count_degree_avg += s;

                s = node.getAttribute("s");
                if (s != 0) {
                    count_s ++;
                }
                //if (d_max < d) {
                //    d_max = d;
                //}
                //int idx = Integer.parseInt(node.getAttribute("id"));

                int cnt_1 = 0;
                int cnt_0 = 0;
                // for each edge
                for (Edge e : node.getEachEdge()) {
                    //System.out.printf("node %d neighbor %s via %s%n", i, e.getOpposite(node).getId(), e.getId() );
                    Node oppnode = e.getOpposite(node);

                    // count 0 or 1
                    int opps = oppnode.getAttribute("s");
                    if (opps == 0) {
                        cnt_0 ++;
                    } else {
                        cnt_1++;
                    }

                }
                if ((s == 0) && (cnt_0 >= node.getEdgeSet().size())) {
                    s = 1;
                    node.setAttribute("s", s);
                    is_changed = true;
                    System.out.println ("DEBUG: (" + debug_round + ") R1: [" + node.getId() + "] s=" + s);
                } else if ((s == 1) && (cnt_1 >= node.getEdgeSet().size())) {
                    s = 0;
                    node.setAttribute("s", s);
                    is_changed = true;
                    System.out.println ("DEBUG: (" + debug_round + ") R2: [" + node.getId() + "] s=" + s);
                }
            }
            //System.out.println ("DEBUG: total degree=" + count_degree_avg + ", num=" + num + ", avg=" + (count_degree_avg / num));
            count_degree_avg /= num;
            // update the values
            for (i = 0; i < num; i++) {
                node = theGraph.getNode(i);
                s = node.getAttribute("s");
                node.setAttribute("ui.label", "[" + node.getId() + "] s=" + s);
                if (s == 1) {
                    node.addAttribute("ui.class", "memberinset");
                } else {
                    node.addAttribute("ui.class", "member");
                }
            }
        }
        //System.out.println ("DEBUG: END compute. # of (nodes,steps,|S|)=(" + theGraph.getNodeCount() + "," + (debug_round-1) + "," + count_s + ")" );
        result.nodes = theGraph.getNodeCount();
        result.steps = (debug_round-1);
        result.dominating_set_size = count_s;
        result.d_max = d_max;
        result.node_degree_min = count_degree_min;
        result.node_degree_max = count_degree_max;
        result.node_degree_avg = count_degree_avg;
        System.out.println ("DEBUG: END compute. # of (nodes,steps,|S|,d_max,dgre_min,dgre_max,dgre_avg)=("
                + theGraph.getNodeCount()
                + "," + (debug_round-1)
                + "," + count_s
                + "," + d_max
                + "," + count_degree_min
                + "," + count_degree_max
                + "," + count_degree_avg
                + ")" );
    }

    public void nodeAdded(String sourceId, long timeId, String nodeId) {
        Node node = theGraph.getNode(nodeId);
        System.out.println ("DEBUG: Add node: " + node.getId());

        node.addAttribute("ui.class", "member");
        int s;

        if( node.hasAttribute("s") ) {
            s = node.getAttribute("s");
        } else {
            s = 0;
            node.setAttribute("s", s);
        }
        node.setAttribute("ui.label", "[" + node.getId() + "] s=" + s);
        node.addAttribute("id", lastId + ""); lastId ++;
        if (s == 1) {
            node.addAttribute("ui.class", "memberinset");
        } else {
            node.addAttribute("ui.class", "member");
        }
    }

    public void nodeRemoved(String sourceId, long timeId, String nodeId) {
        Node node = theGraph.getNode(nodeId);
        System.out.println ("DEBUG: Delete node: " + node.getId());
    }

    // reset all nodes at the end of -init()
    private void resetAllNode () {
        Node node;
        int s = 0;
        int i;
        //for(n : theGraph.getEachNode() ) {
        int num = theGraph.getNodeCount();
        for (i = 0; i < num; i++) {
            node = theGraph.getNode(i);
            node.addAttribute("id", i + "");
            if( node.hasAttribute("s") ) {
                s = node.getAttribute("s");
            } else {
                s = 0;
                /*if (Math.random() > 0.5) { // only for debug
                    s = 1;
                }*/
                node.setAttribute("s", s);
            }
            node.setAttribute("ui.label", "[" + node.getId() + "] s=" + s);
            if (s == 1) {
                node.addAttribute("ui.class", "memberinset");
            } else {
                node.addAttribute("ui.class", "member");
            }
        }
        lastId = i;
    }
    protected int lastId = 0;
}
