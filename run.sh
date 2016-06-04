#!/bin/sh

DN_DATA=data
DN_EXEC=out
EXEC_SIMSS=${DN_EXEC}/simss.jar

################################################################################
# generate gnuplot file for Ding's algorithm (since it is a determinant algorithm, there's only one solution from that algorithm)
function generate_gnuplot_ding ()
{
    PARAM_PREFIX=$1
    shift
    PARAM_ALGORITHM=$1
    shift

    cat << EOF > ${PARAM_PREFIX}.gp
# The default size for postscript output is 10 inches x 7 inches.
# The default for eps output is 5 x 3.5 inches
#set terminal pdf color solid lw 1 size 5.83,4.13 font "cmr12" enh
#set pointsize 1
#set output "${PARAM_PREFIX}-steps.pdf"
set terminal postscript eps color enhanced size 2.57,1.8
set output "${PARAM_PREFIX}-steps.eps"


set title "Ding's algorithm steps, grouped by # of nodes\n"
set ylabel "steps"
set xlabel "nodes"

set logscale xy

plot '${PARAM_PREFIX}.dat' using 1:2 with lp t 'steps'


# set terminal png transparent nocrop enhanced size 450,320 font "arial,8" 
set terminal png size 800,600
set output "${PARAM_PREFIX}-steps.png"
replot



set terminal postscript eps color enhanced size 2.57,1.8
set output "${PARAM_PREFIX}-dset.eps"


set title "Dominate sets size, grouped by # of nodes\n"
set ylabel "size"
set xlabel "nodes"

set logscale xy

plot '${PARAM_PREFIX}.dat' using 1:3 with lp t 'size'


# set terminal png transparent nocrop enhanced size 450,320 font "arial,8" 
set terminal png size 800,600
set output "${PARAM_PREFIX}-dset.png"
replot
EOF
}


# generate gnuplot file for randomized algorithm (there's various solutions from that algorithm)
function generate_gnuplot_rand ()
{
    PARAM_PREFIX=$1
    shift
    PARAM_ALGORITHM=$1
    shift

    cat << EOF > ${PARAM_PREFIX}.gp
#set terminal pdf color solid lw 1 size 5.83,4.13 font "cmr12" enh
#set pointsize 1
#set output "${PARAM_PREFIX}-steps.pdf"
set terminal postscript eps color enhanced size 2.57,1.8
set output "${PARAM_PREFIX}-steps.eps"


set title "Distribution of random algorithm steps, grouped by # of nodes\n"
set ylabel "steps"
set xlabel "nodes"


set border 2 front lt black linewidth 1.000 dashtype solid
set boxwidth 0.5 absolute
set style fill   solid 0.25 border lt -1
unset key
set pointsize 0.5
set style data boxplot
set xtics border in scale 0,0 nomirror norotate  autojustify
set xtics  norangelimit
set xtics   ()
set ytics border in scale 1,0.5 nomirror norotate  autojustify
x = 0.0

plot '${PARAM_PREFIX}.dat' using (1):2:(0):1


# set terminal png transparent nocrop enhanced size 450,320 font "arial,8" 
set terminal png size 800,600
set output "${PARAM_PREFIX}-steps.png"
replot



set terminal postscript eps color enhanced size 2.57,1.8
set output "${PARAM_PREFIX}-dset.eps"


set title "Distribution of dominate sets size, grouped by # of nodes\n"
set ylabel "size"
set xlabel "nodes"


set border 2 front lt black linewidth 1.000 dashtype solid
set boxwidth 0.5 absolute
set style fill   solid 0.25 border lt -1
unset key
set pointsize 0.5
set style data boxplot
set xtics border in scale 0,0 nomirror norotate  autojustify
set xtics  norangelimit
set xtics   ()
set ytics border in scale 1,0.5 nomirror norotate  autojustify
x = 0.0

set logscale y

plot '${PARAM_PREFIX}.dat' using (1):3:(0):1


# set terminal png transparent nocrop enhanced size 450,320 font "arial,8" 
set terminal png size 800,600
set output "${PARAM_PREFIX}-dset.png"
replot

EOF
}


################################################################################
if [ 0 = 1 ]; then
# generate data


# Dorogovtsev-Mendes
java -jar ${EXEC_SIMSS} -g doro -n    12 -l ${DN_DATA}/selfstab-doro-12-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g doro -n    34 -l ${DN_DATA}/selfstab-doro-34-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g doro -n   102 -l ${DN_DATA}/selfstab-doro-102-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g doro -n   318 -l ${DN_DATA}/selfstab-doro-318-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g doro -n  1002 -l ${DN_DATA}/selfstab-doro-1002-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g doro -n  3164 -l ${DN_DATA}/selfstab-doro-3164-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g doro -n 10002 -l ${DN_DATA}/selfstab-doro-10002-raw.dgs -o results.csv
cat ${DN_DATA}/selfstab-doro-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-doro-12.dgs
cat ${DN_DATA}/selfstab-doro-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-doro-34.dgs
cat ${DN_DATA}/selfstab-doro-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-doro-102.dgs
cat ${DN_DATA}/selfstab-doro-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-doro-318.dgs
cat ${DN_DATA}/selfstab-doro-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-doro-1002.dgs
cat ${DN_DATA}/selfstab-doro-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-doro-3164.dgs
cat ${DN_DATA}/selfstab-doro-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-doro-10002.dgs

# fan 1 layer
java -jar ${EXEC_SIMSS} -g fan -n    12 -l ${DN_DATA}/selfstab-fan1l-12-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g fan -n    34 -l ${DN_DATA}/selfstab-fan1l-34-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g fan -n   102 -l ${DN_DATA}/selfstab-fan1l-102-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g fan -n   318 -l ${DN_DATA}/selfstab-fan1l-318-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g fan -n  1002 -l ${DN_DATA}/selfstab-fan1l-1002-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g fan -n  3164 -l ${DN_DATA}/selfstab-fan1l-3164-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g fan -n 10002 -l ${DN_DATA}/selfstab-fan1l-10002-raw.dgs -o results.csv
cat ${DN_DATA}/selfstab-fan1l-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-12.dgs
cat ${DN_DATA}/selfstab-fan1l-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-34.dgs
cat ${DN_DATA}/selfstab-fan1l-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-102.dgs
cat ${DN_DATA}/selfstab-fan1l-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-318.dgs
cat ${DN_DATA}/selfstab-fan1l-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-1002.dgs
cat ${DN_DATA}/selfstab-fan1l-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-3164.dgs
cat ${DN_DATA}/selfstab-fan1l-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-fan1l-10002.dgs

# We have to run the generator for rand-f5, rand-fp3, rand-fp10 multiple time,
# since these types of graphs are hard to get for all of nodes get a fix degree.
# And we also need to verify all of the generated .dgs files to make sure all of nodes have the same degree.
if [ 0 = 1 ]; then
    java -jar ${EXEC_SIMSS} -g rand -f -n    12 -d 5 -l ${DN_DATA}/selfstab-rand-f5-12-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n    34 -d 5 -l ${DN_DATA}/selfstab-rand-f5-34-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n   102 -d 5 -l ${DN_DATA}/selfstab-rand-f5-102-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n   318 -d 5 -l ${DN_DATA}/selfstab-rand-f5-318-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n  1002 -d 5 -l ${DN_DATA}/selfstab-rand-f5-1002-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n  3164 -d 5 -l ${DN_DATA}/selfstab-rand-f5-3164-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n 10002 -d 5 -l ${DN_DATA}/selfstab-rand-f5-10002-raw.dgs -o results.csv
    cat ${DN_DATA}/selfstab-rand-f5-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-12.dgs
    cat ${DN_DATA}/selfstab-rand-f5-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-34.dgs
    cat ${DN_DATA}/selfstab-rand-f5-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-102.dgs
    cat ${DN_DATA}/selfstab-rand-f5-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-318.dgs
    cat ${DN_DATA}/selfstab-rand-f5-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-1002.dgs
    cat ${DN_DATA}/selfstab-rand-f5-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-3164.dgs
    cat ${DN_DATA}/selfstab-rand-f5-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-rand-f5-10002.dgs
    java -jar ${EXEC_SIMSS} -g rand -f -n    12 -d   2 -l ${DN_DATA}/selfstab-rand-fp3-12-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n    34 -d   2 -l ${DN_DATA}/selfstab-rand-fp3-34-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n   102 -d   3 -l ${DN_DATA}/selfstab-rand-fp3-102-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n   318 -d   9 -l ${DN_DATA}/selfstab-rand-fp3-318-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n  1002 -d  30 -l ${DN_DATA}/selfstab-rand-fp3-1002-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n  3164 -d  90 -l ${DN_DATA}/selfstab-rand-fp3-3164-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n 10002 -d 300 -l ${DN_DATA}/selfstab-rand-fp3-10002-raw.dgs -o results.csv
    cat ${DN_DATA}/selfstab-rand-fp3-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-12.dgs
    cat ${DN_DATA}/selfstab-rand-fp3-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-34.dgs
    cat ${DN_DATA}/selfstab-rand-fp3-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-102.dgs
    cat ${DN_DATA}/selfstab-rand-fp3-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-318.dgs
    cat ${DN_DATA}/selfstab-rand-fp3-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-1002.dgs
    cat ${DN_DATA}/selfstab-rand-fp3-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-3164.dgs
    cat ${DN_DATA}/selfstab-rand-fp3-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp3-10002.dgs
    java -jar ${EXEC_SIMSS} -g rand -f -n    12 -d    2 -l ${DN_DATA}/selfstab-rand-fp10-12-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n    34 -d    3 -l ${DN_DATA}/selfstab-rand-fp10-34-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n   102 -d   10 -l ${DN_DATA}/selfstab-rand-fp10-102-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n   318 -d   32 -l ${DN_DATA}/selfstab-rand-fp10-318-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n  1002 -d  100 -l ${DN_DATA}/selfstab-rand-fp10-1002-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n  3164 -d  316 -l ${DN_DATA}/selfstab-rand-fp10-3164-raw.dgs -o results.csv
    java -jar ${EXEC_SIMSS} -g rand -f -n 10002 -d 1000 -l ${DN_DATA}/selfstab-rand-fp10-10002-raw.dgs -o results.csv
    cat ${DN_DATA}/selfstab-rand-fp10-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-12.dgs
    cat ${DN_DATA}/selfstab-rand-fp10-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-34.dgs
    cat ${DN_DATA}/selfstab-rand-fp10-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-102.dgs
    cat ${DN_DATA}/selfstab-rand-fp10-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-318.dgs
    cat ${DN_DATA}/selfstab-rand-fp10-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-1002.dgs
    cat ${DN_DATA}/selfstab-rand-fp10-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-3164.dgs
    cat ${DN_DATA}/selfstab-rand-fp10-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-rand-fp10-10002.dgs
fi

# rand-m5
java -jar ${EXEC_SIMSS} -g rand -n    12 -d 5 -l ${DN_DATA}/selfstab-rand-m5-12-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n    34 -d 5 -l ${DN_DATA}/selfstab-rand-m5-34-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n   102 -d 5 -l ${DN_DATA}/selfstab-rand-m5-102-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n   318 -d 5 -l ${DN_DATA}/selfstab-rand-m5-318-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n  1002 -d 5 -l ${DN_DATA}/selfstab-rand-m5-1002-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n  3164 -d 5 -l ${DN_DATA}/selfstab-rand-m5-3164-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n 10002 -d 5 -l ${DN_DATA}/selfstab-rand-m5-10002-raw.dgs -o results.csv
cat ${DN_DATA}/selfstab-rand-m5-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-12.dgs
cat ${DN_DATA}/selfstab-rand-m5-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-34.dgs
cat ${DN_DATA}/selfstab-rand-m5-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-102.dgs
cat ${DN_DATA}/selfstab-rand-m5-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-318.dgs
cat ${DN_DATA}/selfstab-rand-m5-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-1002.dgs
cat ${DN_DATA}/selfstab-rand-m5-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-3164.dgs
cat ${DN_DATA}/selfstab-rand-m5-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-rand-m5-10002.dgs

# rand-mp3
java -jar ${EXEC_SIMSS} -g rand -n    12 -d   2 -l ${DN_DATA}/selfstab-rand-mp3-12-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n    34 -d   2 -l ${DN_DATA}/selfstab-rand-mp3-34-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n   102 -d   3 -l ${DN_DATA}/selfstab-rand-mp3-102-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n   318 -d   9 -l ${DN_DATA}/selfstab-rand-mp3-318-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n  1002 -d  30 -l ${DN_DATA}/selfstab-rand-mp3-1002-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n  3164 -d  90 -l ${DN_DATA}/selfstab-rand-mp3-3164-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n 10002 -d 300 -l ${DN_DATA}/selfstab-rand-mp3-10002-raw.dgs -o results.csv
cat ${DN_DATA}/selfstab-rand-mp3-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-12.dgs
cat ${DN_DATA}/selfstab-rand-mp3-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-34.dgs
cat ${DN_DATA}/selfstab-rand-mp3-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-102.dgs
cat ${DN_DATA}/selfstab-rand-mp3-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-318.dgs
cat ${DN_DATA}/selfstab-rand-mp3-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-1002.dgs
cat ${DN_DATA}/selfstab-rand-mp3-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-3164.dgs
cat ${DN_DATA}/selfstab-rand-mp3-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp3-10002.dgs

# rand-mp10
java -jar ${EXEC_SIMSS} -g rand -n    12 -d    2 -l ${DN_DATA}/selfstab-rand-mp10-12-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n    34 -d    3 -l ${DN_DATA}/selfstab-rand-mp10-34-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n   102 -d   10 -l ${DN_DATA}/selfstab-rand-mp10-102-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n   318 -d   32 -l ${DN_DATA}/selfstab-rand-mp10-318-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n  1002 -d  100 -l ${DN_DATA}/selfstab-rand-mp10-1002-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n  3164 -d  316 -l ${DN_DATA}/selfstab-rand-mp10-3164-raw.dgs -o results.csv
java -jar ${EXEC_SIMSS} -g rand -n 10002 -d 1000 -l ${DN_DATA}/selfstab-rand-mp10-10002-raw.dgs -o results.csv
cat ${DN_DATA}/selfstab-rand-mp10-12-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-12.dgs
cat ${DN_DATA}/selfstab-rand-mp10-34-raw.dgs    | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-34.dgs
cat ${DN_DATA}/selfstab-rand-mp10-102-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-102.dgs
cat ${DN_DATA}/selfstab-rand-mp10-318-raw.dgs   | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-318.dgs
cat ${DN_DATA}/selfstab-rand-mp10-1002-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-1002.dgs
cat ${DN_DATA}/selfstab-rand-mp10-3164-raw.dgs  | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-3164.dgs
cat ${DN_DATA}/selfstab-rand-mp10-10002-raw.dgs | grep -v "cn " > ${DN_DATA}/selfstab-rand-mp10-10002.dgs

rm ${DN_DATA}/*-raw.dgs
fi

################################################################################
# config arrays for simulation and data processing

#LIST_NODES=( 12 34 102 318 1002 3164 )
LIST_NODES=( 12 34 102 318 1002 3164 10002 )

#LIST_GRAPH=( rand-f5 rand-fp3 rand-fp10 rand-m5 rand-mp3 rand-mp10 doro fan1l fan2l )
LIST_GRAPH=( rand-f5 rand-fp3 rand-fp10 rand-m5 rand-mp3 rand-mp10 doro fan1l )

LIST_GRAPH_DESC=(
    "rand-f5   -- Randomized graph with fixed degree 5"
    "rand-fp3  -- Randomized graph with degree value of 3\% nodes"
    "rand-fp10 -- Randomized graph with degree value of 10\% nodes"
    "rand-m5   -- Randomized graph with max degree 5"
    "rand-mp3  -- Randomized graph with max degree value of 3\% nodes"
    "rand-mp10 -- Randomized graph with max degree value of 10\% nodes"
    "doro      -- Dorogovtsev Mendes graph"
    "fan1l     -- 1 layer fan grpah"
    "fan2l     -- 2 layers fan grpah"
    )

################################################################################
# simulations

if [ 1 = 1 ]; then

i=0
while (( $i < ${#LIST_GRAPH[*]} )) ; do
    rm -f ${LIST_GRAPH[$i]}-ding.dat ${LIST_GRAPH[$i]}-rand.dat ${LIST_GRAPH[$i]}-rand-heuristic.dat
    j=0
    while (( $j < ${#LIST_NODES[*]} )) ; do
        FN_DATA="selfstab-${LIST_GRAPH[$i]}-${LIST_NODES[$j]}.dgs"
        echo "process ${LIST_GRAPH_DESC[$i]} with nodes ${LIST_NODES[$j]}"

if [ 1 = 1 ]; then
        # generate data
        #java -jar ${EXEC_SIMSS} -g rand -n 1002 -d 10 -l test.dgs -a rand -u
        # cat test.dgs | grep -v "cn " > ${FN_DATA}

        # Ding's linear algorithm
        java -jar ${EXEC_SIMSS} -a ding -i ${DN_DATA}/${FN_DATA} -o ${LIST_GRAPH[$i]}-ding.dat

        # randomized algorithm
        C=0
        while (( $C < 12 )); do
            java -jar ${EXEC_SIMSS} -a rand -i ${DN_DATA}/${FN_DATA} -o ${LIST_GRAPH[$i]}-rand.dat
            C=$(($C + 1))
        done
fi

if [ 1 = 1 ]; then
        # randomized algorithm with heuristic mode on
        C=0
        while (( $C < 12 )); do
            java -jar ${EXEC_SIMSS} -a rand -i ${DN_DATA}/${FN_DATA} -o ${LIST_GRAPH[$i]}-rand-heuristic.dat -u
            C=$(($C + 1))
        done
fi

        j=$((j+1))
    done

    i=$((i+1))
done

fi


################################################################################
# process data
function randsummary ()
{
    PARAM_INPUT=$1
    shift

    PRE=
    while read a b c d e f g h; do \
        if [ ! "$PRE" = "$a" ]; then \
            if [ ! "$PRE" = "" ]; then
                echo -e "$PRE $SMIN-$SMAX $DMIN-$DMAX"; \
            fi; \
            SMIN=$b; \
            SMAX=$b; \
            DMIN=$c; \
            DMAX=$c; \
            PRE=$a; \
        fi; \
        if (($SMIN > $b)) ; then \
            SMIN=$b; \
        fi; \
        if (($SMAX < $b)) ; then \
            SMAX=$b; \
        fi; \
        if (($DMIN > $c)) ; then \
            DMIN=$c; \
        fi; \
        if (($DMAX < $c)) ; then \
            DMAX=$c; \
        fi; \
    done <"${PARAM_INPUT}" ; \
    if [ ! "$PRE" = "" ]; then \
        echo -e "$PRE\t$SMIN-$SMAX\t$DMIN-$DMAX"; \
    fi; \
}


#rm -f *.png *.eps *.gp

FN_TEX=all.tex
cat << EOF > "${FN_TEX}"
\documentclass[letter,12pt,onecolumn]{article}

\newcommand{\doctitle}{Self-Stabilizing Graph Data Figures and Tables}
\newcommand{\docauthor}{Yunhui Fu}
\newcommand{\dockeywords}{}
\newcommand{\docsubject}{}

\usepackage{ifthen}
\usepackage{ifpdf}
\usepackage{ifxetex}
\usepackage{ifluatex}

\usepackage{color}
\usepackage[rgb,x11names]{xcolor} %must before tikz, x11names defines RoyalBlue3

\usepackage{booktabs,longtable} % table in seperate pages.
\usepackage{array}
% table's multirow and multicolumn
\usepackage{multirow}

% ============================================
% Check for PDFLaTeX/LaTeX
% ============================================
\newcommand{\outengine}{xetex}
\newif\ifpdf
\ifx\pdfoutput\undefined
  \pdffalse % we are not running PDFLaTeX
  \ifxetex
    \renewcommand{\outengine}{xetex}
  \else
    \renewcommand{\outengine}{dvipdfmx}
  \fi
\else
  \pdfoutput=1 % we are running PDFLaTeX
  \pdftrue
  \usepackage{thumbpdf}
  \renewcommand{\outengine}{pdftex}
\fi
\usepackage[\outengine,
    bookmarksnumbered, %dvipdfmx
    %% unicode, %% 不能有unicode选项，否则bookmark会是乱码
    colorlinks=true,
    citecolor=red,
    urlcolor=blue,        % \href{...}{...} external (URL)
    filecolor=red,      % \href{...} local file
    linkcolor=black, % \ref{...} and \pageref{...}
    breaklinks,
    pdftitle={\doctitle},
    pdfauthor={\docauthor},
    pdfsubject={\docsubject},
    pdfkeywords={\dockeywords},
    pdfproducer={Latex with hyperref},
    pdfcreator={pdflatex},
    %%pdfadjustspacing=1,
    pdfborder=1,
    pdfpagemode=UseNone,
    pagebackref,
    bookmarksopen=true]{hyperref}

% --------------------------------------------
% Load graphicx package with pdf if needed 
% --------------------------------------------
\ifxetex    % xelatex
    \usepackage{graphicx}
\else
    \ifpdf
        \usepackage[pdftex]{graphicx}
        \pdfcompresslevel=9
    \else
        \usepackage{graphicx} % \usepackage[dvipdfm]{graphicx}
    \fi
\fi
%% \DeclareGraphicsRule{.jpg}{eps}{.bb}{}
%\DeclareGraphicsRule{.png}{eps}{.bb}{}
\graphicspath{{./} {figures/}}
\usepackage{flafter} % 防止图形在文字前

\usepackage[caption=false,font=footnotesize]{subfig}

\newcommand{\algoding}{\texttt{DWS}}
\newcommand{\algorand}{\texttt{Rand}}

\title{\doctitle}
\author{\docauthor}
\date{}
\begin{document}

\maketitle


\section{Introduction}
There are ${#LIST_GRAPH[*]} types of graph to be tested in this section.
\begin{enumerate}
EOF

i=0
while (( $i < ${#LIST_GRAPH[*]} )) ; do
    cat << EOF >> "${FN_TEX}"
  \item ${LIST_GRAPH_DESC[$i]};
EOF
    i=$((i+1))
done

cat << EOF >> "${FN_TEX}"
\end{enumerate}

EOF

i=0
while (( $i < ${#LIST_GRAPH[*]} )) ; do
    #FN_DATA="selfstab-${LIST_GRAPH[$i]}-${LIST_NODES[$j]}.dgs"

    FN_TABLE="table-rand-f5.tex"
    FN_RAND="rand-f5-rand.dat"
    FN_RAND_HEU="rand-f5-rand-heuristic.dat"
    FN_SUM_DING="rand-f5-ding.dat"
    FN_SUM_RAND="tmp-rand-f5-rand-sum.dat"
    FN_SUM_RAND_HEU="tmp-rand-f5-rand-heusum.dat"

    FN_TABLE="table-${LIST_GRAPH[$i]}.tex"
    FN_RAND="${LIST_GRAPH[$i]}-rand.dat"
    FN_RAND_HEU="${LIST_GRAPH[$i]}-rand-heuristic.dat"
    FN_SUM_DING="${LIST_GRAPH[$i]}-ding.dat"
    FN_SUM_RAND="tmp-${LIST_GRAPH[$i]}-rand-sum.dat"
    FN_SUM_RAND_HEU="tmp-${LIST_GRAPH[$i]}-rand-heusum.dat"

    # generate result summary table
    randsummary "${FN_RAND}"     > "${FN_SUM_RAND}"
    randsummary "${FN_RAND_HEU}" > "${FN_SUM_RAND_HEU}"

    #while IFS= read -r -u3 a && IFS= read -r -u4 b; do
    #    #printf '%s is good in %s\n' "$l1" "$l2"
    #    N=$(echo $a | awk '{print $1}')
    #    S1=$(echo $a | awk '{print $2}')
    #    D1=$(echo $a | awk '{print $3}')
    #    S2=$(echo $b | awk '{print $2}')
    #    D2=$(echo $b | awk '{print $3}')
    #    echo "$N & $S1 & $S2 & $D1 & $D2 \\\\"
    #done 3<${FN_SUM_DING} 4<${FN_SUM_RAND}
    echo "%\begin{table}[h!t] \caption{The convergence steps and the size of dominating set by the \algoding~ and the \algorand~ algorithm on XXX graphs.} \label{tab:comparealgorithmxxx}" > "${FN_TABLE}"
    echo "    \begin{center}" >> "${FN_TABLE}"
    echo "        \begin{tabular}{|r|r|c|c|r|c|c|}" >> "${FN_TABLE}"
    echo "            \hline  & \multicolumn{3}{c|}{\textbf{\textit{Steps}}} & \multicolumn{3}{c|}{\textbf{\textit{Dominating Set Size}}} \\\\" >> "${FN_TABLE}"
    echo "            \hline  \textbf{Nodes} & \textbf{\algoding} & \textbf{\algorand} & \textbf{\algorand} heuristic & \textbf{\algoding} & \textbf{\algorand} & \textbf{\algorand} heuristic \\\\" >> "${FN_TABLE}"
    echo "            \hline \hline" >> "${FN_TABLE}"
    while IFS= read -r -u3 a && IFS= read -r -u4 b && IFS= read -r -u5 c; do
        #printf '%s is good in %s\n' "$l1" "$l2"
        N=$(echo $a | awk '{print $1}')
        S1=$(echo $a | awk '{print $2}')
        D1=$(echo $a | awk '{print $3}')
        S2=$(echo $b | awk '{print $2}')
        D2=$(echo $b | awk '{print $3}')
        S3=$(echo $c | awk '{print $2}')
        D3=$(echo $c | awk '{print $3}')
        echo "$N & $S1 & $S2 & $S3 & $D1 & $D2 & $D3 \\\\ \hline"
    done 3<${FN_SUM_DING} 4<${FN_SUM_RAND} 5<${FN_SUM_RAND_HEU} >> "${FN_TABLE}"
    echo "        \end{tabular}" >> "${FN_TABLE}"
    echo "    \end{center}" >> "${FN_TABLE}"
    echo "%\end{table}" >> "${FN_TABLE}"

    generate_gnuplot_rand "${LIST_GRAPH[$i]}-rand-heuristic" "${LIST_GRAPH_DESC[$i]}"
    gnuplot "${LIST_GRAPH[$i]}-rand-heuristic.gp"

    generate_gnuplot_ding "${LIST_GRAPH[$i]}-ding" "${LIST_GRAPH_DESC[$i]}"
    generate_gnuplot_rand "${LIST_GRAPH[$i]}-rand" "${LIST_GRAPH_DESC[$i]}"
    gnuplot "${LIST_GRAPH[$i]}-ding.gp"
    gnuplot "${LIST_GRAPH[$i]}-rand.gp"


    # put the results to latex source file
    cat << EOF >> "${FN_TEX}"
\section{${LIST_GRAPH_DESC[$i]}}

\begin{figure}[h!t] \centering
	\vspace{-10pt}
	\subfloat[{The run steps by \algoding~ algorithm.} \label{fig:fanstepsding}]{
		\includegraphics[width=0.31\textwidth]{${LIST_GRAPH[$i]}-ding-steps.eps}
	}
	~%add desired spacing between images, e. g. ~, \quad, \qquad etc.
	%(or a blank line to force the subfigure onto a new line)
	%\hspace{-2mm}
	\subfloat[{The run steps by \algorand~ algorithm.}\label{fig:fanstepsrand}]{
		\includegraphics[width=0.31\textwidth]{${LIST_GRAPH[$i]}-rand-steps.eps}
	}
	~
	\subfloat[{The run steps by \algorand~ heuristic algorithm.}\label{fig:fanstepsrand}]{
		\includegraphics[width=0.31\textwidth]{${LIST_GRAPH[$i]}-rand-heuristic-steps.eps}
	}
	\\\\%add desired spacing between images, e. g. ~, \quad, \qquad etc.
	%(or a blank line to force the subfigure onto a new line)
	%\vspace{-4pt}
	\subfloat[{The dominating set size of the graph by \algoding~ algorithm.}\label{fig:fandsetding}]{
		\includegraphics[width=0.31\textwidth]{${LIST_GRAPH[$i]}-ding-dset.eps}
	}
	~%add desired spacing between images, e. g. ~, \quad, \qquad etc.
	%(or a blank line to force the subfigure onto a new line)
	%\hspace{-2mm}
	\subfloat[{The dominating set size of the graph by \algorand~ algorithm.}\label{fig:fandsetrand}]{
		\includegraphics[width=0.31\textwidth]{${LIST_GRAPH[$i]}-rand-dset.eps}
	}
	~
	\subfloat[{The dominating set size of the graph by \algorand~ heuristic algorithm.}\label{fig:fandsetrand}]{
		\includegraphics[width=0.31\textwidth]{${LIST_GRAPH[$i]}-rand-heuristic-dset.eps}
	}
%    \\\\%add desired spacing between images, e. g. ~, \quad, \qquad etc.
%      %(or a blank line to force the subfigure onto a new line)
%    \vspace{-7pt}
%    \subfloat[Trace 3-5 (Android WiFi)\label{fig:trace3-5}]{
%        \includegraphics[width=0.24\textwidth]{trace3-5.eps}
%    }
%    \hspace{-4mm}
	\caption{${LIST_GRAPH_DESC[$i]}. The first two figures show the convergence steps of the \algoding~ and \algorand~ algorithm.
		The last two figures shows the number of dominating set nodes generated by these two algorithms.} \label{fig:dcomp-${LIST_GRAPH[$i]}}
\end{figure}


% the data
\begin{table}[h!t] \caption{The convergence steps and the size of dominating set by the \algoding~ and the \algorand~ algorithm on a ${LIST_GRAPH_DESC[$i]}.} \label{tab:compalgorandf5}
	\input{${FN_TABLE}}
\end{table}

\clearpage
EOF

    i=$((i+1))
done


cat << EOF >> "${FN_TEX}"

\end{document}
EOF
