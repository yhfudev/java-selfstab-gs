package com.yhfudev;

import org.graphstream.graph.EdgeRejectedException;
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
        graph.addAttribute("ui.stylesheet", "url(data/selfstab-mwcds.css);");
        graph.display();

        // save the trace to file
        FileSinkDGS dgs = new FileSinkDGS();
        graph.addSink(dgs);
        try {
            dgs.begin("trace-selfstab-linear.dgs");
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
        int count_edge_error = 0;
        try {
            //source.begin("data/selfstab-mwcds.dgs"); // Ding's paper example
            //source.begin("data/selfstab-ds.dgs");    // DS example
            //source.begin("data/selfstab-doro-1002.dgs"); // DorogovtsevMendes
            //source.begin("data/selfstab-rand-p10-10002.dgs"); // random connected graph with degree = 10% nodes
            source.begin("data/selfstab-rand-f5-34.dgs"); // random connected graph with degree = 5
            while(true) {
                try {
                    if (false == source.nextEvents()) {
                        break;
                    }
                } catch (EdgeRejectedException e) {
                    // ignore
                    count_edge_error ++;
                    System.out.println("DEBUG: adding edge error: " + e.toString());
                }
                // Thread.sleep(50);
            }
            source.end();
        //} catch (InterruptedException e) {
        //    e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println ("DEBUG: END read from source. # of edges ignored=" + count_edge_error);

        // initialize the algorithm
        //DynamicOneToAllShortestPath algorithm = new DynamicOneToAllShortestPath(null);
        SelfStabilizingMWCDSLinear algorithm = new SelfStabilizingMWCDSLinear();
        //SelfStabilizingMWCDSRandom algorithm = new SelfStabilizingMWCDSRandom();
        //SelfStabilizingDSLinear algorithm = new SelfStabilizingDSLinear();
        algorithm.init(graph);
        algorithm.setSource("0");
        //algorithm.setAnimationDelay(4000);

        delay();
        algorithm.compute();

        GraphVerificator verificator = new MWCDSGraphVerificator();
        if (verificator.verify(graph)) {
        	System.out.println ("DEBUG: PASS MWCDSGraphVerificator verficiation.");
        } else {
        	System.out.println ("DEBUG: FAILED MWCDSGraphVerificator verficiation!");
        }

        algorithm.terminate();
        graph.removeSink(dgs);
        try {
            dgs.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }
}
