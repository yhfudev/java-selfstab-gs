simss -- Self-Stabilizing Algorithm Simulator
=============================================

This simulator is implemented in java by using GraphStream library.

Features
--------

* Generates various graphs
* Supports Ding-Wang-Srimani's self-stabilizing algorithm for minimal weakly connected dominating sets
* Supports randomized self-stabilizing algorithm for minimal weakly connected dominating sets
* GUI interface to show the graph and interact with it

Usage
-----
Assume you create .jar file in sub-folder out.
For example, in Eclipse, you can export the project from the menu "File" -- "Export ..." -- "Runnalbe JAR file".

* Help

    java -jar out/simss.jar -h
    usage: simss <options>
    -a <arg>   the algorithm name, ding or rand
    -d <arg>   the node degree (max)
    -f         if the degree value is fix or not
    -g <arg>   the graph generator algorithm name: fan, rand, doro, flower,
                watt, lobster
    -h         print this message
    -i <arg>   the input file name
    -l <arg>   the trace log file name
    -n <arg>   the number of nodes
    -o <arg>   the attachable output cvs file name
    -s <arg>   show the input file with specified delay (ms)
    -u         if heuristic on

* Generate the graph and store it to a file

    java -jar simss.jar -g rand -n 10002 -d 5 -l selfstab-rand-m5-10002-raw.dgs -o results.csv
    # and strip the file
    cat selfstab-rand-m5-10002-raw.dgs | grep -v "cn " > selfstab-rand-m5-10002.dgs

It will call the class ConnectionGenerator to generate a connected graph of size 10002 nodes,
and the degree of each node is up to 5;
The graph is stored in file selfstab-rand-m5-10002-raw.dgs, which is striped and save to selfstab-rand-m5-10002.dgs;
A 
self-stabilizing algorithm for minimal weakly connected dominating sets
is run on the graph and the results is verified to make sure everything is correct.


* Run a predefined self-stabilizing algorithm on a graph input file

    java -jar simss.jar -a rand -i selfstab-rand-m5-10002.dgs -o rand-m5-rand.dat


* Run a predefined self-stabilizing algorithm on a graph input file and also show the GUI

    java -jar simss.jar -a rand -i selfstab-rand-m5-10002.dgs -o rand-m5-rand.dat -s 10



