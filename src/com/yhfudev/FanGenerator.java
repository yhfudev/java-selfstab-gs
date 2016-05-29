package com.yhfudev;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.stream.SourceBase;

/**
 * Created by yhfu on 5/27/16.
 */
public class FanGenerator extends SourceBase implements Generator
{
    int currentIndex = 0;
    int edgeId = 0;

    public void begin() {
        addNode();
    }

    public boolean nextEvents() {
        addNode();
        return true;
    }

    public void end() {
        // Nothing to do
    }

    protected void addNode() {
        sendNodeAdded(sourceId, Integer.toString(currentIndex));
        if (currentIndex == 0) {
            currentIndex++;
            return;
        }
        sendEdgeAdded(sourceId, Integer.toString(edgeId++), Integer.toString(currentIndex - 1), Integer.toString(currentIndex), false);
        if (currentIndex > 2) {
            sendEdgeAdded(sourceId, Integer.toString(edgeId++), Integer.toString(1), Integer.toString(currentIndex), false);
        }
        currentIndex++;
    }
}