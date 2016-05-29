package com.yhfudev;

import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.SinkAdapter;

/**
 * base on the self-stabilizing algorithm in "A Linear Time Self-stabilizing Algorithm for Minimal Weakly Connected Dominating Sets"
 * Created by yhfu on 5/22/16.
 */
public class SelfStabilizingMWCDSLinear extends SinkAdapter implements DynamicAlgorithm {
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
        int m = 0;
        int rho = 0;
        int minMj;
        int i;
        int j;
        int count_s = 0;

        int debug_round = 0; // debug

        System.out.println ("DEBUG: START compute");
        //for(n : theGraph.getEachNode() ) {
        int num = theGraph.getNodeCount();
        while (is_changed) {
            debug_round ++;
            processAnimationDelay();
            System.out.println ("DEBUG: +++++++ round: " + debug_round + " SelfStabilizingMWCDSLinear");

            // calculate the next state
            count_s = 0;
            is_changed = false;
            for (i = 0; i < num; i++) {
                node = theGraph.getNode(i);

                s = -1;
                d = -1;
                m = -1;
                // set the temp buffer values
                node.setAttribute("s_next", s);
                node.setAttribute("d_next", d);
                node.setAttribute("m_next", m);

                s = node.getAttribute("s");
                d = node.getAttribute("d");
                m = node.getAttribute("m");
                if (s != 0) {
                    count_s ++;
                }
                int idx = Integer.parseInt((String) node.getAttribute("id"));
                if (i == 0) {
                    // root
                    if ((d != 0) || (m != 0) || (s != 1)) {
                        // R0
                        //if (s != 1) {
                        //    is_changed = true;
                        //}
                        s = 1;
                        d = 0;
                        m = 0;
                        node.setAttribute("s_next", s);
                        node.setAttribute("d_next", d);
                        node.setAttribute("m_next", m);

                        System.out.println ("DEBUG: (" + debug_round + ") R0: [" + node.getId() + "] m=" + m + ",d=" + d + ",s=" + s);
                    }
                    continue;
                }

                boolean is_enter = false;
                boolean is_leave = false;
                boolean is_nomemb = true; // for all j in N_{<=}(i): sj=0
                rho = Integer.MAX_VALUE;
                if (m == 1) {
                	minMj = idx; // this node
                } else {
                	minMj = Integer.MAX_VALUE;
                }
                // for each edge
                for (Edge e : node.getEachEdge()) {
                    //System.out.printf("node %d neighbor %s via %s%n", i, e.getOpposite(node).getId(), e.getId() );
                    Node oppnode = e.getOpposite(node);

                    // calculate rho_i
                    int oppd = oppnode.getAttribute("d");
                    rho = Math.min(oppd + 1, rho);
                    if (oppd <= d) {
                        int opps = oppnode.getAttribute("s");
                        if (opps != 0) {
                            is_nomemb = false;
                        }
                    }

                    // calculate minM_i
                    int oppm = oppnode.getAttribute("m");
                    if ((oppd == d) && (oppm == 1)) {
                        j = Integer.parseInt((String) oppnode.getAttribute("id"));
                        minMj = Math.min(j, minMj);
                    }
                }
                // calculate enter_i
                int chk = 0;
                if ((s == 0) && (is_nomemb)) {
                    is_enter = true;
                    chk = 1;
                }
                // calculate leave_i
                if ((s == 1) && (!is_nomemb)) {
                    is_leave = true;
                    chk = 1;
                }
                if ((d != rho) || (m != chk)) {
                    // R1
                    d = rho;
                    m = chk;
                    node.setAttribute("d_next", d);
                    node.setAttribute("m_next", m);
                    System.out.println ("DEBUG: (" + debug_round + ") R1: [" + node.getId() + "] m=" + m + ",d=" + d);
                } else if ((d == rho) && (chk == 1) && (minMj == idx)) {
                    // R2
                    m = 0;
                    if (is_enter) {
                        s = 1;
                        //is_changed = true;
                    } else if (is_leave) {
                        s = 0;
                        //is_changed = true;
                    }
                    node.setAttribute("s_next", s);
                    node.setAttribute("m_next", m);
                    System.out.println ("DEBUG: (" + debug_round + ") R2: [" + node.getId() + "] m=" + m + ",s=" + s);
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
                m = node.getAttribute("m_next");
                if (m >= 0) {
                    is_changed = true; // DEBUG
                    node.setAttribute("m", m);
                } else {
                    m = node.getAttribute("m");
                }
                node.setAttribute("ui.label", "[" + node.getId() + "] m=" + m + ",d=" + d + ",s=" + s);
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
            //System.out.println ("DEBUG: # of s=" + count_s);
        }
        System.out.println ("DEBUG: END compute. # of (nodes,steps,|S|)=(" + theGraph.getNodeCount() + "," + (debug_round-1) + "," + count_s + ")" );
    }

    public void nodeAdded(String sourceId, long timeId, String nodeId) {
        Node node = theGraph.getNode(nodeId);
        System.out.println ("DEBUG: Add node: " + node.getId());

        node.addAttribute("ui.class", "member");
        int s;
        int d;
        int m;

        if( node.hasAttribute("s") ) {
            s = node.getAttribute("s");
        } else {
            s = 0;
            node.setAttribute("s", s);
        }
        if( node.hasAttribute("d") ) {
            d = node.getAttribute("d");
        } else {
            d = 0;
            node.setAttribute("d", d);
        }
        if( node.hasAttribute("m") ) {
            m = node.getAttribute("m");
        } else {
            m = 0;
            node.setAttribute("m", m);
        }
        node.setAttribute("ui.label", "[" + node.getId() + "] m=" + m + ",d=" + d + ",s=" + s);
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
        int s = 0;
        int d = 0;
        int m = 0;
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
                d = 0;
                node.setAttribute("d", d);
            }
            if( node.hasAttribute("m") ) {
                m = node.getAttribute("m");
            } else {
                m = 0;
                node.setAttribute("m", m);
            }

            // Only for debug:
            /*s = 0;
            node.setAttribute("s", s);
            d = 0;
            node.setAttribute("d", d);
            m = 0;
            node.setAttribute("m", m);*/

            node.setAttribute("ui.label", "[" + node.getId() + "] m=" + m + ",d=" + d + ",s=" + s);
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
