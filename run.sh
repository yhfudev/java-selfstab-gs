#!/bin/sh

# generate gnuplot file for Ding's algorithm (since it is a determinant algorithm, there's only one solution from that algorithm)
function generate_gnuplot_ding ()
{
    PARAM_PREFIX=$1
    shift
    PARAM_ALGORITHM=$1
    shift

    cat << EOF > ${PARAM_PREFIX}.gp
#set terminal pdf color solid lw 1 size 5.83,4.13 font "cmr12" enh
#set pointsize 1
#set output "${PARAM_PREFIX}-steps.pdf"
set terminal postscript eps color enhanced
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



set terminal postscript eps color enhanced
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
set terminal postscript eps color enhanced
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



set terminal postscript eps color enhanced
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


rm *.png *.eps *.dat *.gp

LIST_NODES=( 12 34 102 318 1002 3164 10002 )
#LIST_NODES=( 12 34 102 318  )

#LIST_GRAPH=( rand-f5 rand-fp3 rand-fp10 rand-m5 rand-mp3 rand-mp10 doro fan1l fan2l )
LIST_GRAPH=( rand-f5 rand-fp3 rand-fp10 )
LIST_GRAPH=( doro fan1l )

LIST_GRAPH_DESC=(
    "rand-f5   -- Randomized graph with fixed degree 5"
    "rand-fp3  -- Randomized graph with degree value of 3% nodes"
    "rand-fp10 -- Randomized graph with degree value of 10% nodes"
    "rand-m5   -- Randomized graph with max degree 5"
    "rand-mp3  -- Randomized graph with max degree value of 3% nodes"
    "rand-mp10 -- Randomized graph with max degree value of 10% nodes"
    "doro      -- Dorogovtsev Mendes graph"
    "fan1l     -- 1 layer fan grpah"
    "fan2l     -- 2 layers fan grpah"
    )

i=0
while (( $i < ${#LIST_GRAPH[*]} )) ; do
    j=0
    while (( $j < ${#LIST_NODES[*]} )) ; do
        FN_DATA="selfstab-${LIST_GRAPH[$i]}-${LIST_NODES[$j]}.dgs"
        echo "process ${LIST_GRAPH_DESC[$i]} with nodes ${LIST_NODES[$j]}"

if [ 1 = 0 ]; then
        # generate data
        #java -jar out/simss.jar -g rand -n 1002 -d 10 -l test.dgs -a rand -u
        # cat test.dgs | grep -v "cn " > ${FN_DATA}

        # Ding's linear algorithm
        java -jar out/simss.jar -a ding -i data/${FN_DATA} -o ${LIST_GRAPH[$i]}-ding.dat

        # randomized algorithm
        C=0
        while (( $C < 12 )); do
            java -jar out/simss.jar -a rand -i data/${FN_DATA} -o ${LIST_GRAPH[$i]}-rand.dat
            C=$(($C + 1))
        done
fi

        # randomized algorithm with heuristic mode on
        C=0
        while (( $C < 12 )); do
            java -jar out/simss.jar -a rand -i data/${FN_DATA} -o ${LIST_GRAPH[$i]}-rand-heuristic.dat -u
            C=$(($C + 1))
        done

        j=$((j+1))
    done
    generate_gnuplot_rand "${LIST_GRAPH[$i]}-rand-heuristic" "${LIST_GRAPH_DESC[$i]}"
    gnuplot "${LIST_GRAPH[$i]}-rand-heuristic.gp"

if [ 1 = 0 ]; then
    generate_gnuplot_ding "${LIST_GRAPH[$i]}-ding" "${LIST_GRAPH_DESC[$i]}"
    generate_gnuplot_rand "${LIST_GRAPH[$i]}-rand" "${LIST_GRAPH_DESC[$i]}"
    gnuplot "${LIST_GRAPH[$i]}-ding.gp"
    gnuplot "${LIST_GRAPH[$i]}-rand.gp"
fi

    i=$((i+1))
done
