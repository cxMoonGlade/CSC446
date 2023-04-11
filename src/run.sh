#!/bin/bash
cd /home/ednovas
javac Main.java
previous_seed=10
count=0
for seed_value in 10 20 30 50 12345
do
	sed -i "s/private long seed = $previous_seed;/private long seed = $seed_value;/" TimeTable.java
	while [ $count -lt 100 ]
	do
	  java Main &
	  PID=$!
	  wait $PID
	  count=$(($count+1))
	done
	mv results_1.csv "result_1_100_$seed_value.csv"
	mv results_2.csv "result_2_100_$seed_value.csv"
	mv results_3.csv "result_3_100_$seed_value.csv"
	previous_seed=$seed_value
	javac Main.java
	count=0
done
previous_seed=12345
count=0
for seed_value in 10 20 30 50 12345
do
	sed -i "s/private long seed = $previous_seed;/private long seed = $seed_value;/" TimeTable.java
	while [ $count -lt 10000 ]
	do
	  java Main &
	  PID=$!
	  wait $PID
	  count=$(($count+1))
	done
	mv results_1.csv "result_1_10k_$seed_value.csv"
	mv results_2.csv "result_2_10k_$seed_value.csv"
	mv results_3.csv "result_3_10k_$seed_value.csv"
	previous_seed=$seed_value
	javac Main.java
	count=0
done
