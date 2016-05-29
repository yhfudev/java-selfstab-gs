package com.yhfudev;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.networksimplex.DynamicOneToAllShortestPath;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;

import java.io.IOException;

/**
 * Created by yhfu on 5/22/16.
 */
public class DemoSelfStabilizingMWCDSRandom {

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    public static void main(String[] args) {
    	int maxSteps = 12;
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
            dgs.begin("trace-selfstab-random.dgs");
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

        // initialize the algorithm
        //DynamicOneToAllShortestPath algorithm = new DynamicOneToAllShortestPath(null);
        //SelfStabilizingMWCDSLinear algorithm = new SelfStabilizingMWCDSLinear();
        SelfStabilizingMWCDSRandom algorithm = new SelfStabilizingMWCDSRandom();
        //SelfStabilizingDSLinear algorithm = new SelfStabilizingDSLinear();
        algorithm.init(graph);
        algorithm.setSource("0");
        //algorithm.setAnimationDelay(200);

        // add some nodes and edges
        //Generator generator = new DorogovtsevMendesGenerator();
        Generator generator = new FanGenerator();
        generator.addSink(graph);
        generator.begin();
        for (int i = 0; i < maxSteps; i++) {
            generator.nextEvents();
            //algorithm.compute();
        }
        algorithm.compute();

        pause(2000);

		//RandomWalk algorithm = new RandomWalk();
		//algorithm.init(graph);

        /*
        // now remove some nodes and edges
        for (int i = maxSteps-1; i > 100; i--) {
            graph.removeNode(i);
            algorithm.compute();
        }

        pause(2000);

        // now change the source
        for (int i = 1; i <= 10; i++) {
            algorithm.setSource(i + "");
            algorithm.compute();
        }*/

        GraphVerificator verificator = new MWCDSGraphVerificator();
        if (verificator.verify(graph)) {
        	System.out.println ("DEBUG: PASS MWCDSGraphVerificator verficiation.");
        } else {
        	System.out.println ("DEBUG: FAILED MWCDSGraphVerificator verficiation!");
        }

        algorithm.terminate();
        graph.removeSink(dgs);
        generator.removeSink(graph);
        try {
            dgs.end();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
