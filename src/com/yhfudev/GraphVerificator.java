package com.yhfudev;

import org.graphstream.graph.Graph;

public interface GraphVerificator
{
    /**
     * Verify the correctness of a graph generated
     */
    boolean verify (Graph g);
}