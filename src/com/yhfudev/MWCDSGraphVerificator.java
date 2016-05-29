package com.yhfudev;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

public class MWCDSGraphVerificator implements GraphVerificator
{

	@Override
	public boolean verify(Graph graph)
	{
		for(Node node: graph.getEachNode()) {
			int s = node.getAttribute("s");
			boolean has_adj_s = false;
			for(Edge e: node.getEachEdge()) {
				Node oppnode = e.getOpposite(node);
				//System.out.println ("DEBUG: MWCDSGraphVerificator get node " + node.getId() + ", opp node " + oppnode.getId());
				int opps = oppnode.getAttribute("s");
				if (opps != 0) {
					has_adj_s = true;
				}
				if ((s != 0) && (has_adj_s)) {
					// failed
					System.out.println ("DEBUG: MWCDSGraphVerificator failed at: node " + node.getId() + "(s=" + s + "), opp node " + oppnode.getId() + "(s=" + opps + ")");
					return false;
				}
			}
			if (s == 0) {
				if (! has_adj_s) {
					System.out.println ("DEBUG: MWCDSGraphVerificator failed at: node " + node.getId() + "(s=" + s + ") has no adjacent dominate node");
					return false;
				}
			}
		}
		return true;
	}
}
