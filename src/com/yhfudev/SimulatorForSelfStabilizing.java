package com.yhfudev;

import org.apache.commons.cli.*;
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
        options.addOption("s", true, "show the input file with specified delay (ms)");
        options.addOption("i", true, "the input file name");
        options.addOption("o", true, "the attachable output cvs file name");
        options.addOption("l", true, "the trace log file name");
        options.addOption("a", true, "the algorithm name, ding or rand");
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

        sFileName = null;
        if(cmd.hasOption("i")) {
            sFileName = cmd.getOptionValue("i");
        }
        if (null == sFileName) {
            System.out.println ("Error: not specify the input file");
            showHelp ( options );
            return;
        }
        System.out.println ("DEBUG: the input file=" + sFileName);
        FileSource source = new FileSourceDGS();
        source.addSink( graph );
        int count_edge_error = 0;
        try {
            //source.begin("data/selfstab-mwcds.dgs"); // Ding's paper example
            //source.begin("data/selfstab-ds.dgs");    // DS example
            //source.begin("data/selfstab-doro-1002.dgs"); // DorogovtsevMendes
            //source.begin("data/selfstab-rand-p10-10002.dgs"); // random connected graph with degree = 10% nodes
            //source.begin("data/selfstab-rand-f5-34.dgs"); // random connected graph with degree = 5
            source.begin(sFileName);
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
                if (delay_time > 0) { delay(delay_time); }
            }
            source.end();
            //} catch (InterruptedException e) {
            //    e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println ("DEBUG: END read from source. # of edges ignored=" + count_edge_error);

        String algo = "rand";
        if(cmd.hasOption("a")) {
            algo = cmd.getOptionValue("a");
        }
        SinkAlgorithm algorithm = null;
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
        algorithm.compute();

        GraphVerificator verificator = new MWCDSGraphVerificator();
        if (verificator.verify(graph)) {
            System.out.println ("DEBUG: PASS MWCDSGraphVerificator verficiation.");
        } else {
            System.out.println ("DEBUG: FAILED MWCDSGraphVerificator verficiation!");
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
