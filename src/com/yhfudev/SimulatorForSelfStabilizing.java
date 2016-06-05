/**
 * Copyright 2016 Yunhui Fu <yhfudev@gmail.com>
 * License: GPL v3.0 or later
 */
package com.yhfudev;

import org.apache.commons.cli.*;
import org.graphstream.algorithm.generator.*;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkDGS;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Simulator For Self-Stabilizing
 */
public class SimulatorForSelfStabilizing {

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
    private static void showHelp (Options opt)
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "simss <options>", opt );
    }

    public static void main(String[] args)
    {
        // command line lib: apache CLI http://commons.apache.org/proper/commons-cli/
        // command line arguments:
        //  -- input file
        //  -- output line to a csv file
        //  -- algorithm: Ding's linear or randomized

        // single thread parsing ...
        Options options = new Options();
        options.addOption("h", false, "print this message");
        //heuristic
        options.addOption("u", false, "if heuristic on");
        options.addOption("s", true, "show the input file with specified delay (ms)");
        options.addOption("i", true, "the input file name");
        options.addOption("o", true, "the attachable output cvs file name");
        options.addOption("l", true, "the trace log file name");
        options.addOption("a", true, "the algorithm name, ding or rand");
        // options specified to generator
        options.addOption("g", true, "the graph generator algorithm name: fan1l, fan2l, rand, doro, flower, watt, lobster");
        options.addOption("n", true, "the number of nodes");
        options.addOption("d", true, "the node degree (max)");
        options.addOption("f", false, "if the degree value is fix or not");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        if(cmd.hasOption("h")) {
            showHelp ( options );
            return;
        }

        int delay_time = 0;
        if(cmd.hasOption("s")) {
            delay_time = Integer.parseInt(cmd.getOptionValue("s"));
        }

        String sFileName = null;
        sFileName = null;
        FileWriter writer = null;
        if(cmd.hasOption("o")) {
            sFileName = cmd.getOptionValue("o");
        }
        if ((null != sFileName) && (! "".equals(sFileName))) {
            try {
                writer = new FileWriter(sFileName, true); // true: append
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error: unable to open the output file " + sFileName);
                return;
            }
        }

        sFileName = null;
        if(cmd.hasOption("i")) {
            sFileName = cmd.getOptionValue("i");
        }
        String genname = null;
        if(cmd.hasOption("g")) {
            genname = cmd.getOptionValue("g");
        }
        if ((null == genname) && (null == sFileName)) {
            System.out.println ("Error: not specify the input file or graph generator");
            showHelp ( options );
            return;
        }
        if ((null != genname) && (null != sFileName)) {
            System.out.println ("Error: do not specify the input file and graph generator at the same time");
            showHelp ( options );
            return;
        }

        if (delay_time > 0) {
            // create and display a graph
            System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        }

        Graph graph = new SingleGraph("test");
        //graph.setNullAttributesAreErrors(true); // to throw an exception instead of returning null (in getAttribute()).
        if (delay_time > 0) {
            graph.addAttribute("ui.quality");
            graph.addAttribute("ui.antialias");
            graph.addAttribute("ui.stylesheet", "url(data/selfstab-mwcds.css);");
            graph.display();
        }

        // save the trace to file
        FileSinkDGS dgs = null;
        if(cmd.hasOption("l")) {
            dgs = new FileSinkDGS();
            graph.addSink(dgs);
            try {
                dgs.begin(cmd.getOptionValue("l"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Generator generator = null;
        if (null != sFileName) {
            System.out.println("DEBUG: the input file=" + sFileName);
            FileSource source = new FileSourceDGS();
            source.addSink(graph);
            int count_edge_error = 0;
            try {
                //source.begin("data/selfstab-mwcds.dgs"); // Ding's paper example
                //source.begin("data/selfstab-ds.dgs");    // DS example
                //source.begin("data/selfstab-doro-1002.dgs"); // DorogovtsevMendes
                //source.begin("data/selfstab-rand-p10-10002.dgs"); // random connected graph with degree = 10% nodes
                //source.begin("data/selfstab-rand-f5-34.dgs"); // random connected graph with degree = 5
                source.begin(sFileName);
                while (true) {
                    try {
                        if (false == source.nextEvents()) {
                            break;
                        }
                    } catch (EdgeRejectedException e) {
                        // ignore
                        count_edge_error++;
                        System.out.println("DEBUG: adding edge error: " + e.toString());
                    }
                    if (delay_time > 0) {
                        delay(delay_time);
                    }
                }
                source.end();
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("DEBUG: END read from source. # of edges ignored=" + count_edge_error);
        } else {
            // assert (genname != null);

            // graph generator
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

            int i;
            int n=12; // the number of nodes
            if(cmd.hasOption("n")) {
                n = Integer.parseInt(cmd.getOptionValue("n"));
            }
            int d=3; // the degree of nodes
            if(cmd.hasOption("d")) {
                d = Integer.parseInt(cmd.getOptionValue("d"));
            }
            boolean isFix = false;
            if(cmd.hasOption("f")) {
                isFix = true;
            }
            if ("".equals(genname)) {
                System.out.println ("Error: not set generator name");
                return;
            } else if ("fan1l".equals(genname)) {
                generator = new FanGenerator();
            } else if ("fan2l".equals(genname)) {
                generator = new Fan2lGenerator (graph, d);
            } else if ("doro".equals(genname)) {
                generator = new DorogovtsevMendesGenerator();
            } else if ("flower".equals(genname)) {
                generator = new FlowerSnarkGenerator();
            } else if ("lobster".equals(genname)) {
                generator = new LobsterGenerator();
            } else if ("rand".equals(genname)) {
                generator = new ConnectionGenerator(graph, d, false, isFix);
            } else if ("watt".equals(genname)) {
                // WattsStrogatzGenerator(n,k,beta)
                // a ring of n nodes
                // each node is connected to its k nearest neighbours, k must be even
                // n >> k >> log(n) >> 1
                // beta being a probability it must be between 0 and 1.
                int k;
                double beta = 0.5;
                k = (n / 20) * 2;
                if (k < 2) {
                    k = 2;
                }
                if (n < 2 * 6) {
                    n = 2 * 6;
                }
                generator = new WattsStrogatzGenerator(n, k, beta);
            }
        /*int listf5[][] = {
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
        */
            generator.addSink(graph);
            generator.begin();
            for (i = 1; i < n; i++) {
                generator.nextEvents();
            }
            generator.end();
            delay(500);
        }

        if(cmd.hasOption("a")) {
            SinkAlgorithm algorithm = null;
            String algo = "rand";
            algo = cmd.getOptionValue("a");
            if ("ding".equals(algo)) {
                algorithm = new SelfStabilizingMWCDSLinear();
            } else if ("ds".equals(algo)) {
                algorithm = new SelfStabilizingDSLinear();
            } else {
                algorithm = new SelfStabilizingMWCDSRandom();
            }
            algorithm.init(graph);
            algorithm.setSource("0");
            if (delay_time > 0) {
                algorithm.setAnimationDelay(delay_time);
            }
            if (cmd.hasOption("u")) {
                algorithm.heuristicOn(true);
            } else {
                algorithm.heuristicOn(false);
            }
            algorithm.compute();

            GraphVerificator verificator = new MWCDSGraphVerificator();
            if (verificator.verify(graph)) {
                System.out.println("DEBUG: PASS MWCDSGraphVerificator verficiation.");
            } else {
                System.out.println("DEBUG: FAILED MWCDSGraphVerificator verficiation!");
            }

            if (null != writer) {
                AlgorithmResult result = algorithm.getResult();
                result.SaveTo(writer);
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            algorithm.terminate();
        }

        if (null != generator) {
            generator.removeSink(graph);
        }
        if (dgs != null) {
            graph.removeSink(dgs);
            try {
                dgs.end();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void delay(int val) {
        try {
            Thread.sleep(val);
        } catch (InterruptedException e) {
        }
    }
}
