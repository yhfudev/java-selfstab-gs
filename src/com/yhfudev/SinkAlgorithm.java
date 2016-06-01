package com.yhfudev;

import org.graphstream.algorithm.DynamicAlgorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.stream.SinkAdapter;

/**
 * Created by yhfu on 5/22/16.
 */
public abstract class SinkAlgorithm extends SinkAdapter implements DynamicAlgorithm {

    protected AlgorithmResult result = new AlgorithmResult();
    public AlgorithmResult getResult() {
        return result;
    }

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
