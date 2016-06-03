package com.yhfudev;

import org.graphstream.algorithm.DynamicAlgorithm;
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
     * if use heuristic strategic
     */
    protected boolean is_heuristic_on = false;
    public boolean isHeuristicOn () { return this.is_heuristic_on;}
    public void heuristicOn (boolean on) { this.is_heuristic_on = on;}
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
