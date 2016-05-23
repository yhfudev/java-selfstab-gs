package com.yhfudev;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

import java.io.IOException;

/**
 * Created by yhfu on 5/22/16.
 */
public class DemoSelfStabilizingMWCDSLinear {

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        // create and display a graph
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        Graph graph = new SingleGraph("test");
        //graph.setNullAttributesAreErrors(true); // to throw an exception instead of returning null (in getAttribute()).
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url(data/seflstab-mwcds.css);");
        graph.display();

        // save the trace to file
        FileSinkDGS dgs = new FileSinkDGS();
        graph.addSink(dgs);
        try {
            dgs.begin("trace.dgs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Read from file
        static String my_graph =
                "DGS004\n" +
                "my 0 0\n" +
                "an A \n" +
                "an B \n";
        StringReader reader = new StringReader(my_graph);
 		FileSourceDGS source = new FileSourceDGS();
 		source.addSink(graph);
 		source.readAll(reader);
        */

        FileSource source = new FileSourceDGS();
        source.addSink( graph );
        try {
            source.begin("data/seflstab-mwcds.dgs");
            while(source.nextEvents());// Thread.sleep(50);
            source.end();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // initialize the algorithm
        //DynamicOneToAllShortestPath algorithm = new DynamicOneToAllShortestPath(null);
        SelfStabilizingMWCDSLinear algorithm = new SelfStabilizingMWCDSLinear();
        //SelfStabilizingMWCDSRandom algorithm = new SelfStabilizingMWCDSRandom();
        algorithm.init(graph);
        algorithm.setSource("0");
        algorithm.setAnimationDelay(2000);

        algorithm.compute();

        algorithm.terminate();
        graph.removeSink(dgs);
        try {
            dgs.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
