package com.yhfudev;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Created by yhfu on 5/22/16.
 */
public class SelfStabilizingMWCDSRandom extends SinkAlgorithm {
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
        int d = 0;
        int rho = 0;
        //int minMj;
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
            System.out.println ("DEBUG: +++++++ round: " + debug_round + " SelfStabilizingMWCDSRandom");

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

                s = -1;
                d = -1;
                // set the temp buffer values
                node.setAttribute("s_next", s);
                node.setAttribute("d_next", d);

                s = node.getAttribute("s");
                d = node.getAttribute("d");
                if (s != 0) {
                    count_s ++;
                }
                if (d_max < d) {
                    d_max = d;
                }
                //int idx = Integer.parseInt(node.getAttribute("id"));

                boolean has_maximum_degree_adj = false; // detect if there exist a adjacent node which degree > than tha of any adjacent node.
                int maxadj_degree = 0; // the maximum degree value of the adjacent node
                String maxadj_id = "";     // the id of the adjacent node with maximum degree
                boolean is_maximal_degree = true; // detect if the current node's degree is > than that of any adjacent nodes.
                boolean is_enter = false;
                boolean is_leave = false;
                boolean is_leave_random = false;
                boolean is_nomemb = ((s == 0)?true:false); // for all j in N_{<}(i): sj=0
                rho = d;
                //minMj = idx; // this node
                // for each edge
                for (Edge e : node.getEachEdge()) {
                    //System.out.printf("node %d neighbor %s via %s%n", i, e.getOpposite(node).getId(), e.getId() );
                    Node oppnode = e.getOpposite(node);
                    if (isHeuristicOn()) {
                        int dgr = node.getDegree();
                        int dgr_adj = oppnode.getDegree();
                        if (node.getDegree() <= oppnode.getDegree()) {
                            is_maximal_degree = false;
                            if ((node.getDegree() < oppnode.getDegree()) && (maxadj_degree < oppnode.getDegree())) {
                                maxadj_degree = oppnode.getDegree();
                                maxadj_id = oppnode.getId();
                            }
                        }
                    }

                    // calculate rho_i
                    int oppd = oppnode.getAttribute("d");
                    if (oppd < Integer.MAX_VALUE) {
                        rho = Math.min(oppd + 1, rho);
                    }
                    //if ((oppd != Integer.MAX_VALUE) && (d != Integer.MAX_VALUE))
                    //if (oppd != Integer.MAX_VALUE)
                    if (oppd <= d) {
                        int opps = oppnode.getAttribute("s");
                        if (opps != 0) {
                            is_nomemb = false;
                        }
                        if (s == 1 && opps == 1) {
                            if (oppd < d) {
                                if (opps != 0) {
                                    is_leave = true;
                                }
                            } else {
                                // oppd == d
                                // XXX: detect if one of node exit from the set by the value of s? if(s1 == s2)?
                                if (opps != 0) {
                                    is_leave_random = true;
                                }
                            }
                        }
                    }
                }
                if (d != rho) { // to detect if the value is changed
                    node.setAttribute("d_next", rho);
                }
                // calculate enter_i
                is_enter = is_nomemb;

                if (isHeuristicOn()) {
                    // note the adjacent maximum node
                    if (maxadj_degree > 0) {
                        Node oppnode = theGraph.getNode(maxadj_id);
                        oppnode.setAttribute("h", 1);
                    }
                }

                if (is_enter) {
                    if (s != 1) { // to detect if the value is changed
                        s = 1;
                        node.setAttribute("s_next", s);
                        System.out.println ("DEBUG: (" + debug_round + ") R1: [" + node.getId() + "] s=" + s);
                    }
                } else if (is_leave) {
                    if (s != 0) { // to detect if the value is changed
                        s = 0;
                        node.setAttribute("s_next", s);
                        System.out.println ("DEBUG: (" + debug_round + ") R2: [" + node.getId() + "] s=" + s);
                    }
                } else if (is_leave_random) {
                    s = 1;
                    if (isHeuristicOn()) {
                        is_maximal_degree = false;
                        if (node.hasAttribute("h")) {
                            int h = node.getAttribute("h");
                            if (h != 0) {
                                is_maximal_degree = true;
                            }
                        }
                        if (is_maximal_degree) {
                            if (Math.random() < 0.2) {
                                s = 0;
                            }
                        } else {
                            if (Math.random() < 0.8) {
                                s = 0;
                            }
                        }
                    } else {
                        if (Math.random() < 0.5) {
                            s = 0;
                        }
                    }

                    // randomized values are always changed item
                    node.setAttribute("s_next", s);
                    System.out.println ("DEBUG: (" + debug_round + ") R3: [" + node.getId() + "] s=" + s);
                }
            }
            //System.out.println ("DEBUG: total degree=" + count_degree_avg + ", num=" + num + ", avg=" + (count_degree_avg / num));
            count_degree_avg /= num;
            // update the values
            for (i = 0; i < num; i++) {
                node = theGraph.getNode(i);
                s = node.getAttribute("s_next");
                if (s >= 0) {
                    is_changed = true; // DEBUG
                    node.setAttribute("s", s);
                } else {
                    s = node.getAttribute("s");
                }
                d = node.getAttribute("d_next");
                if (d >= 0) {
                    is_changed = true; // DEBUG
                    node.setAttribute("d", d);
                } else {
                    d = node.getAttribute("d");
                }
                node.setAttribute("ui.label", "[" + node.getId() + "] d=" + d + ",s=" + s);
                if (i == 0) {
                    // root
                    if (s == 1) {
                        node.addAttribute("ui.class", "rootinset");
                    } else {
                        node.addAttribute("ui.class", "root");
                    }
                } else {
                    int h = 0;
                    if (isHeuristicOn()) {
                        if (node.hasAttribute("h")) {
                            h = node.getAttribute("h");
                        }
                    }
                    if (h == 1) {
                        node.addAttribute("ui.class", "maximaldegree");
                    } else
                    if (s == 1) {
                        node.addAttribute("ui.class", "memberinset");
                    } else {
                        node.addAttribute("ui.class", "member");
                    }
                }
            }
            //System.out.println ("DEBUG: # of s=" + count_s);
        }
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
        int d;

        if( node.hasAttribute("s") ) {
            s = node.getAttribute("s");
        } else {
            s = 0;
            node.setAttribute("s", s);
        }
        if( node.hasAttribute("d") ) {
            d = node.getAttribute("d");
        } else {
            if (theGraph.getNodeCount() > 1) {
                d = Integer.MAX_VALUE;
            } else {
                // root
                d = 0;
            }
            node.setAttribute("d", d);
        }
        node.setAttribute("ui.label", "[" + node.getId() + "] d=" + d + ",s=" + s);
        node.addAttribute("id", lastId + ""); lastId ++;
        int h = 0;
        if (isHeuristicOn()) {
            if (node.hasAttribute("h")) {
                h = node.getAttribute("h");
            }
        }
        if (h == 1) {
            node.addAttribute("ui.class", "maximaldegree");
        } else
        if (s == 1) {
            node.addAttribute("ui.class", "memberinset");
        } else {
            node.addAttribute("ui.class", "member");
        }
    }

    public void nodeRemoved(String sourceId, long timeId, String nodeId) {
        Node node = theGraph.getNode(nodeId);
        System.out.println ("DEBUG: Delete node: " + node.getId());

        String type = node.getAttribute("ui.class");
        if ("root".equals (type) && (theGraph.getNodeCount() > 0)) {
            node = theGraph.getNode(0);
            node.addAttribute("ui.class", "root");
        }
    }

    // reset all nodes at the end of -init()
    private void resetAllNode () {
        Node node;
        int s;
        int d;
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
                node.setAttribute("s", s);
            }
            if( node.hasAttribute("d") ) {
                d = node.getAttribute("d");
            } else {
                if (i == 0) {
                    d = 0;
                } else {
                    d = Integer.MAX_VALUE;
                }
                node.setAttribute("d", d);
            }
            node.setAttribute("ui.label", "[" + node.getId() + "] d=" + d + ",s=" + s);
            if (i == 0) {
                // root
                if (s == 1) {
                    node.addAttribute("ui.class", "rootinset");
                } else {
                    node.addAttribute("ui.class", "root");
                }
            } else {
                int h = 0;
                if (isHeuristicOn()) {
                    if (node.hasAttribute("h")) {
                        h = node.getAttribute("h");
                    }
                }
                if (h == 1) {
                    node.addAttribute("ui.class", "maximaldegree");
                } else
                if (s == 1) {
                    node.addAttribute("ui.class", "memberinset");
                } else {
                    node.addAttribute("ui.class", "member");
                }
            }
        }
        lastId = i;
    }
    protected int lastId = 0;

}
