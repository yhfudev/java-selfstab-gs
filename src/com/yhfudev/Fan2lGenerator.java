/**
 * Copyright 2016 Yunhui Fu <yhfudev@gmail.com>
 * License: GPL v3.0 or later
 */
package com.yhfudev;

import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.stream.SourceBase;

/**
 * 2 layers fan graph
 */
public class Fan2lGenerator extends BaseGenerator
{
    int currentIndex = 0;
    Graph theGraph;
    int degree = 3; // the max degree between layer 1 and 2

    public Fan2lGenerator (Graph g, int degree_int) {
        theGraph = g;
        degree = degree_int;
    }

    public void begin() {
        addNode();
    }

    public boolean nextEvents() {
        addNode();
        return true;
    }

    public void end() {
        String edgeId;
        int s1 = (currentIndex - 2 + 1) / 2;
        int s2 = (currentIndex - 2 - s1);
        int i;
        int j;
        int src;
        int dest;
        // randomly connect the nodes between layer 1 and 2
        for (i = 0; i < s2; i ++) {
            dest = 3 + i * 2;
            for (j = 0; j < degree; j ++) {
            	src = 2 + this.random.nextInt(s1) * 2;
                //System.out.println("DEBUG: connect node " + src + " -- " + dest);
                edgeId = "" + Integer.toString(src) + "-" +  Integer.toString(dest);
                Edge edge = theGraph.getEdge(edgeId);
                if (null == edge) {
                    try {
                        sendEdgeAdded(sourceId, edgeId
                                , Integer.toString(src)
                                , Integer.toString(dest)
                                , false);
                    } catch (EdgeRejectedException e) {
                        // ignore
                        System.out.println("ERROR: connect node " + src + " -- " + dest);
                    }
                }
            }
        }
    }

    protected void addNode() {
        int thisIndex = currentIndex;
        currentIndex ++;
        sendNodeAdded(sourceId, Integer.toString(thisIndex));
        if (thisIndex == 0) {
            return;
        }
        String edgeId;
        if (thisIndex < 4) {
            edgeId = "" + Integer.toString(thisIndex - 1) + "-" + Integer.toString(thisIndex);
            sendEdgeAdded(sourceId, edgeId
                    , Integer.toString(thisIndex - 1)
                    , Integer.toString(thisIndex)
                    , false);
        } else {
            edgeId = "" + Integer.toString(thisIndex - 2) + "-" + Integer.toString(thisIndex);
            sendEdgeAdded(sourceId, edgeId
                    , Integer.toString(thisIndex - 2)
                    , Integer.toString(thisIndex)
                    , false);
            if (0 == (thisIndex % 2)) {
                edgeId = "" + Integer.toString(1) + "-" + Integer.toString(thisIndex);
                sendEdgeAdded(sourceId, edgeId
                        , Integer.toString(1)
                        , Integer.toString(thisIndex)
                        , false);
            }
        }
    }
}