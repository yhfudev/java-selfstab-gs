/**
 * Copyright 2016 Yunhui Fu <yhfudev@gmail.com>
 * License: GPL v3.0 or later
 */

package com.yhfudev;

import java.io.IOException;
import java.io.OutputStreamWriter;

class AlgorithmResult
{
    public int nodes = 0;
    public int steps = 0;
    public int dominating_set_size = 0;
    public int d_max = 0;
    public int node_degree_min = 0;
    public int node_degree_max = 0;
    public int node_degree_avg = 0;

    public static void writeHeader(OutputStreamWriter fwr, String comments)
    {
        try {
			fwr.append("#" + comments + "\n");
	        fwr.append("#steps, dominating set size, d_max, minimum node degree, maximum node degree, average node degree\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void SaveTo (OutputStreamWriter fwr)
    {
        try {
			fwr.append("" + nodes
			    + "\t" + steps
			    + "\t" + dominating_set_size
			    + "\t" + d_max
			    + "\t" + node_degree_min
			    + "\t" + node_degree_max
			    + "\t" + node_degree_avg
			    + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
