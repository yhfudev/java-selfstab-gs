package com.yhfudev;

import org.graphstream.algorithm.generator.*;
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
        int i;
    	int maxSteps = 3164; // 12,34,102,318,1002,3164,10002
        // create and display a graph
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        Graph graph = new SingleGraph("test");
        //graph.setNullAttributesAreErrors(true); // to throw an exception instead of returning null (in getAttribute()).
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", "url(data/selfstab-mwcds.css);");
        //graph.display();

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
        Generator generator = null;
        //generator = new ChvatalGenerator(); // fix size
        //generator = new FullGenerator(); // full connected, 2 steps,1 node in dominate set
        //generator = new GridGenerator(); // only one result
        //generator = new HypercubeGenerator(); // one result
        //generator = new IncompleteGridGenerator(); // error
        //generator = new PetersenGraphGenerator(); // fix size
        //generator = new PointsOfInterestGenerator(); // error
        //generator = new RandomEuclideanGenerator(); // linear algo endless loop
        //generator = new RandomFixedDegreeDynamicGraphGenerator(); //
        //generator = new RandomGenerator(); //
        //generator = new URLGenerator("http://www.cnbeta.com"); //
        //generator = new WikipediaGenerator("Antarctica"); // no end

        //generator = new DorogovtsevMendesGenerator(); // ok
        //generator = new FlowerSnarkGenerator(); // ok
        //generator = new WattsStrogatzGenerator(maxSteps, 30, 0.5); // small world, ok
        //generator = new LobsterGenerator(); // tree like, ok

        //generator = new FanGenerator();
        int listf5[][] = {
                {12, 5},
                {34, 5},
                {102, 5},
                {318, 5},
                {1002, 5},
                {3164, 5},
                {10002, 5},
        };
        int listp3[][] = {
                {12, 2},
                {34, 2},
                {102, 3},
                {318, 9},
                {1002, 30},
                {3164, 90},
                {10002, 300},
        };
        int listp10[][] = {
                {12, 2},
                {34, 3},
                {102, 10},
                {318, 32},
                {1002, 100},
                {3164, 316},
                {10002, 1000},
        };
        i = 6;
        maxSteps = listf5[i][0];
        int degree = listf5[i][1];
        generator = new ConnectionGenerator(graph, degree, false, true);

        generator.addSink(graph);
        generator.begin();
        for (i = 1; i < maxSteps; i++) {
            generator.nextEvents();
            //algorithm.compute();
        }
        generator.end();
        delay();
        algorithm.compute();

        /*
        pause(2000);

		//RandomWalk algorithm = new RandomWalk();
		//algorithm.init(graph);

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
    private static void delay() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }
}
