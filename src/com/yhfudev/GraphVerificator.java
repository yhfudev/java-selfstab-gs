/**
 * Copyright 2016 Yunhui Fu <yhfudev@gmail.com>
 * License: GPL v3.0 or later
 */
package com.yhfudev;

import org.graphstream.graph.Graph;

public interface GraphVerificator
{
    /**
     * Verify the correctness of a graph generated
     */
    boolean verify (Graph g);
}