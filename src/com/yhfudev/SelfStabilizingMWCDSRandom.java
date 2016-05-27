package com.yhfudev;

import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.SinkAdapter;

/**
 * Created by yhfu on 5/22/16.
 */
public class SelfStabilizingMWCDSRandom extends SinkAdapter implements DynamicAlgorithm {
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
        int j;

        int debug_round = 0; // debug

        System.out.println ("DEBUG: START compute");
        //for(n : theGraph.getEachNode() ) {
        int num = theGraph.getNodeCount();
        while (is_changed) {
            debug_round ++;
            processAnimationDelay();
            System.out.println ("DEBUG: +++++++ round: " + debug_round + " SelfStabilizingMWCDSRandom");

            // calculate the next state
            is_changed = false;
            for (i = 0; i < num; i++) {
                node = theGraph.getNode(i);

                s = -1;
                d = -1;
                // set the temp buffer values
                node.setAttribute("s_next", s);
                node.setAttribute("d_next", d);

                s = node.getAttribute("s");
                d = node.getAttribute("d");
                //int idx = Integer.parseInt(node.getAttribute("id"));

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
                if (is_enter) {
                    if (s != 1) { // to detect if the value is changed
                        s = 1;
                        node.setAttribute("s_next", s);
                        System.out.println ("DEBUG: (" + debug_round + ") R1: " + node.getId());
                    }
                } else if (is_leave) {
                    if (s != 0) { // to detect if the value is changed
                        s = 0;
                        node.setAttribute("s_next", s);
                        System.out.println ("DEBUG: (" + debug_round + ") R2: " + node.getId());
                    }
                } else if (is_leave_random) {
                    s = 1;
                    if (Math.random() > 0.5) {
                        s = 0;
                    }
                    // randomized values are always changed item
                    node.setAttribute("s_next", s);
                    System.out.println ("DEBUG: (" + debug_round + ") R3: " + node.getId());
                }
            }
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
                    if (s == 1) {
                        node.addAttribute("ui.class", "memberinset");
                    } else {
                        node.addAttribute("ui.class", "member");
                    }
                }
            }
        }
        System.out.println ("DEBUG: END compute");
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

    protected String sourceId;
    public String getSource() {
        return this.sourceId;
    }
    public void setSource(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * If this delay is positive, sleeps at the end of each pivot and updates UI
     * classes
     */
    protected long animationDelay = 0;
    public void setAnimationDelay(long millis) {
        animationDelay = millis;
    }
    protected void processAnimationDelay() {
        if (animationDelay > 0) {
            try {
                Thread.sleep(animationDelay);
            } catch (InterruptedException e) {
            }
        }
    }
}
